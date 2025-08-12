package com.hxrdsxk.enchantment;

import com.hxrdsxk.enchantment.custom.*;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.effect.*;
import net.minecraft.enchantment.effect.entity.DamageEntityEnchantmentEffect;
import net.minecraft.enchantment.effect.entity.SpawnParticlesEnchantmentEffect;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.provider.number.EnchantmentLevelLootNumberProvider;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.floatprovider.ConstantFloatProvider;

import java.util.List;

import static com.hxrdsxk.AllINeedMod.MOD_ID;
import static net.minecraft.component.EnchantmentEffectComponentTypes.TICK;

public class ModEnchantments {
    public static final RegistryKey<Enchantment> BOOM =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(MOD_ID, "boom"));

    public static final RegistryKey<Enchantment> ENLIGHTENED =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(MOD_ID, "enlightened"));

    public static final RegistryKey<Enchantment> CELERITY =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(MOD_ID, "celerity"));

    public static final RegistryKey<Enchantment> WIND_BOOST =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(MOD_ID, "wind_boost"));

    public static final RegistryKey<Enchantment> SONIC_BOOM =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(MOD_ID, "sonic_boom"));

    public static final RegistryKey<Enchantment> FLYING_SWORD =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(MOD_ID, "flying_sword"));

    public static final RegistryKey<Enchantment> CAPTAIN_SHIELD =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(MOD_ID, "captain_shield"));

    public static final RegistryKey<Enchantment> HOOK =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(MOD_ID, "hook"));

    public static final RegistryKey<Enchantment> MAGNETISM =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(MOD_ID, "magnetism"));

    public static final RegistryKey<Enchantment> WHIRLWIND =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(MOD_ID, "whirlwind"));


    public static final RegistryKey<Enchantment> TELEKINESIS =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(MOD_ID, "telekinesis"));

    public static final RegistryKey<Enchantment> DESERT_STORM =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(MOD_ID, "desert_storm"));


    public static void bootstrap(Registerable<Enchantment> registerable) {
        var enchantments = registerable.getRegistryLookup(RegistryKeys.ENCHANTMENT);

        var items = registerable.getRegistryLookup(RegistryKeys.ITEM);

        register(registerable, BOOM, Enchantment.builder(Enchantment.definition(
                                items.getOrThrow(ItemTags.PICKAXES),
                                500,
                                3,
                                Enchantment.leveledCost(15, 20),
                                Enchantment.leveledCost(35, 25),
                                2,
                                AttributeModifierSlot.MAINHAND
                        ))
                        .addEffect(
                                EnchantmentEffectComponentTypes.HIT_BLOCK,
                                new BoomEnchantmentEffect()
                        )
        );

        register(registerable, ENLIGHTENED, Enchantment.builder(Enchantment.definition(
                        items.getOrThrow(ItemTags.ARMOR_ENCHANTABLE),
                        500,
                        1,
                        Enchantment.leveledCost(15, 0),
                        Enchantment.leveledCost(35, 0),
                        2,
                        AttributeModifierSlot.MAINHAND
                ))
                .addEffect(
                        EnchantmentEffectComponentTypes.POST_ATTACK, // или другое подходящее событие
                        EnchantmentEffectTarget.ATTACKER,
                        EnchantmentEffectTarget.VICTIM,
                        new EnlightenedEnchantmentEffect()
                ));

        register(registerable, CELERITY, Enchantment.builder(Enchantment.definition(
                        items.getOrThrow(ItemTags.ARMOR_ENCHANTABLE),
                        500,
                        2,
                        Enchantment.leveledCost(15, 0),
                        Enchantment.leveledCost(35, 0),
                        2,
                        AttributeModifierSlot.HEAD,
                        AttributeModifierSlot.CHEST,
                        AttributeModifierSlot.LEGS,
                        AttributeModifierSlot.FEET
                ))
                .addEffect(
                        TICK,
                        new CelerityEnchantmentEffect()
                ));


        register(registerable, WIND_BOOST, Enchantment.builder(Enchantment.definition(
                        items.getOrThrow(ItemTags.SWORD_ENCHANTABLE),
                        items.getOrThrow(ItemTags.MACE_ENCHANTABLE),
                        500,
                        3,
                        Enchantment.leveledCost(5, 7),
                        Enchantment.leveledCost(25, 9),
                        2,
                        AttributeModifierSlot.MAINHAND
                ))
                .exclusiveSet(enchantments.getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE_SET))
                .addEffect(
                        EnchantmentEffectComponentTypes.POST_ATTACK,
                        EnchantmentEffectTarget.ATTACKER,
                        EnchantmentEffectTarget.VICTIM,
                        new WindBoostEnchantmentEffect()
                ));

        register(registerable, SONIC_BOOM, Enchantment.builder(Enchantment.definition(
                        items.getOrThrow(ItemTags.TRIDENT_ENCHANTABLE),
                        500, // вес
                        1, // макс уровень
                        Enchantment.leveledCost(5, 7),
                        Enchantment.leveledCost(25, 9),
                        20,
                        AttributeModifierSlot.MAINHAND
                ))
                .exclusiveSet(enchantments.getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE_SET))
                .addEffect(
                        EnchantmentEffectComponentTypes.POST_ATTACK,
                        EnchantmentEffectTarget.ATTACKER,
                        EnchantmentEffectTarget.VICTIM,
                        new SonicBoomEnchantmentEffect()
                ));

        register(registerable, FLYING_SWORD, Enchantment.builder(Enchantment.definition(
                        items.getOrThrow(ItemTags.SWORD_ENCHANTABLE),
                        500,
                        1,
                        Enchantment.leveledCost(15, 0),
                        Enchantment.leveledCost(35, 0),
                        2,
                        AttributeModifierSlot.MAINHAND
                ))
                .addEffect(
                        EnchantmentEffectComponentTypes.POST_ATTACK,
                        EnchantmentEffectTarget.ATTACKER,
                        EnchantmentEffectTarget.VICTIM,
                        new FlyingSwordEnchantmentEffect()
                ));

        register(
                registerable,
                CAPTAIN_SHIELD,
                Enchantment.builder(
                                Enchantment.definition(
                                        items.getOrThrow(ItemTags.SWORD_ENCHANTABLE),
                                        500,
                                        1,
                                        Enchantment.leveledCost(15, 0),
                                        Enchantment.leveledCost(35, 0),
                                        2,
                                        AttributeModifierSlot.ANY
                                )
                        )
                        .addEffect(
                                TICK,
                                new CaptainShieldEnchantmentEffect()
                        )

        );

        register(
                registerable,
                HOOK,
                Enchantment.builder(
                                Enchantment.definition(
                                        items.getOrThrow(ItemTags.FISHING_ENCHANTABLE),
                                        500,
                                        1,
                                        Enchantment.leveledCost(15, 0),
                                        Enchantment.leveledCost(35, 0),
                                        2,
                                        AttributeModifierSlot.MAINHAND
                                )
                        )
                        .addEffect(
                                TICK,
                                new HookEnchantmentEffect()
                        )

        );

        register(
                registerable,
                MAGNETISM,
                Enchantment.builder(
                        Enchantment.definition(
                                items.getOrThrow(ItemTags.SWORD_ENCHANTABLE),
                                500,
                                1, // только 1 уровень
                                Enchantment.leveledCost(10, 0),
                                Enchantment.leveledCost(30, 0),
                                2,
                                AttributeModifierSlot.ANY
                        )
                ).addEffect(
                        TICK,
                        new MagnetismEnchantmentEffect()
                )
        );

        register(
                registerable,
                WHIRLWIND,
                Enchantment.builder(
                        Enchantment.definition(
                                items.getOrThrow(ItemTags.SWORDS),
                                600,
                                1,
                                Enchantment.leveledCost(20, 10),
                                Enchantment.leveledCost(50, 10),
                                2,
                                AttributeModifierSlot.MAINHAND
                        )
                ).addEffect(
                        TICK,
                        new WhirlwindEnchantmentEffect()
                )
        );

        register(
                registerable,
                TELEKINESIS,
                Enchantment.builder(
                        Enchantment.definition(
                                items.getOrThrow(ItemTags.SWORD_ENCHANTABLE),
                                600,
                                1,
                                Enchantment.leveledCost(20, 10),
                                Enchantment.leveledCost(50, 10),
                                1,
                                AttributeModifierSlot.MAINHAND
                        )
                ).addEffect(
                        TICK,
                        new TelekinesisEnchantmentEffect()
                )
        );

        register(
                registerable,
                DESERT_STORM,
                Enchantment.builder(
                        Enchantment.definition(
                                items.getOrThrow(ItemTags.SWORD_ENCHANTABLE),
                                600,
                                3,
                                Enchantment.leveledCost(20, 10),
                                Enchantment.leveledCost(50, 10),
                                2,
                                AttributeModifierSlot.MAINHAND
                        )
                ).addEffect(
                        TICK,
                        new DesertStormEnchantmentEffect()
                )
        );
    }




    private static void register(Registerable<Enchantment> registry, RegistryKey<Enchantment> key, Enchantment.Builder builder) {
        registry.register(key, builder.build(key.getValue()));
    }
}
