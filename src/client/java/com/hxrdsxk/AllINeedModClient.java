package com.hxrdsxk;

import com.hxrdsxk.block.MagicBlockEntityRenderer;
import com.hxrdsxk.block.ModBlockEntities;
import com.hxrdsxk.item.ModItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.render.entity.model.BookModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
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

		BlockEntityRendererFactories.register(ModBlockEntities.MAGIC_BLOCK_ENTITY, MagicBlockEntityRenderer::new);
	}
}