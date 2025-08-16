package com.hxrdsxk;

import com.hxrdsxk.item.ModItems;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class CoreKeyComponent {
    private int blockCount = 0;

    private static final int BLOCK_THRESHOLD = 1000;

    public void addBlock(ServerPlayerEntity player) {
        blockCount++;
        if (blockCount >= BLOCK_THRESHOLD) {
            blockCount = 0;
            player.getInventory().insertStack(new ItemStack(ModItems.CORE_KEY));
            player.sendMessage(Text.literal("§6[CORE KEY] §aВы получили ключ за блоки!"), false);
        }
    }

    public int getBlockCount() { return blockCount; }
}
