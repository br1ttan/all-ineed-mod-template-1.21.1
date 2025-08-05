package com.hxrdsxk.entity.custom.steve.goals;

import com.hxrdsxk.entity.custom.steve.SteveNpcEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;

import java.util.EnumSet;

public class FollowPlayerGoal extends Goal {
    private final SteveNpcEntity npc;
    private final double speed;
    private final float minDistance;

    public FollowPlayerGoal(SteveNpcEntity npc, double speed, float minDistance) {
        this.npc = npc;
        this.speed = speed;
        this.minDistance = minDistance;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        PlayerEntity target = npc.getFollowTarget();
        if (target == null || !target.isAlive()) return false;

        double distanceSq = npc.squaredDistanceTo(target);
        return distanceSq > minDistance * minDistance;
    }

    @Override
    public void tick() {
        PlayerEntity target = npc.getFollowTarget();
        if (target != null) {
            npc.getNavigation().startMovingTo(target, speed);
        }
    }
}
