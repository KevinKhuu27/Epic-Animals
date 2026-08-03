package com.epic.animals.entity;

import com.epic.animals.EpicAnimals;
import com.epic.animals.ModEntities;

import com.epic.animals.entity.goal.RhinoBeetleBreedGoal;
import com.epic.animals.tag.ModBlockTags;
import com.epic.animals.tag.ModItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.event.EventHooks;
import org.jspecify.annotations.Nullable;

public class RhinoBeetle extends BuffAnimal implements NeutralMob {

    public final AnimationState idleAnimationState = new AnimationState();
    private static final EntityDataAccessor<Boolean> DATA_HAS_EGG = SynchedEntityData.defineId(RhinoBeetle.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_LAYING_EGG = SynchedEntityData.defineId(RhinoBeetle.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Long> DATA_ANGER_END_TIME = SynchedEntityData.defineId(RhinoBeetle.class, EntityDataSerializers.LONG);

    public int layEggCounter;
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 30);
    @Nullable private EntityReference<LivingEntity> persistentAngerTarget;

    public RhinoBeetle(EntityType<? extends RhinoBeetle> type, Level level) {
        super(type, level);
        this.setTame(false, false);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 100.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.15D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D);
    }

    @Override
    public Holder<MobEffect> getAuraEffect() {
        return MobEffects.RESISTANCE;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(3, new FollowOwnerGoal(this, 1.5, 10.0F, 2.0F));
        this.goalSelector.addGoal(4, new RhinoBeetleBreedGoal(this, 1.0));
        this.goalSelector.addGoal(5, new TemptGoal(this, 1.25, this::isFood, false));
        this.goalSelector.addGoal(5, new TemptGoal(this, 1.25, stack -> !this.isTame() && stack.is(ModItemTags.RHINO_BEETLE_TAMING_FOOD), false));
        this.goalSelector.addGoal(6, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(3, new ResetUniversalAngerTargetGoal<>(this, true));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.idleAnimationState.startIfStopped(this.tickCount);
        }
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(ModItemTags.RHINO_BEETLE_FOOD);
    }

    @Nullable
    @Override
    public RhinoBeetle getBreedOffspring(ServerLevel level, AgeableMob partner) {
        RhinoBeetle baby = ModEntities.RHINO_BEETLE.get().create(level, EntitySpawnReason.BREEDING);
        if (baby != null && this.isTame()) {
            baby.setOwnerReference(this.getOwnerReference());
            baby.setTame(true, true);
        }
        return baby;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (this.isTame()) {
            if (this.isFood(itemStack) && this.getHealth() < this.getMaxHealth()) {
                this.feed(player, hand, itemStack, 2.0F, 2.0F);
                return InteractionResult.SUCCESS;
            }
            InteractionResult result = super.mobInteract(player, hand);
            if (!result.consumesAction() && this.isOwnedBy(player)) {
                if (!this.level().isClientSide()) {
                    this.setOrderedToSit(!this.isOrderedToSit());
                    this.jumping = false;
                    this.navigation.stop();
                    this.setTarget(null);
                    this.setPersistentAngerEndTime(-1L);
                }
                return InteractionResult.SUCCESS;
            }
            return result;
        } else if (!this.level().isClientSide() && itemStack.is(ModItemTags.RHINO_BEETLE_TAMING_FOOD)) {
            itemStack.consume(1, player);
            this.tryToTame(player);
            return InteractionResult.SUCCESS_SERVER;
        }

        return super.mobInteract(player, hand);
    }

    private void tryToTame(Player player) {
        if (this.random.nextInt(3) == 0 && !EventHooks.onAnimalTame(this, player)) {
            this.tame(player);
            this.navigation.stop();
            this.setTarget(null);
            this.setOrderedToSit(true);
            this.level().broadcastEntityEvent(this, (byte)7);
        } else {
            this.level().broadcastEntityEvent(this, (byte)6);
        }
    }

