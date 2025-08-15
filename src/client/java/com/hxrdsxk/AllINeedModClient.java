package com.hxrdsxk;

import com.hxrdsxk.entity.ModEntities;
import com.hxrdsxk.entity.SteveNpcRenderer;
import com.hxrdsxk.item.FlyingSwordEntityRenderer;
import com.hxrdsxk.block.MagicBlockEntityRenderer;
import com.hxrdsxk.block.ModBlockEntities;
import com.hxrdsxk.item.ModItemEntities;
import com.hxrdsxk.item.ModItems;
import com.hxrdsxk.item.payload.ActivateTotemPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.entity.model.BookModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import org.lwjgl.glfw.GLFW;

public class AllINeedModClient implements ClientModInitializer {
	public static final EntityModelLayer ENCHANTING_TABLE_BOOK_LAYER = new EntityModelLayer(
			Identifier.of("minecraft", "enchanting_table_book"), "main"
	);

	private static KeyBinding openBackpackKey;

	@Override
	public void onInitializeClient() {

		EntityModelLayerRegistry.registerModelLayer(
				ENCHANTING_TABLE_BOOK_LAYER,
				BookModel::getTexturedModelData
		);

		ArmorRenderer.register(
				new SuitArmorRenderer(),
				ModItems.DOOM_HELMET,
				ModItems.DOOM_CHESTPLATE,
				ModItems.DOOM_LEGGINGS,
				ModItems.DOOM_BOOTS
		);


		ArmorRenderer.register(new BackpackRenderer(), ModItems.BACKPACK);
		BlockEntityRendererFactories.register(ModBlockEntities.MAGIC_BLOCK_ENTITY, MagicBlockEntityRenderer::new);
		EntityRendererRegistry.register(ModItemEntities.FLYING_SWORD, FlyingSwordEntityRenderer::new);
		EntityRendererRegistry.register(ModEntities.STEVE_NPC, SteveNpcRenderer::new);

		PayloadTypeRegistry.playS2C().register(ActivateTotemPayload.ID, ActivateTotemPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(ActivateTotemPayload.ID, ActivateTotemPayload.CODEC);

		ClientPlayNetworking.registerGlobalReceiver(
				ActivateTotemPayload.ID,
				(payload, context) -> {
					context.client().execute(() -> {
						context.client().gameRenderer.showFloatingItem(new ItemStack(ModItems.TOTEM_OF_DARKNESS));
					});
				}
		);

		openBackpackKey = KeyBindingHelper.registerKeyBinding(
				new KeyBinding(
						"key.all-ineed-mod.open_backpack",
						InputUtil.Type.KEYSYM,
						GLFW.GLFW_KEY_B,
						"category.all-ineed-mod"
				)
		);


		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (openBackpackKey.wasPressed()) {
				if (client.player != null &&
						client.player.getEquippedStack(EquipmentSlot.CHEST).getItem() == ModItems.BACKPACK) {

					client.player.sendMessage(Text.literal("Рюкзак открыт!"), true);
				}
			}
		});
	}
}