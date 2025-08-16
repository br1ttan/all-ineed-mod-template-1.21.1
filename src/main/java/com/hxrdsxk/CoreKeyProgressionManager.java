package com.hxrdsxk;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;

public class CoreKeyProgressionManager {
    private static final Map<ServerPlayerEntity, CoreKeyComponent> players = new HashMap<>();

    public static CoreKeyComponent get(ServerPlayerEntity player) {
        return players.computeIfAbsent(player, p -> new CoreKeyComponent());
    }

    public static void remove(ServerPlayerEntity player) {
        players.remove(player);
    }
}