    @Override
    protected void applyTamingSideEffects() {
        if (this.isTame()) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue((double)120.0F);
            this.setHealth(this.getMaxHealth());
        } else {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue((double)100.0F);
        }
    }

    @Override
    public boolean doHurtTarget(ServerLevel level, Entity target) {
        if (target instanceof LivingEntity victim && this.isOwnedBy(victim)) {
            return false;
        }
        boolean hit = super.doHurtTarget(level, target);
        if (hit && !this.isBaby() && target instanceof LivingEntity victim) {
            double kbRes  = victim.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
            double launch = 0.4 * Math.max(0.0, 1.0 - kbRes);
            victim.setDeltaMovement(victim.getDeltaMovement().add(0.0, launch, 0.0));
            victim.hurtMarked = true;
        }
        return hit;
    }

    @Override
    public boolean canFallInLove() {
        return super.canFallInLove() && !this.hasEgg();
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        if (this.isOwnedBy(target)) return false;
        if (this.isBaby() || this.isOrderedToSit()) return false;
        return super.canAttack(target);
    }

    public boolean hasEgg() { return this.entityData.get(DATA_HAS_EGG); }

    public void setHasEgg(boolean hasEgg) { this.entityData.set(DATA_HAS_EGG, hasEgg); }

    public boolean isLayingEgg() { return this.entityData.get(DATA_LAYING_EGG); }

    public void setLayingEgg(boolean laying) {
        this.layEggCounter = laying ? 1 : 0;
        this.entityData.set(DATA_LAYING_EGG, laying);
    }

    @Override
    public long getPersistentAngerEndTime() {
        return this.entityData.get(DATA_ANGER_END_TIME);
    }

    @Override
    public void setPersistentAngerEndTime(long endTime) {
        this.entityData.set(DATA_ANGER_END_TIME, endTime);
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setTimeToRemainAngry((long) PERSISTENT_ANGER_TIME.sample(this.random));
    }

    @Override
    public @Nullable EntityReference<LivingEntity> getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable EntityReference<LivingEntity> target) {
        this.persistentAngerTarget = target;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_HAS_EGG, false);
        builder.define(DATA_LAYING_EGG, false);
        builder.define(DATA_ANGER_END_TIME, -1L);
    }

    @Override
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("HasEgg", this.hasEgg());
        this.addPersistentAngerSaveData(output);
    }

    @Override
    public void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.setHasEgg(input.getBooleanOr("HasEgg", false));
        this.readPersistentAngerSaveData(this.level(), input);
    }

    @Override
    protected void customServerAiStep(ServerLevel level) {
        super.customServerAiStep(level);
        this.updatePersistentAnger(level, true);
        if (!this.hasEgg()) {
            return;
        }
        BlockPos eggPos = this.blockPosition();
        if (!level.getBlockState(eggPos.below()).isFaceSturdy(level, eggPos.below(), Direction.UP)) {
            return;
        }
        if (!this.isLayingEgg()) {
            this.setLayingEgg(true);
        }
        this.getNavigation().stop();
        if (this.layEggCounter > this.random.nextInt(200) + 50) {
            level.playSound(null, eggPos, SoundEvents.TURTLE_LAY_EGG, SoundSource.BLOCKS,
                    0.3F, 0.9F + level.getRandom().nextFloat() * 0.2F);
            level.setBlock(eggPos, EpicAnimals.RHINO_BEETLE_EGG.get().defaultBlockState(), 3);
            level.gameEvent(GameEvent.BLOCK_PLACE, eggPos, GameEvent.Context.of(this, level.getBlockState(eggPos)));
            this.setHasEgg(false);
            this.setLayingEgg(false);
            this.setInLoveTime(600);
        } else {
            this.layEggCounter++;
            if (this.layEggCounter % 5 == 0) {
                BlockPos below = eggPos.below();
                BlockState groundState = level.getBlockState(below);
                level.sendParticles(
                        new BlockParticleOption(ParticleTypes.BLOCK, groundState),
                        this.getX(), this.getY() + 0.1, this.getZ(),
                        8,
                        0.2, 0.0, 0.2,
                        0.02);
            }
        }
    }

    public static boolean checkRhinoBeetleSpawnRules(
            EntityType<? extends Animal> type,
            LevelAccessor level,
            EntitySpawnReason reason,
            BlockPos pos,
            RandomSource random) {
        return level.getBlockState(pos.below()).is(ModBlockTags.RHINO_BEETLE_SPAWNABLE_ON)
                && isBrightEnoughToSpawn(level, pos);
    }
}
