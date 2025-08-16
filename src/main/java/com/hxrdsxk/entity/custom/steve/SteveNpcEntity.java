package com.hxrdsxk.entity.custom.steve;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SteveNpcEntity extends PathAwareEntity {
    private PlayerEntity followTarget;
    private final SimpleInventory inventory = new SimpleInventory(30);
    private int buildCooldown = 0;
    private int attackCooldown = 0;
    private LivingEntity combatTarget;

    public SteveNpcEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        this.inventory.setStack(3, new ItemStack(Items.IRON_SWORD));
        this.inventory.setStack(4, new ItemStack(Items.STONE_PICKAXE));
        this.inventory.setStack(0, new ItemStack(Blocks.DIRT, 16));
    }

    // Регистрация атрибутов с улучшенными характеристиками
    public static DefaultAttributeContainer.Builder createSteveAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 40.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6.0)
                .add(EntityAttributes.GENERIC_ARMOR, 4.0)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32.0);
    }

    @Override
    protected void initGoals() {
        // Боевые цели
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.2, true));
        this.targetSelector.add(2, new RevengeGoal(this));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, HostileEntity.class, true));

        // Поведенческие цели
//        this.goalSelector.add(4, new FollowPlayerGoal(this, 1.0, 5.0f, 3.0f));
        this.goalSelector.add(5, new BuildRandomStructureGoal(this));
        this.goalSelector.add(6, new WanderAroundFarGoal(this, 0.8));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
    }

    @Override
    public void tick() {
        super.tick();
        updateCombatState();
        updateBuilding();
    }

    // Логика боя
    private void updateCombatState() {
        if (attackCooldown > 0) attackCooldown--;

        if (combatTarget != null) {
            if (!combatTarget.isAlive() || distanceTo(combatTarget) > 16) {
                combatTarget = null;
            } else {
                getNavigation().startMovingTo(combatTarget, 1.2);
                if (distanceTo(combatTarget) < 3.0 && attackCooldown <= 0) {
                    tryAttack(combatTarget);
                    attackCooldown = 20; // 1 секунда КД
                }
            }
        }
    }

    // Логика строительства
    private void updateBuilding() {
        if (buildCooldown > 0) {
            buildCooldown--;
        } else if (random.nextInt(200) == 0 && hasBuildingMaterials()) {
            buildRandomBlock();
            buildCooldown = 100; // 5 секунд КД
        }
    }

    private boolean hasBuildingMaterials() {
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (stack != null && stack.getItem().toString().contains("_block")) {
                return true;
            }
        }
        return false;
    }

    private void buildRandomBlock() {
        Vec3d lookVec = getRotationVector();
        BlockPos buildPos = BlockPos.ofFloored(getPos().add(lookVec.multiply(2)));

        World world = this.getWorld();
        if (world.isAir(buildPos)) {
            for (int i = 0; i < inventory.size(); i++) {
                ItemStack stack = inventory.getStack(i);
                if (stack != null && stack.getItem().toString().contains("_block")) {
                    world.setBlockState(buildPos, ((BlockItem) stack.getItem()).getBlock().getDefaultState());
                    stack.decrement(1);
                    break;
                }
            }
        }
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        Entity attacker = source.getAttacker();
        if (attacker instanceof LivingEntity) {
            combatTarget = (LivingEntity) attacker;
        }
        return super.damage(source, amount);
    }

    // Геттеры/сеттеры
    public void setFollowTarget(PlayerEntity player) {
        this.followTarget = player;
    }

    public PlayerEntity getFollowTarget() {
        return followTarget;
    }

    public SimpleInventory getInventory() {
        return inventory;
    }

    // Кастомные цели ИИ
    static class FollowPlayerGoal extends Goal {
        private final SteveNpcEntity npc;
        private final double speed;
        private final float minDistance;
        private final float maxDistance;

        public FollowPlayerGoal(SteveNpcEntity npc, double speed, float minDistance, float maxDistance) {
            this.npc = npc;
            this.speed = speed;
            this.minDistance = minDistance;
            this.maxDistance = maxDistance;
        }

        @Override
        public boolean canStart() {
            return npc.getFollowTarget() != null &&
                    npc.distanceTo(npc.getFollowTarget()) > minDistance;
        }

        @Override
        public void start() {
            npc.getNavigation().startMovingTo(npc.getFollowTarget(), speed);
        }
    }

    static class BuildRandomStructureGoal extends Goal {
        private final SteveNpcEntity npc;
        private BlockPos targetPos;

        public BuildRandomStructureGoal(SteveNpcEntity npc) {
            this.npc = npc;
        }

        @Override
        public boolean canStart() {
            return npc.hasBuildingMaterials() && npc.random.nextInt(100) < 20;
        }

        @Override
        public void start() {
            Vec3d lookVec = npc.getRotationVector();
            targetPos = BlockPos.ofFloored(npc.getPos().add(lookVec.multiply(3)));
            npc.getNavigation().startMovingTo(targetPos.getX(), targetPos.getY(), targetPos.getZ(), 1.0);
        }

        @Override
        public void tick() {
            if (npc.getPos().distanceTo(Vec3d.ofCenter(targetPos)) < 2.0) {
                npc.buildRandomBlock();
            }
        }
    }
}