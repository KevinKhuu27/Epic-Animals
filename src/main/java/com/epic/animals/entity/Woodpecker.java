package com.epic.animals.entity;

import com.epic.animals.ModEntities;
import com.epic.animals.entity.aura.BuffAura;
import com.epic.animals.tag.ModBlockTags;
import com.epic.animals.tag.ModItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;
import org.jspecify.annotations.Nullable;

public class Woodpecker extends TamableAnimal {
    public float flap;
    public float flapSpeed;
    public float oFlapSpeed;
    public float oFlap;
    private float flapping = 1.0F;
    private float nextFlap = 1.0F;

    public Woodpecker(EntityType<? extends Woodpecker> type, Level level) {
        super(type, level);
        this.moveControl = new FlyingMoveControl(this, 10, false);
        this.setPathfindingMalus(PathType.FIRE_IN_NEIGHBOR, -1.0F);
        this.setPathfindingMalus(PathType.FIRE, -1.0F);
        this.setPathfindingMalus(PathType.COCOA, -1.0F);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new FollowOwnerGoal(this, 1.5, 10.0F, 2.0F));
        this.goalSelector.addGoal(3, new PanicGoal(this, 1.25));
        this.goalSelector.addGoal(4, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(5, new TemptGoal(this, 1.25, this::isFood, false));
        this.goalSelector.addGoal(5, new TemptGoal(this, 1.25, stack -> !this.isTame() && stack.is(ModItemTags.WOODPECKER_TAMING_FOOD), false));
        this.goalSelector.addGoal(6, new FollowParentGoal(this, 1.25));
        this.goalSelector.addGoal(7, new WoodpeckerWanderGoal(this, 1.0));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.FLYING_SPEED, 0.4)
                .add(Attributes.MOVEMENT_SPEED, 0.2);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation flyingPathNavigation = new FlyingPathNavigation(this, level);
        flyingPathNavigation.setCanOpenDoors(false);
        flyingPathNavigation.setCanFloat(true);
        return flyingPathNavigation;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (!this.isTame() && itemStack.is(ModItemTags.WOODPECKER_TAMING_FOOD)) {
            if (!this.level().isClientSide()) {
                this.tryToTame(player);
            }
            return InteractionResult.SUCCESS;
        }

        if (this.isTame() && itemStack.is(ModItemTags.WOODPECKER_TAMING_FOOD)
                && this.getHealth() < this.getMaxHealth()) {
            this.usePlayerItem(player, hand, itemStack);
            if (!this.level().isClientSide()) {
                this.feed(player, hand, itemStack, 2.0F, 2.0F);
            }
            return InteractionResult.SUCCESS;
        }

