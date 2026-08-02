package com.epic.animals.entity;

import com.epic.animals.ModEntities;

import com.epic.animals.tag.ModItemTags;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import net.neoforged.neoforge.event.EventHooks;
import org.jspecify.annotations.Nullable;

public class RhinoBeetle extends BuffAnimal {

    public RhinoBeetle(EntityType<? extends RhinoBeetle> type, Level level) {
        super(type, level);
        this.setTame(false, false);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 100.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.15D);
    }

    @Override
    public Holder<MobEffect> getAuraEffect() {
        return MobEffects.RESISTANCE;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 2.0D));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(3, new FollowOwnerGoal(this, 1, 10.0F, 2.0F));
        this.goalSelector.addGoal(4, new BreedGoal(this, 1));
        this.goalSelector.addGoal(5, new TemptGoal(this, 1.25, this::isFood, false));
        this.goalSelector.addGoal(5, new TemptGoal(this, 1.25, stack -> !this.isTame() && stack.is(ModItemTags.RHINO_BEETLE_TAMING_FOOD), false));
        this.goalSelector.addGoal(6, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
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
}
