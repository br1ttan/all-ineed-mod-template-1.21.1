package com.hxrdsxk.enchantment;

import com.hxrdsxk.AllINeedMod;
import com.hxrdsxk.enchantment.custom.*;
import com.mojang.serialization.MapCodec;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;


public class ModEnchantmentEffect {
    public static MapCodec<? extends EnchantmentEntityEffect> LIGHTNING_STRIKER =
            registerEntityEffect("lightning_striker", LightningStrikerEnchantmentEffect.CODEC);

    public static final MapCodec<? extends EnchantmentEntityEffect> BOOM =
            registerEntityEffect("boom", BoomEnchantmentEffect.CODEC);

    private static MapCodec<? extends EnchantmentEntityEffect> ENLIGHTENED = registerEntityEffect("enlightened", EnlightenedEnchantmentEffect.CODEC);

    private static MapCodec<? extends EnchantmentEntityEffect> CELERITY = registerEntityEffect("celerity", CelerityEnchantmentEffect.CODEC);

    private static MapCodec<? extends EnchantmentEntityEffect> WIND_BOOST = registerEntityEffect("wind_boost", WindBoostEnchantmentEffect.CODEC);


    private static MapCodec<? extends EnchantmentEntityEffect> registerEntityEffect(
            String name, MapCodec<? extends EnchantmentEntityEffect> codec) {
        return Registry.register(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, Identifier.of(AllINeedMod.MOD_ID, name), codec);
    }

    public static void registerEnchantmentEffects() {
        AllINeedMod.LOGGER.info("Registering Mod Enchantments Effects for " + AllINeedMod.MOD_ID);
    }
}
