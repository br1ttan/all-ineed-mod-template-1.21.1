package com.hxrdsxk.datagen;

import com.hxrdsxk.AllINeedMod;
import com.hxrdsxk.block.ModBlocks;
import com.hxrdsxk.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        List<ItemConvertible> PINK_GARNET_SMELTABLES = List.of(
                ModItems.RAW_PINK_GARNET,
                ModBlocks.PINK_GARNET_ORE,
                ModBlocks.PINK_GARNET_DEEPSLATE_ORE
        );

        offerSmelting(
                exporter,
                PINK_GARNET_SMELTABLES,
                RecipeCategory.MISC,
                ModItems.PINK_GARNET,
                0.25f,
                200,
                "pink_garnet"
        );

        offerBlasting(
                exporter,
                PINK_GARNET_SMELTABLES,
                RecipeCategory.MISC,
                ModItems.PINK_GARNET,
                0.25f,
                100,
                "pink_garnet"
        );

        offerReversibleCompactingRecipes(
                exporter,
                RecipeCategory.BUILDING_BLOCKS,
                ModItems.PINK_GARNET,
                RecipeCategory.DECORATIONS,
                ModBlocks.PINK_GARNET_BLOCK
        );

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.RAW_PINK_GARNET_BLOCK)
                .pattern("RRR")
                .pattern("RRR")
                .pattern("RRR")
                .input('R', ModItems.RAW_PINK_GARNET)
                .criterion(hasItem(ModItems.RAW_PINK_GARNET), conditionsFromItem(ModItems.RAW_PINK_GARNET))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.RAW_PINK_GARNET, 9)
                .input(ModBlocks.RAW_PINK_GARNET_BLOCK)
                .criterion(hasItem(ModBlocks.RAW_PINK_GARNET_BLOCK), conditionsFromItem(ModBlocks.RAW_PINK_GARNET_BLOCK))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.RAW_PINK_GARNET, 32)
                .input(ModBlocks.MAGIC_BLOCK)
                .criterion(hasItem(ModBlocks.MAGIC_BLOCK), conditionsFromItem(ModBlocks.MAGIC_BLOCK))
                .offerTo(exporter, Identifier.of(AllINeedMod.MOD_ID, "raw_pink_garnet_from_magic_block"));


        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, ModBlocks.MAGIC_BLOCK)
                .pattern("BBB")
                .pattern("AGA")
                .pattern("GGG")
                .input('B', Blocks.POLISHED_DEEPSLATE) // предположительно чёрный блок
                .input('A', Items.ECHO_SHARD)        // аметист (или твой аналог кристалла)
                .input('G', Items.CRYING_OBSIDIAN)
                .criterion(hasItem(ModItems.PINK_GARNET), conditionsFromItem(ModItems.PINK_GARNET))
                .offerTo(exporter);


    }
}
