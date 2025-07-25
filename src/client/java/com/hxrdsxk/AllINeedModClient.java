package com.hxrdsxk;

import com.hxrdsxk.item.FlyingSwordEntityRenderer;
import com.hxrdsxk.block.MagicBlockEntityRenderer;
import com.hxrdsxk.block.ModBlockEntities;
import com.hxrdsxk.item.ModItemEntities;
import com.hxrdsxk.item.ModItems;
import com.hxrdsxk.item.custom.ActivateTotemPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.entity.model.BookModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

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
		EntityRendererRegistry.register(ModItemEntities.FLYING_SWORD, FlyingSwordEntityRenderer::new);

		PayloadTypeRegistry.playS2C().register(ActivateTotemPayload.ID, ActivateTotemPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(ActivateTotemPayload.ID, ActivateTotemPayload.CODEC);

		ClientPlayNetworking.registerGlobalReceiver(
				ActivateTotemPayload.ID,
				(payload, context) -> {
					context.client().execute(() -> {
						context.client().gameRenderer.showFloatingItem(new ItemStack(ModItems.CHISEL));
					});
				}
		);
	}
}