// MagicBlockEntity.java
package com.hxrdsxk.block.entity;

import com.hxrdsxk.block.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

import org.jetbrains.annotations.Nullable;

public class MagicBlockEntity extends BlockEntity implements ImplementedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);
    private float rotation = 0;

    public float openProgress = 0.0F;
    public float pageFlip = 0.0F;
    public float pageFlipPrev = 0.0F;

    public MagicBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MAGIC_BLOCK_ENTITY, pos, state);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    public float getRenderingRotation() {
        rotation += 0.5f;
        if (rotation >= 360) rotation = 0;
        return rotation;
    }

    public void sync() {
        if (world != null && !world.isClient) {
            System.out.println("synced");
            world.markDirty(pos);
            markDirty();
            world.updateListeners(pos, getCachedState(), getCachedState(), 3);
        }
    }

    public void spawnEnchantingParticles(ServerWorld world, BlockPos pos, BlockState state) {
        if (world.isClient) {
            return; // Не делаем ничего на клиенте — сервер отправит сам
        }

        double centerX = pos.getX() + 0.5;
        double centerY = pos.getY() + 1.5;
        double centerZ = pos.getZ() + 0.5;

        for (int i = 0; i < 20; i++) {
            double angle = world.random.nextDouble() * 2 * Math.PI;
            double radius = 0.4 + world.random.nextDouble() * 0.1;

            double offsetX = Math.cos(angle) * radius;
            double offsetY = world.random.nextDouble() * 0.2;
            double offsetZ = Math.sin(angle) * radius;

            double particleX = centerX + offsetX;
            double particleY = centerY + offsetY;
            double particleZ = centerZ + offsetZ;

            // Увеличиваем множитель скорости по X и Z, чтобы частицы быстрее шли в центр
            double velocityX = (centerX - particleX) * 0.3;
            double velocityY = -0.05;
            double velocityZ = (centerZ - particleZ) * 0.3;

            world.spawnParticles(
                    ParticleTypes.ENCHANT,
                    particleX, particleY, particleZ,
                    0,
                    velocityX, velocityY, velocityZ,
                    0.0
            );
        }
    }


    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        Inventories.writeNbt(nbt, inventory, registryLookup);
        nbt.putFloat("OpenProgress", openProgress);
        nbt.putFloat("PageFlip", pageFlip);
        nbt.putFloat("PageFlipPrev", pageFlipPrev);

        System.out.println("writing: " + inventory); // <- это выведет текущее состояние инвентаря на сервере
    }


    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);

        Inventories.readNbt(nbt, inventory, registryLookup);
        openProgress = nbt.getFloat("OpenProgress");
        pageFlip = nbt.getFloat("PageFlip");
        pageFlipPrev = nbt.getFloat("PageFlipPrev");

        System.out.println("reading: " + inventory); // <- это клиент читает
    }

}
