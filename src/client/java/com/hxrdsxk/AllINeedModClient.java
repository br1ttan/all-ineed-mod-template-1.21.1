package com.hxrdsxk;

import com.hxrdsxk.block.MagicBlockEntityRenderer;
import com.hxrdsxk.block.ModBlockEntities;
import com.hxrdsxk.block.ModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.BookModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.util.Identifier;

public class AllINeedModClient implements ClientModInitializer {

	public static final EntityModelLayer ENCHANTING_TABLE_BOOK_LAYER = new EntityModelLayer(
			Identifier.of("minecraft", "enchanting_table_book"), "main"
	);

	@Override
	public void onInitializeClient() {
		EntityModelLayerRegistry.registerModelLayer(
				ENCHANTING_TABLE_BOOK_LAYER,
				BookModel::getTexturedModelData
		);

		BlockEntityRendererRegistry.register(ModBlockEntities.MAGIC_BLOCK_ENTITY, MagicBlockEntityRenderer::new);
	}
}
