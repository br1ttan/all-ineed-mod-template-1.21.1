package com.hxrdsxk.item;

import com.hxrdsxk.AllINeedMod;
import com.hxrdsxk.item.custom.TotemOfDarknessItem;
import com.hxrdsxk.item.custom.GrowthToggleItem;
import com.hxrdsxk.item.custom.PinkGarnetSwordItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item PINK_GARNET = registerItem("pink_garnet", new Item(new Item.Settings()));

    public static final Item TOTEM_OF_DARKNESS = registerItem("totem_of_darkness", new TotemOfDarknessItem(new Item.Settings().maxDamage(3)));

    public static final Item PINK_NETHERITE_HELMET = registerItem("pink_netherite_helmet",
            new ArmorItem(ModArmorMaterials.PINK_NETHERITE_ARMOR_MATERIAL, ArmorItem.Type.HELMET, new Item.Settings()
                    .maxDamage(ArmorItem.Type.HELMET.getMaxDamage(15))));

    public static final Item PINK_NETHERITE_CHESTPLATE = registerItem("pink_netherite_chestplate",
            new ArmorItem(ModArmorMaterials.PINK_NETHERITE_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Settings()
                    .maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(15))));

    public static final Item PINK_NETHERITE_LEGGINGS = registerItem("pink_netherite_leggings",
            new ArmorItem(ModArmorMaterials.PINK_NETHERITE_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Settings()
                    .maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(15))));

    public static final Item PINK_NETHERITE_BOOTS = registerItem("pink_netherite_boots",
            new ArmorItem(ModArmorMaterials.PINK_NETHERITE_ARMOR_MATERIAL, ArmorItem.Type.BOOTS, new Item.Settings()
                    .maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(15))));

    public static final Item PINK_GARNET_SWORD = registerItem("pink_garnet_sword",
            new PinkGarnetSwordItem(ToolMaterials.DIAMOND, new SwordItem.Settings()
                    .attributeModifiers(SwordItem.createAttributeModifiers(ToolMaterials.DIAMOND, 3, -2.4f))));

    public static final Item GROWTH_TOGGLE = registerItem("growth_toggle", new GrowthToggleItem(new Item.Settings().maxCount(1)));


    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(AllINeedMod.MOD_ID, name), item);
    }

    public static void registerModItems() {
        AllINeedMod.LOGGER.info("Registering Mod Items for " + AllINeedMod.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(PINK_GARNET);
        });
    }
}
