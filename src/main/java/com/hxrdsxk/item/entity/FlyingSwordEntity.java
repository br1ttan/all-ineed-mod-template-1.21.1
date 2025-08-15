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
import net.minecraft.nbt.NbtElement;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.UUID;

public class FlyingSwordEntity extends Entity {
    public float yawAngle = Float.NaN;

    private static final TrackedData<ItemStack> SWORD_STACK =
            DataTracker.registerData(FlyingSwordEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);

    private LivingEntity target;
    public int age = 0;
    private boolean dealtDamage;
    private LivingEntity owner;
    private UUID ownerUuid;
    public static final int SPIN_DURATION = 10;

    private Vec3d initialDirection = Vec3d.ZERO;

    public FlyingSwordEntity(EntityType<? extends FlyingSwordEntity> type, World world) {
        super(type, world);
    }

    public FlyingSwordEntity(World world, LivingEntity owner, ItemStack stack, LivingEntity target) {
        this(ModItemEntities.FLYING_SWORD, world);
        this.owner = owner;
        this.target = target;
        this.ownerUuid = owner.getUuid();
        this.noClip = true;
        this.setSwordStack(stack);
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

    public LivingEntity getOwner() {
        return this.owner;
    }

    public LivingEntity getTarget() {
        return target;
    }

    public void setInitialDirection(Vec3d direction) {
        this.initialDirection = direction;
    }

    public Vec3d getInitialDirection() {
        return initialDirection;
    }

    @Override
    public void tick() {
        super.tick();
        age++;
        World world = getWorld();

        if (!world.isClient) {
            if (target == null || !target.isAlive() || age > 200) {
                discard();
                return;
            }
        }

        if (target == null) return;

        if (age < SPIN_DURATION) {
            this.setVelocity(Vec3d.ZERO);
        } else {
            Vec3d targetPos = target.getPos().add(0, target.getHeight() / 2.0, 0);
            Vec3d direction = targetPos.subtract(this.getPos()).normalize();

            // Первый раз рассчитываем угол
            if (Double.isNaN(yawAngle) && direction.lengthSquared() > 0.0001) {
                yawAngle = (float) Math.toDegrees(Math.atan2(direction.x, direction.z));
            }

            // Ускорение: прибавляем к текущей скорости направление * ускорение
            Vec3d acceleration = direction.multiply(0.05); // сила ускорения
            Vec3d newVelocity = this.getVelocity().add(acceleration);

            // Ограничиваем максимальную скорость
            double maxSpeed = 1.2;
            if (newVelocity.length() > maxSpeed) {
                newVelocity = newVelocity.normalize().multiply(maxSpeed);
            }

            this.setVelocity(newVelocity);
            this.move(MovementType.SELF, this.getVelocity());
        }

        if (!world.isClient && !dealtDamage && this.getBoundingBox().intersects(target.getBoundingBox())) {
            target.damage(this.getDamageSources().indirectMagic(this, getOwner()), 5.0f);
            dealtDamage = true;
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0F, 1.0F);
            discard();
        }
    }


    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        World world = getWorld();
        ItemStack stack = getSwordStack();
        if (!stack.isEmpty()) {
            NbtElement stackNbt = stack.encode(world.getRegistryManager());
            if (stackNbt instanceof NbtCompound compound) {
                nbt.put("SwordStack", compound);
            }
        }
        if (ownerUuid != null) {
            nbt.putUuid("OwnerUUID", ownerUuid);
        }
        nbt.putInt("Age", age);
        nbt.putBoolean("DealtDamage", dealtDamage);

        // Сохраняем initialDirection
        nbt.putDouble("InitDirX", initialDirection.x);
        nbt.putDouble("InitDirY", initialDirection.y);
        nbt.putDouble("InitDirZ", initialDirection.z);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        World world = getWorld();
        if (nbt.contains("SwordStack", 10)) {
            NbtCompound compound = nbt.getCompound("SwordStack");
            ItemStack stack = ItemStack.fromNbtOrEmpty(world.getRegistryManager(), compound);
            setSwordStack(stack);
        }
        if (nbt.containsUuid("OwnerUUID")) {
            ownerUuid = nbt.getUuid("OwnerUUID");
        }

        age = nbt.getInt("Age");
        dealtDamage = nbt.getBoolean("DealtDamage");

        // Читаем initialDirection
        double x = nbt.getDouble("InitDirX");
        double y = nbt.getDouble("InitDirY");
        double z = nbt.getDouble("InitDirZ");
        initialDirection = new Vec3d(x, y, z);
    }
}
