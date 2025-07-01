package com.hxrdsxk.block.entity;

import com.hxrdsxk.block.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;

public class MagicBlockEntity extends BlockEntity {
    private ItemStack book = ItemStack.EMPTY;

    public MagicBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MAGIC_BLOCK_ENTITY, pos, state);
    }

    public ItemStack getBook() {
        return book;
    }

    public void setBook(ItemStack book) {
        this.book = book.copy();
        markDirty();
    }

    public ItemStack removeBook() {
        ItemStack oldBook = this.book;
        this.book = ItemStack.EMPTY;
        markDirty();
        return oldBook;
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        if (!book.isEmpty()) {
            nbt.put("Book", this.book.encode(registryLookup));
        }
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        if (nbt.contains("Book")) {
            book = ItemStack.fromNbt(registryLookup, nbt.getCompound("Book")).orElse(ItemStack.EMPTY);
        } else {
            book = ItemStack.EMPTY;
        }
    }
}
