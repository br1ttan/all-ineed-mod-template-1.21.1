package com.hxrdsxk.enchantment;

import com.hxrdsxk.AllINeedMod;
import com.hxrdsxk.enchantment.custom.*;
import com.mojang.serialization.MapCodec;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;


public class ModEnchantmentEffect {
    public static final MapCodec<? extends EnchantmentEntityEffect> BOOM =
            registerEntityEffect("boom", BoomEnchantmentEffect.CODEC);

    private static MapCodec<? extends EnchantmentEntityEffect> ENLIGHTENED = registerEntityEffect("enlightened", EnlightenedEnchantmentEffect.CODEC);

    private static MapCodec<? extends EnchantmentEntityEffect> CELERITY = registerEntityEffect("celerity", CelerityEnchantmentEffect.CODEC);

    private static MapCodec<? extends EnchantmentEntityEffect> WIND_BOOST = registerEntityEffect("wind_boost", WindBoostEnchantmentEffect.CODEC);

    private static MapCodec<? extends EnchantmentEntityEffect> SONIC_BOOM = registerEntityEffect("sonic_boom", SonicBoomEnchantmentEffect.CODEC);

    private static MapCodec<? extends EnchantmentEntityEffect> FLYING_SWORD = registerEntityEffect("flying_sword", FlyingSwordEnchantmentEffect.CODEC);

    private static MapCodec<? extends EnchantmentEntityEffect> CAPTAIN_SHIELD = registerEntityEffect("captain_shield", CaptainShieldEnchantmentEffect.CODEC);

    private static MapCodec<? extends EnchantmentEntityEffect> HOOK = registerEntityEffect("hook", HookEnchantmentEffect.CODEC);

    private static final MapCodec<? extends EnchantmentEntityEffect> DESERT_STORM =
            registerEntityEffect("desert_storm", DesertStormEnchantmentEffect.CODEC);


    private static final MapCodec<? extends EnchantmentEntityEffect> MAGNETISM =
            registerEntityEffect("magnetism", MagnetismEnchantmentEffect.CODEC);

    private static final MapCodec<? extends EnchantmentEntityEffect> WHIRLWIND =
            registerEntityEffect("whirlwind", WhirlwindEnchantmentEffect.CODEC);

    private static final MapCodec<? extends EnchantmentEntityEffect> TELEKINESIS =
            registerEntityEffect("telekinesis", TelekinesisEnchantmentEffect.CODEC);


    private static MapCodec<? extends EnchantmentEntityEffect> registerEntityEffect(
            String name, MapCodec<? extends EnchantmentEntityEffect> codec) {
        return Registry.register(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, Identifier.of(AllINeedMod.MOD_ID, name), codec);
    }

    public static void registerEnchantmentEffects() {
        AllINeedMod.LOGGER.info("Registering Mod Enchantments Effects for " + AllINeedMod.MOD_ID);
    }
}
