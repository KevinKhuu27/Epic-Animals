package com.epic.animals.entity;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.List;

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

        if (level().isClientSide()) {
            return;
        }

        if (tickCount % AURA_INTERVAL_TICKS != 0) {
            return;
        }

        if (!isAuraActive()) { return; }

        Holder<MobEffect> effect = getAuraEffect();
        if (effect == null) { return; }

        List<Player> nearby = level().getEntitiesOfClass(
                Player.class,
                getBoundingBox().inflate(getAuraRadius()),
                this::shouldBuff
        );

        for (Player player : nearby) {
            player.addEffect(new MobEffectInstance(
                    effect,
                    AURA_EFFECT_DURATION_TICKS,
                    getAuraAmplifier(),
                    true,
                    false
            ));
        }
    }
}


