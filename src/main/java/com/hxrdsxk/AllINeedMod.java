package com.hxrdsxk;

import com.hxrdsxk.block.ModBlocks;
import com.hxrdsxk.enchantment.ModEnchantmentEffect;
import com.hxrdsxk.entity.ModEntities;
import com.hxrdsxk.entity.custom.steve.SpawnSteveOnJoin;
import com.hxrdsxk.item.ModItemGroups;
import com.hxrdsxk.item.ModItems;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AllINeedMod implements ModInitializer {
	public static final String MOD_ID = "all-ineed-mod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItemGroups.registerItemGroups();

		ModItems.registerModItems();
		ModBlocks.registerModBlocks();

		CoreKeyEvents.register();
		CoreKeyCommand.register();

		ModEntities.register();

		ModEnchantmentEffect.registerEnchantmentEffects();

		SpawnSteveOnJoin.register();
	}
}
