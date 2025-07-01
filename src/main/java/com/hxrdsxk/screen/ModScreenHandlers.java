package com.hxrdsxk.screen;

import com.hxrdsxk.AllINeedMod;
import com.hxrdsxk.screen.custom.MagicBlockScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class ModScreenHandlers {
    public static final ScreenHandlerType<MagicBlockScreenHandler> MAGIC_BLOCK_SCREEN_HANDLER =
            Registry.register(
                    Registries.SCREEN_HANDLER,
                    Identifier.of(AllINeedMod.MOD_ID, "magic_block_screen_handler"),
                    new ExtendedScreenHandlerType<>(MagicBlockScreenHandler::new, BlockPos.PACKET_CODEC)
            );

    public static void registerScreenHandlers() {
        AllINeedMod.LOGGER.info("Registering Screen Handlers for " + AllINeedMod.MOD_ID);
    }
}
