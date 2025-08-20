package com.hxrdsxk.armor;

import com.hxrdsxk.armor.custom.BackpackRenderer;
import com.hxrdsxk.armor.custom.SuitArmorRenderer;
import com.hxrdsxk.item.ModItems;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;

public class ModArmorRenderers {
    public static void register() {
        ArmorRenderer.register(
                new SuitArmorRenderer(),
                ModItems.DOOM_HELMET,
                ModItems.DOOM_CHESTPLATE,
                ModItems.DOOM_LEGGINGS,
                ModItems.DOOM_BOOTS
        );

        ArmorRenderer.register(new BackpackRenderer(), ModItems.BACKPACK);
    }
}
