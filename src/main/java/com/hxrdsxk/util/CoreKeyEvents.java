package com.hxrdsxk.util;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.server.network.ServerPlayerEntity;

public class CoreKeyEvents {
    public static void register() {
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            if (!world.isClient && player instanceof ServerPlayerEntity sp) {
                CoreKeyProgressionManager.get(sp).addBlock(sp);
            }
        });
    }
}
