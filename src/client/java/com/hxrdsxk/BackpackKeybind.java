package com.hxrdsxk;

import com.hxrdsxk.item.custom.BackpackItem;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class BackpackKeybind {
    public static final KeyBinding OPEN_BACKPACK = KeyBindingHelper.registerKeyBinding(
            new KeyBinding(
                    "key.all-ineed-mod.open_backpack",
                    InputUtil.Type.KEYSYM,
                    GLFW.GLFW_KEY_B,
                    "category.all-ineed-mod"
            )
    );

    public static void checkKeybind(PlayerEntity player) {
        if (OPEN_BACKPACK.wasPressed() &&
                player.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof BackpackItem) {

            player.openHandledScreen(new SimpleNamedScreenHandlerFactory(
                    (syncId, inventory, playerEntity) ->
                            new GenericContainerScreenHandler(ScreenHandlerType.GENERIC_9X3, syncId, inventory, new SimpleInventory(27), 3),
                    Text.translatable("container.backpack")
            ));
        }
    }
}