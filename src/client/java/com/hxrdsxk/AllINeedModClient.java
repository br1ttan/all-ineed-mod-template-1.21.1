package com.hxrdsxk;

import com.hxrdsxk.armor.ModArmorRenderers;
import com.hxrdsxk.block.ModBlockEntityRenderers;
import com.hxrdsxk.entity.ModEntityRenderers;
import com.hxrdsxk.model.ModModelLayers;
import com.hxrdsxk.network.ModNetworking;
import net.fabricmc.api.ClientModInitializer;

public class AllINeedModClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ModModelLayers.register();
		ModArmorRenderers.register();
		ModBlockEntityRenderers.register();
		ModEntityRenderers.register();
		ModNetworking.register();
	}
}
