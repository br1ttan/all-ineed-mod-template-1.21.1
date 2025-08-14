package com.hxrdsxk.datagen;

import com.hxrdsxk.block.ModBlocks;
import com.hxrdsxk.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.*;
import net.minecraft.item.ArmorItem;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.PINK_GARNET_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.MAGIC_BLOCK);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.PINK_GARNET, Models.GENERATED);
        itemModelGenerator.register(ModItems.TOTEM_OF_DARKNESS, Models.GENERATED);
        itemModelGenerator.register(ModItems.GROWTH_TOGGLE, Models.GENERATED);

        itemModelGenerator.registerArmor(((ArmorItem) ModItems.PINK_NETHERITE_HELMET));
        itemModelGenerator.registerArmor(((ArmorItem) ModItems.PINK_NETHERITE_CHESTPLATE));
        itemModelGenerator.registerArmor(((ArmorItem) ModItems.PINK_NETHERITE_LEGGINGS));
        itemModelGenerator.registerArmor(((ArmorItem) ModItems.PINK_NETHERITE_BOOTS));

        itemModelGenerator.register(ModItems.PINK_GARNET_SWORD, Models.HANDHELD);


        itemModelGenerator.registerArmor(((ArmorItem) ModItems.ENDERITE_HELMET));
        itemModelGenerator.registerArmor(((ArmorItem) ModItems.ENDERITE_CHESTPLATE));
        itemModelGenerator.registerArmor(((ArmorItem) ModItems.ENDERITE_LEGGINGS));
        itemModelGenerator.registerArmor(((ArmorItem) ModItems.ENDERITE_BOOTS));


        itemModelGenerator.registerArmor(((ArmorItem) ModItems.DOOM_HELMET));
        itemModelGenerator.registerArmor(((ArmorItem) ModItems.DOOM_CHESTPLATE));
        itemModelGenerator.registerArmor(((ArmorItem) ModItems.DOOM_LEGGINGS));
        itemModelGenerator.registerArmor(((ArmorItem) ModItems.DOOM_BOOTS));

        itemModelGenerator.register(ModItems.BACKPACK, Models.GENERATED);

    }
}
