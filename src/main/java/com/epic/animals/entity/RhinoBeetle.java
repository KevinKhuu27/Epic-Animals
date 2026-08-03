package com.epic.animals.entity;

import com.epic.animals.EpicAnimals;
import com.epic.animals.ModEntities;

import com.epic.animals.entity.goal.RhinoBeetleBreedGoal;
import com.epic.animals.tag.ModBlockTags;
import com.epic.animals.tag.ModItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.event.EventHooks;
import org.jspecify.annotations.Nullable;

public class RhinoBeetle extends BuffAnimal {

    public final AnimationState idleAnimationState = new AnimationState();
    private static final EntityDataAccessor<Boolean> DATA_HAS_EGG = SynchedEntityData.defineId(RhinoBeetle.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_LAYING_EGG = SynchedEntityData.defineId(RhinoBeetle.class, EntityDataSerializers.BOOLEAN);
    public int layEggCounter;

    public RhinoBeetle(EntityType<? extends RhinoBeetle> type, Level level) {
        super(type, level);
        this.setTame(false, false);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 100.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.15D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5D);
    }

    @Override
    public Holder<MobEffect> getAuraEffect() {
        return MobEffects.RESISTANCE;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.0D));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(3, new FollowOwnerGoal(this, 1.5, 10.0F, 2.0F));
        this.goalSelector.addGoal(4, new RhinoBeetleBreedGoal(this, 1.0));
        this.goalSelector.addGoal(5, new TemptGoal(this, 1.25, this::isFood, false));
        this.goalSelector.addGoal(5, new TemptGoal(this, 1.25, stack -> !this.isTame() && stack.is(ModItemTags.RHINO_BEETLE_TAMING_FOOD), false));
        this.goalSelector.addGoal(6, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
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
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_HAS_EGG, false);
        builder.define(DATA_LAYING_EGG, false);
    }

    public boolean hasEgg() { return this.entityData.get(DATA_HAS_EGG); }

    public void setHasEgg(boolean hasEgg) { this.entityData.set(DATA_HAS_EGG, hasEgg); }

    public boolean isLayingEgg() { return this.entityData.get(DATA_LAYING_EGG); }

    public void setLayingEgg(boolean laying) {
        this.layEggCounter = laying ? 1 : 0;
        this.entityData.set(DATA_LAYING_EGG, laying);
    }

    @Override
    public boolean canFallInLove() {
        return super.canFallInLove() && !this.hasEgg();
    }

    @Override
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("HasEgg", this.hasEgg());
    }

    @Override
    public void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.setHasEgg(input.getBooleanOr("HasEgg", false));
    }

    @Override
    protected void customServerAiStep(ServerLevel level) {
        super.customServerAiStep(level);
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
