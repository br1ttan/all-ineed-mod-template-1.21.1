package com.hxrdsxk.screen.custom;

import com.hxrdsxk.screen.ModScreenHandlers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;

public class MagicBlockScreenHandler extends ScreenHandler {
    private final BlockPos pos;

    // 💡 Конструктор принимает BlockPos напрямую!
    public MagicBlockScreenHandler(int syncId, PlayerInventory playerInventory, BlockPos pos) {
        super(ModScreenHandlers.MAGIC_BLOCK_SCREEN_HANDLER, syncId);
        this.pos = pos;

        // Пример слотов для рулетки
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(new DummyInventory(), i, 10 + i * 18, 20) {
                @Override
                public boolean canInsert(ItemStack stack) { return false; }
                @Override
                public boolean canTakeItems(PlayerEntity player) { return false; }
            });
        }

        // Слоты игрока
        int m, l;
        for (m = 0; m < 3; ++m) {
            for (l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 51 + m * 18));
            }
        }
        for (m = 0; m < 9; ++m) {
            this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 109));
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) { return true; }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) { return ItemStack.EMPTY; }
}
