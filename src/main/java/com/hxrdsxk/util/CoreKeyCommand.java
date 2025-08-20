package com.hxrdsxk.util;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class CoreKeyCommand {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("corekeyprogress")
                    .executes(ctx -> {
                        ServerCommandSource source = ctx.getSource();
                        ServerPlayerEntity player = source.getPlayer();
                        if (player == null) return 0;

                        var comp = CoreKeyProgressionManager.get(player);

                        player.sendMessage(Text.literal("§e[Core KEY] §fВаш прогресс:"));
                        player.sendMessage(Text.literal("§7Блоки: §a" + comp.getBlockCount() + "§7/§a1000"));
                        return 1;
                    }));
        });
    }
}
