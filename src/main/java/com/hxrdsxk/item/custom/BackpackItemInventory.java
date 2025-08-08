package com.hxrdsxk.item.custom;

import com.hxrdsxk.block.entity.ImplementedInventory;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;

public class BackpackItemInventory implements ImplementedInventory {
    private final ItemStack stack;
    private final DefaultedList<ItemStack> inventory;
    private final RegistryWrapper.WrapperLookup registryLookup;

    public BackpackItemInventory(ItemStack stack, RegistryWrapper.WrapperLookup registryLookup) {
        this.stack = stack;
        this.registryLookup = registryLookup;
        this.inventory = DefaultedList.ofSize(27, ItemStack.EMPTY);
        readNbt();
    }

    private void readNbt() {
        // Берем компонент CUSTOM_DATA или создаём новый
        NbtCompound nbt = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt();

        if (nbt.contains("Items", NbtElement.LIST_TYPE)) {
            Inventories.readNbt(nbt, inventory, registryLookup);
        }
    }

    private void writeNbt() {
        NbtCompound nbt = new NbtCompound();
        Inventories.writeNbt(nbt, inventory, false, registryLookup);

        // Записываем в DataComponent
        stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public void markDirty() {
        writeNbt();
    }
}
