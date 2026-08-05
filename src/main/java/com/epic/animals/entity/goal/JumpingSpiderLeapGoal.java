package com.epic.animals.entity.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

/**
 * Leap-at-target goal for the jumping spider.
 *
 * <p>Unlike vanilla {@code LeapAtTargetGoal}, which launches with a FIXED magnitude
 * (a normalized direction times a hardcoded ~0.4, carrying the mob only ~3 blocks no
 * matter the trigger distance), this goal scales the horizontal launch by the actual
 * gap to the target, so it can cover 5-8 blocks. Two independent knobs:
 *
 *   - horizontalPower : reach dial. Horizontal launch velocity per block of distance.
 *                       Because vertical launch is fixed (constant airtime), landing
 *                       distance is ~linear in horizontal velocity: distance ~= launch / 0.12
 *                       under vanilla air-drag (~0.91/tick) + gravity (~0.08/tick).
 *                       Start ~0.12 and tune live.
 *   - verticalPower   : arc height / airtime. Higher = floatier, higher, and (because of
 *                       longer airtime) slightly farther. Re-tune horizontalPower if changed.
 *
 * <p>The goal only acts when the mob already HAS a target. Target acquisition (and its
 * !isTame() + not-holding-a-calming-item gating) lives in the target-selector goal, so
 * this leap is automatically inert on a tamed or pacified spider — no gate needed here.
 *
 * <p>VERIFY IN IDE (Ctrl+B), per the 26.2 handoff notes — treat constants as starting points:
 *   - {@code onGround()} vs {@code isOnGround()} for the ground check in this MC/NeoForge build.
 *   - the vanilla air-drag / gravity constants above (they drive the ~0.12 figure).
 *   - whether you need {@code this.mob.hasImpulse = true;} in start() for the velocity to
 *     sync to the client cleanly (add it if the leap looks server-only / rubber-bands).
 *
 * <p>This is flat-ground math: the launch is horizontal + a fixed lift, so uphill targets
 * won't be reached cleanly. Fine for mostly-flat forests; add a dy term to verticalPower
 * if you want it to leap onto ledges.
 */
public class JumpingSpiderLeapGoal extends Goal {
    private final PathfinderMob mob;
    private LivingEntity target;

    private final double minRange;
    private final double maxRange;
    private final double horizontalPower;
    private final double verticalPower;
    private final int cooldownTicks;
    private int cooldown;

    public JumpingSpiderLeapGoal(PathfinderMob mob, double minRange, double maxRange,
                                 double horizontalPower, double verticalPower, int cooldownTicks) {
        this.mob = mob;
        this.minRange = minRange;
        this.maxRange = maxRange;
        this.horizontalPower = horizontalPower;
        this.verticalPower = verticalPower;
        this.cooldownTicks = cooldownTicks;
        this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.cooldown > 0) {
            this.cooldown--;
            return false;
        }
        if (!this.mob.onGround()) {
            return false;
        }
        this.target = this.mob.getTarget();
        if (this.target == null || !this.target.isAlive()) {
            return false;
        }

        double dx = this.target.getX() - this.mob.getX();
        double dz = this.target.getZ() - this.mob.getZ();
        double horizSqr = dx * dx + dz * dz;
        return horizSqr >= this.minRange * this.minRange
            && horizSqr <= this.maxRange * this.maxRange;
    }

    @Override
    public boolean canContinueToUse() {
        return !this.mob.onGround();
    }

    @Override
    public void start() {
        double dx = this.target.getX() - this.mob.getX();
        double dz = this.target.getZ() - this.mob.getZ();
        Vec3 flat = new Vec3(dx, 0.0, dz);
        double dist = flat.length();
        if (dist < 1.0E-4) {
            return;
        }

        Vec3 launch = flat.normalize().scale(dist * this.horizontalPower);

        this.mob.setDeltaMovement(launch.x, this.verticalPower, launch.z);
        this.mob.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
        this.cooldown = this.cooldownTicks;
    }
}
