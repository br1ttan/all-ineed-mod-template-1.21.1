package com.hxrdsxk.datagen;

import com.hxrdsxk.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(ItemTags.ARMOR_ENCHANTABLE)
                .add(ModItems.PINK_NETHERITE_HELMET)
                .add(ModItems.PINK_NETHERITE_LEGGINGS)
                .add(ModItems.PINK_NETHERITE_CHESTPLATE)
                .add(ModItems.PINK_NETHERITE_BOOTS);

        getOrCreateTagBuilder(ItemTags.ARMOR_ENCHANTABLE)
                .add(ModItems.DOOM_HELMET)
                .add(ModItems.DOOM_LEGGINGS)
                .add(ModItems.DOOM_CHESTPLATE)
                .add(ModItems.DOOM_BOOTS);

        getOrCreateTagBuilder(ItemTags.SWORD_ENCHANTABLE)
                .add(ModItems.PINK_GARNET_SWORD);
    }
}
