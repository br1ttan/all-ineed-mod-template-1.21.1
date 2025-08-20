package com.hxrdsxk.network;

import com.hxrdsxk.item.ModItems;
import com.hxrdsxk.item.payload.ActivateTotemPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.item.ItemStack;

public class ModNetworking {
    public static void register() {
        registerPayloads();

        // Activate Totem
        ClientPlayNetworking.registerGlobalReceiver(
                ActivateTotemPayload.ID,
                (payload, context) -> context.client().execute(() ->
                        context.client().gameRenderer.showFloatingItem(new ItemStack(ModItems.TOTEM_OF_DARKNESS))
                )
        );
    }

    private static void registerPayloads() {
        // Activate Totem
        PayloadTypeRegistry.playS2C().register(ActivateTotemPayload.ID, ActivateTotemPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(ActivateTotemPayload.ID, ActivateTotemPayload.CODEC);
    }
}
