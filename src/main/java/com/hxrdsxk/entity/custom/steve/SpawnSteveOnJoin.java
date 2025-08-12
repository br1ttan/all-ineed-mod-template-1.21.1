package com.hxrdsxk.entity.custom.steve;

import com.hxrdsxk.entity.ModEntities;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

public class SpawnSteveOnJoin {
    public static void register() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerEntity player = handler.player;
            ServerWorld world = player.getServerWorld();
            BlockPos pos = player.getBlockPos().add(2, 0, 0);

            SteveNpcEntity npc = new SteveNpcEntity(ModEntities.STEVE_NPC, world);
//            npc.refreshPositionAndAngles(pos, 0.0F, 0.0F);
//            world.spawnEntity(npc);
//            npc.setFollowTarget(player);
        });
    }
}
