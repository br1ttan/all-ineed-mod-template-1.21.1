package com.hxrdsxk;

import com.hxrdsxk.block.ModBlockEntities;
import com.hxrdsxk.block.ModBlocks;
import com.hxrdsxk.components.ModComponents;
import com.hxrdsxk.enchantment.ModEnchantmentEffect;
import com.hxrdsxk.item.ModItemGroups;
import com.hxrdsxk.item.ModItems;
import com.hxrdsxk.screen.ModScreenHandlers;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AllINeedMod implements ModInitializer {
	public static final String MOD_ID = "all-ineed-mod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModComponents.register();

		ModItemGroups.registerItemGroups();

		ModItems.registerModItems();
		ModBlocks.registerModBlocks();

		ModEnchantmentEffect.registerEnchantmentEffects();
	}
}
