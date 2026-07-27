package com.epic.animals.entity;

import com.epic.animals.ModEntities;
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
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.neoforge.event.EventHooks;
import org.jspecify.annotations.Nullable;

public class Capybara extends BuffAnimal {
    public Capybara(EntityType<? extends Capybara> type, Level level) {
        super(type, level);
        this.setTame(false, false);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes().add(Attributes.MAX_HEALTH, 8.0).add(Attributes.MOVEMENT_SPEED, 0.2F);
    }

    @Override
    protected Holder<MobEffect> getAuraEffect() {
        return MobEffects.WATER_BREATHING;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 2.0));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(3, new FollowOwnerGoal(this, 1.5, 10.0F, 2.0F));
        this.goalSelector.addGoal(4, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(5, new TemptGoal(this, 1.25, this::isFood, false));
        this.goalSelector.addGoal(5, new TemptGoal(this, 1.25, stack -> !this.isTame() && stack.is(ModItemTags.CAPYBARA_TAMING_FOOD), false));
        this.goalSelector.addGoal(6, new FollowParentGoal(this, 1.25));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(ModItemTags.CAPYBARA_FOOD);
    }

    @Override
    public @Nullable Capybara getBreedOffspring(ServerLevel level, AgeableMob partner) {
        Capybara baby = ModEntities.CAPYBARA.get().create(level, EntitySpawnReason.BREEDING);
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
                this.setOrderedToSit(!this.isOrderedToSit());
                this.jumping = false;
                this.navigation.stop();
                this.setTarget(null);
                return InteractionResult.SUCCESS.withoutItem();
            }
            return result;
        } else if (!this.level().isClientSide() && itemStack.is(ModItemTags.CAPYBARA_TAMING_FOOD)) {
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
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue((double)40.0F);
            this.setHealth(40.0F);
        } else {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue((double)8.0F);
        }
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return SoundEvents.FOX_AMBIENT;
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.FOX_HURT;
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return SoundEvents.FOX_DEATH;
    }

    public static boolean checkCapybaraSpawnRules(
            EntityType<? extends Animal> type,
            LevelAccessor level,
            EntitySpawnReason reason,
            BlockPos pos,
            RandomSource random) {
        return level.getBlockState(pos.below()).is(ModBlockTags.CAPYBARA_SPAWNABLE_ON)
                && isBrightEnoughToSpawn(level, pos);
    }
}
