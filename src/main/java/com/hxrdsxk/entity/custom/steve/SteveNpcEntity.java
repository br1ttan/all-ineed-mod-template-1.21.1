package com.hxrdsxk.entity.custom.steve;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class SteveNpcEntity extends PathAwareEntity {

    private PlayerEntity followTarget = null;

    public SteveNpcEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    public void setFollowTarget(PlayerEntity player) {
        this.followTarget = player;
    }

    public PlayerEntity getFollowTarget() {
        return followTarget;
    }

    public static DefaultAttributeContainer.Builder createSteveAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new FollowPlayerGoal(this, 1.0, 2.0f));
    }
}
