package com.epic.animals.entity;

import com.epic.animals.ModEntities;
import com.epic.animals.tag.ModItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
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
import net.minecraft.world.level.LevelAccessor;
import org.jspecify.annotations.Nullable;

public class Capybara extends Animal {
    public Capybara(EntityType<? extends Capybara> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes().add(Attributes.MAX_HEALTH, 8.0).add(Attributes.MOVEMENT_SPEED, 0.2F);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 2.0));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25, this::isFood, false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(ModItemTags.CAPYBARA_FOOD);
    }

    @Override
    public @Nullable Capybara getBreedOffspring(ServerLevel level, AgeableMob partner) {
        return ModEntities.CAPYBARA.get().create(level, EntitySpawnReason.BREEDING);
    }

    @Override
    public void playAmbientSound() {
        SoundEvent ambient = this.getAmbientSound();
        if (ambient == SoundEvents.FOX_SCREECH) {
            this.playSound(ambient, 2.0F, this.getVoicePitch());
        } else {
            super.playAmbientSound();
        }
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        if (this.isSleeping()) {
            return SoundEvents.FOX_SLEEP;
        }
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
        return level.getBlockState(pos.below()).is(BlockTags.FROGS_SPAWNABLE_ON)
                && isBrightEnoughToSpawn(level, pos);
    }
}
