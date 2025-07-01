package com.hxrdsxk.screen.custom;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public class DummyInventory implements Inventory {
    @Override public int size() { return 9; }
    @Override public boolean isEmpty() { return false; }
    @Override public ItemStack getStack(int slot) { return ItemStack.EMPTY; }
    @Override public ItemStack removeStack(int slot, int amount) { return ItemStack.EMPTY; }
    @Override public ItemStack removeStack(int slot) { return ItemStack.EMPTY; }
    @Override public void setStack(int slot, ItemStack stack) {}
    @Override public void markDirty() {}
    @Override public boolean canPlayerUse(net.minecraft.entity.player.PlayerEntity player) { return true; }
    @Override public void clear() {}
}
