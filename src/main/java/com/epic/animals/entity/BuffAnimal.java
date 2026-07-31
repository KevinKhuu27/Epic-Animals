package com.epic.animals.entity;

import com.epic.animals.entity.aura.BuffAura;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public abstract class BuffAnimal extends TamableAnimal {
    private static final int AURA_INTERVAL_TICKS = 80;
    private static final int AURA_EFFECT_DURATION_TICKS = 180;

    protected BuffAnimal(EntityType<? extends TamableAnimal> type, Level level) {
        super(type, level);
    }

    protected abstract Holder<MobEffect> getAuraEffect();

    protected double getAuraRadius() {
        return 25.0D;
    }

    protected int getAuraAmplifier() {
        return 0;
    }

    protected boolean isAuraActive() {
        return isTame();
    }

    protected boolean shouldBuff(Player player) {
        return true;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        BuffAura.tick(this, isAuraActive(), getAuraEffect(), getAuraRadius(), getAuraAmplifier());
    }
}


