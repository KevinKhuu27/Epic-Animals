package com.epic.animals.entity.aura;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.function.Predicate;

public final class BuffAura {
    public static final int INTERVAL_TICKS = 80;
    public static final int DURATION_TICKS = 180;
    public static final double DEFAULT_RADIUS = 25.0D;

    private BuffAura() {}

    public static void tick(Mob source, boolean active, Holder<MobEffect> effect,
                            double radius, int amplifier) {
        tick(source, active, effect, radius, amplifier, player -> true);
    }

    public static void tick(Mob source, boolean active, Holder<MobEffect> effect,
                            double radius, int amplifier, Predicate<Player> filter) {
        Level level = source.level();

        if (level.isClientSide()) {
            return;
        }
        if (source.tickCount % INTERVAL_TICKS != 0) {
            return;
        }
        if (!active || effect == null) {
            return;
        }

        List<Player> nearby = level.getEntitiesOfClass(
                Player.class, source.getBoundingBox().inflate(radius), filter);
        for (Player player : nearby) {
            player.addEffect(new MobEffectInstance(effect, DURATION_TICKS, amplifier, true, false));
        }
    }
}
