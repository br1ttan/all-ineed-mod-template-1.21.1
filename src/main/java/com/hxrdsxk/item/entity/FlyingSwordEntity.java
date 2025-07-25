package com.hxrdsxk.item.entity;

import com.hxrdsxk.item.ModItemEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FlyingSwordEntity extends Entity {
    private static final TrackedData<ItemStack> SWORD_STACK =
            DataTracker.registerData(FlyingSwordEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);

    private LivingEntity target;
    public int age = 0;
    private boolean dealtDamage;
    private LivingEntity owner;

    public FlyingSwordEntity(EntityType<? extends FlyingSwordEntity> type, World world) {
        super(type, world);
    }

    public FlyingSwordEntity(World world, LivingEntity owner, ItemStack stack, LivingEntity target) {
        this(ModItemEntities.FLYING_SWORD, world);
        this.owner = owner;
        this.target = target;
        this.noClip = true;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(SWORD_STACK, ItemStack.EMPTY);
    }

    public void setSwordStack(ItemStack stack) {
        this.dataTracker.set(SWORD_STACK, stack);
    }

    public ItemStack getSwordStack() {
        return this.dataTracker.get(SWORD_STACK);
    }

    @Override
    public void tick() {
        super.tick();
        this.age++;

        // Проверяем условия деспауна
        if (this.getWorld() == null || target == null || !target.isAlive() || this.age > 200) {
            System.out.println("Меч удалён по причине: " +
                    (getWorld() == null ? "null world" :
                            target == null ? "no target" :
                                    !target.isAlive() ? "target dead" : "timeout"));
            this.discard();
            return;
        }

        // Плавное движение к цели
        Vec3d targetPos = target.getPos().add(0, target.getHeight() / 2.0, 0);
        Vec3d direction = targetPos.subtract(this.getPos()).normalize();

        // Увеличиваем скорость со временем (но не более 0.7)
        double speed = Math.min(0.2 + (age * 0.005), 0.7);
        this.setVelocity(direction.multiply(speed));

        // Применяем движение
        this.move(MovementType.SELF, this.getVelocity());

        // Проверка попадания
        if (!this.getWorld().isClient &&
                this.getPos().distanceTo(targetPos) < 1.5 &&
                !this.dealtDamage)
        {
            target.damage(this.getDamageSources().indirectMagic(this, owner), 5.0f);
            this.dealtDamage = true;
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0F, 1.0F);
            this.discard();
        }
    }

    public LivingEntity getOwner() {
        return this.owner;
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        // TODO: сохранить меч
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        // TODO: сохранить меч
    }
}
