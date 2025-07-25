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
import net.minecraft.nbt.NbtHelper;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.UUID;

public class FlyingSwordEntity extends Entity {
    private static final TrackedData<ItemStack> SWORD_STACK =
            DataTracker.registerData(FlyingSwordEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);

    private LivingEntity target;
    public int age = 0;
    private boolean dealtDamage;
    private LivingEntity owner;

    // Для сохранения владельца в NBT
    private UUID ownerUuid;

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

        Vec3d targetPos = target.getPos().add(0, target.getHeight() / 2.0, 0);
        Vec3d direction = targetPos.subtract(this.getPos()).normalize();

        double speed = Math.min(0.2 + (age * 0.005), 0.7);
        this.setVelocity(direction.multiply(speed));

        this.move(MovementType.SELF, this.getVelocity());

        // Заменяем проверку дистанции на проверку пересечения боксами
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
        // Сохраняем ItemStack меча
        ItemStack stack = getSwordStack();
        if (!stack.isEmpty()) {
            NbtElement stackNbt = stack.encode(world.getRegistryManager());
            if (stackNbt instanceof NbtCompound compound) {
                nbt.put("SwordStack", compound);
            }
        }
        // Сохраняем UUID владельца
        if (ownerUuid != null) {
            nbt.putUuid("OwnerUUID", ownerUuid);
        }

        // Сохраняем age и dealtDamage
        nbt.putInt("Age", age);
        nbt.putBoolean("DealtDamage", dealtDamage);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        World world = getWorld();
        if (nbt.contains("SwordStack", 10)) { // 10 — это ID для Compound
            NbtCompound compound = nbt.getCompound("SwordStack");
            ItemStack stack = ItemStack.fromNbtOrEmpty(world.getRegistryManager(), compound);
            setSwordStack(stack);
        }
        if (nbt.containsUuid("OwnerUUID")) {
            ownerUuid = nbt.getUuid("OwnerUUID");
        }

        age = nbt.getInt("Age");
        dealtDamage = nbt.getBoolean("DealtDamage");
    }

}
