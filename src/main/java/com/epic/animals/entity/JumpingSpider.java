package com.epic.animals.entity;

import com.epic.animals.ModEntities;
import com.epic.animals.entity.goal.JumpingSpiderLeapGoal;
import com.epic.animals.tag.ModBlockTags;
import com.epic.animals.tag.ModItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.neoforge.event.EventHooks;
import org.jspecify.annotations.Nullable;

public class JumpingSpider extends BuffAnimal {

    public JumpingSpider(EntityType<? extends JumpingSpider> entityType, Level level) {
        super(entityType, level);
        this.setTame(false, false);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 16.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.ATTACK_DAMAGE, 3.0)
                .add(Attributes.FOLLOW_RANGE, 16.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new JumpingSpiderLeapGoal(this, 1.0, 20.0, 0.2, 0.42, 30));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 0.75, true));
        this.goalSelector.addGoal(4, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(5, new TemptGoal(this, 1.1, this::isFood, false));
        this.goalSelector.addGoal(5, new TemptGoal(this, 1.1, stack -> !this.isTame() && stack.is(ModItemTags.JUMPING_SPIDER_TAMING_FOOD), false));
        this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.5, 10.0F, 2.0F));
        this.goalSelector.addGoal(7, new FollowParentGoal(this, 1.1));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<Player>(
                this, Player.class, 10, true, false,
                (entity, serverLevel) -> !this.isTame() && !this.isPacifiedBy(entity)));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide()) {
            LivingEntity target = this.getTarget();
            if (target != null && (this.isTame() || this.isPacifiedBy(target))) {
                this.setTarget(null);
            }
        }
    }

    private static boolean isCalmingItem(ItemStack stack) {
        return stack.is(ModItemTags.JUMPING_SPIDER_FOOD) || stack.is(ModItemTags.JUMPING_SPIDER_TAMING_FOOD);
    }

    private boolean isPacifiedBy(LivingEntity entity) {
        return entity.isHolding(JumpingSpider::isCalmingItem);
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
                this.setOrderedToSit(!this.isOrderedToSit());
                this.jumping = false;
                this.navigation.stop();
                this.setTarget(null);
                return InteractionResult.SUCCESS;
            }
            return result;
        } else if (!this.level().isClientSide() && itemStack.is(ModItemTags.JUMPING_SPIDER_TAMING_FOOD)) {
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
            this.level().broadcastEntityEvent(this, (byte) 7);
        } else {
            this.level().broadcastEntityEvent(this, (byte) 6);
        }
    }

    @Override
    protected void applyTamingSideEffects() {
        if (this.isTame()) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue((double)40.0F);
            this.setHealth(this.getMaxHealth());
        } else {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue((double)16.0F);
        }
    }

    @Override
    public Holder<MobEffect> getAuraEffect() {
        return MobEffects.JUMP_BOOST;
    }

    @Override
    @Nullable
    public JumpingSpider getBreedOffspring(ServerLevel level, AgeableMob parent) {
        JumpingSpider child = ModEntities.JUMPING_SPIDER.get().create(level, EntitySpawnReason.BREEDING);
        if (child != null && this.isTame()) {
            child.setOwnerReference(this.getOwnerReference());
            child.setTame(true, true);
        }
        return child;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(ModItemTags.JUMPING_SPIDER_FOOD);
    }

    @Override
    @Nullable
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SPIDER_AMBIENT;
    }

    @Override
    @Nullable
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.SPIDER_HURT;
    }

    @Override
    @Nullable
    protected SoundEvent getDeathSound() {
        return SoundEvents.SPIDER_DEATH;
    }

    public static boolean checkJumpingSpiderSpawnRules(
            EntityType<? extends Animal> type,
            LevelAccessor level,
            EntitySpawnReason reason,
            BlockPos pos,
            RandomSource random) {
        return level.getBlockState(pos.below()).is(ModBlockTags.JUMPING_SPIDER_SPAWNABLE_ON)
                && isBrightEnoughToSpawn(level, pos);
    }
}