        InteractionResult result = super.mobInteract(player, hand);
        if (!result.consumesAction() && this.isTame() && this.isOwnedBy(player) && !this.isFlying()) {
            if (!this.level().isClientSide()) {
                this.setOrderedToSit(!this.isOrderedToSit());
                this.jumping = false;
                this.navigation.stop();
                this.setTarget(null);
            }
            return InteractionResult.SUCCESS;
        }
        return result;
    }

    private void tryToTame(Player player) {
        if (this.random.nextInt(3) == 0 && !EventHooks.onAnimalTame(this, player)) {
            this.tame(player);
            this.level().broadcastEntityEvent(this, (byte) 7);
        } else {
            this.level().broadcastEntityEvent(this, (byte) 6);
        }
    }

    @Override
    protected void applyTamingSideEffects() {
        if (this.isTame()) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(20.0);
            this.setHealth(this.getMaxHealth());
        } else {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(10.0);
        }
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(ModItemTags.WOODPECKER_FOOD);
    }

    @Override
    public @Nullable Woodpecker getBreedOffspring(ServerLevel level, AgeableMob partner) {
        Woodpecker baby = ModEntities.WOODPECKER.get().create(level, EntitySpawnReason.BREEDING);
        if (baby != null && this.isTame()) {
            baby.setOwnerReference(this.getOwnerReference());
            baby.setTame(true, true);
        }
        return baby;
    }

    @Override
    public boolean causeFallDamage(double fallDistance, float damageModifier, DamageSource source) {
        return false;
    }

    public void aiStep() {
        super.aiStep();
        this.calculateFlapping();
        BuffAura.tick(this, this.isTame(), MobEffects.HASTE, BuffAura.DEFAULT_RADIUS, 0);
    }

    private void calculateFlapping() {
        this.oFlap = this.flap;
        this.oFlapSpeed = this.flapSpeed;
        this.flapSpeed += (float)(!this.onGround() && !this.isPassenger() ? 4 : -1) * 0.3F;
        this.flapSpeed = Mth.clamp(this.flapSpeed, 0.0F, 1.0F);
        if (!this.onGround() && this.flapping < 1.0F) {
            this.flapping = 1.0F;
        }

        this.flapping *= 0.9F;
        Vec3 movement = this.getDeltaMovement();
        if (!this.onGround() && movement.y < (double)0.0F) {
            this.setDeltaMovement(movement.multiply((double)1.0F, 0.6, (double)1.0F));
        }

        this.flap += this.flapping * 2.0F;
    }

    protected boolean isFlapping() {
        return this.flyDist > this.nextFlap;
    }

    protected void onFlap() {
        this.playSound(SoundEvents.PARROT_FLY, 0.15F, 1.0F);
        this.nextFlap = this.flyDist + this.flapSpeed / 2.0F;
    }

    public boolean isFlying() {
        return !this.onGround();
    }

    private static class WoodpeckerWanderGoal extends WaterAvoidingRandomFlyingGoal {
        public WoodpeckerWanderGoal(PathfinderMob mob, double speedModifier) {
            super(mob, speedModifier);
        }

        protected @Nullable Vec3 getPosition() {
            Vec3 pos = null;
            if (this.mob.isInWater()) {
                pos = LandRandomPos.getPos(this.mob, 15, 15);
            }

            if (this.mob.getRandom().nextFloat() >= this.probability) {
                pos = this.getTreePos();
            }

            return pos == null ? super.getPosition() : pos;
        }

        private @Nullable Vec3 getTreePos() {
            BlockPos mobPos = this.mob.blockPosition();
            BlockPos.MutableBlockPos abovePos = new BlockPos.MutableBlockPos();
            BlockPos.MutableBlockPos belowPos = new BlockPos.MutableBlockPos();

            for (BlockPos pos : BlockPos.betweenClosed(Mth.floor(this.mob.getX() - (double) 3.0F), Mth.floor(this.mob.getY() - (double) 6.0F), Mth.floor(this.mob.getZ() - (double) 3.0F), Mth.floor(this.mob.getX() + (double) 3.0F), Mth.floor(this.mob.getY() + (double) 6.0F), Mth.floor(this.mob.getZ() + (double) 3.0F))) {
                if (!mobPos.equals(pos)) {
                    BlockState state = this.mob.level().getBlockState(belowPos.setWithOffset(pos, Direction.DOWN));
                    boolean canSitOn = state.getBlock() instanceof LeavesBlock || state.is(BlockTags.LOGS);
                    if (canSitOn && this.mob.level().isEmptyBlock(pos) && this.mob.level().isEmptyBlock(abovePos.setWithOffset(pos, Direction.UP))) {
                        return Vec3.atBottomCenterOf(pos);
                    }
                }
            }

            return null;
        }
    }

    public static boolean checkWoodpeckerSpawnRules(
            EntityType<? extends Animal> type,
            LevelAccessor level,
            EntitySpawnReason reason,
            BlockPos pos,
            RandomSource random) {
        return level.getBlockState(pos.below()).is(ModBlockTags.WOODPECKER_SPAWNABLE_ON)
                && isBrightEnoughToSpawn(level, pos);
    }
}
