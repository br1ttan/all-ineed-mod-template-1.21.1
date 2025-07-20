package com.hxrdsxk.enchantment;

import com.hxrdsxk.enchantment.custom.*;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.effect.EnchantmentEffectTarget;
import net.minecraft.registry.*;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;

import static com.hxrdsxk.AllINeedMod.MOD_ID;

public class ModEnchantments {
    public static final RegistryKey<Enchantment> LIGHTNING_STRIKER =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(MOD_ID, "lightning_striker"));

    public static final RegistryKey<Enchantment> BOOM =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(MOD_ID, "boom"));

    public static final RegistryKey<Enchantment> ENLIGHTENED =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(MOD_ID, "enlightened"));

    public static final RegistryKey<Enchantment> CELERITY =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(MOD_ID, "celerity"));

    public static final RegistryKey<Enchantment> WIND_BOOST =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(MOD_ID, "wind_boost"));


    public static void bootstrap(Registerable<Enchantment> registerable) {
        var enchantments = registerable.getRegistryLookup(RegistryKeys.ENCHANTMENT);

        var items = registerable.getRegistryLookup(RegistryKeys.ITEM);

        register(registerable, LIGHTNING_STRIKER, Enchantment.builder(Enchantment.definition(
                        items.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                        items.getOrThrow(ItemTags.SWORD_ENCHANTABLE),
                        500,
                        2,
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
                        new LightningStrikerEnchantmentEffect()));

        register(registerable, BOOM, Enchantment.builder(Enchantment.definition(
                                items.getOrThrow(ItemTags.PICKAXES),  // применимо к киркам
                                500,                   // вес
                                3,                   // макс уровень
                                Enchantment.leveledCost(15, 20),
                                Enchantment.leveledCost(35, 25),
                                2,                   // anvil cost
                                AttributeModifierSlot.MAINHAND
                        ))
                        .addEffect(
                                EnchantmentEffectComponentTypes.HIT_BLOCK, // после добычи блока
                                new BoomEnchantmentEffect()
                        )
        );

        register(registerable, ENLIGHTENED, Enchantment.builder(Enchantment.definition(
                        items.getOrThrow(ItemTags.ARMOR_ENCHANTABLE),   // применимо к книгам
                        500,                                // вес
                        1,                                 // максимальный уровень
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
                        500, // вес
                        2,  // макс уровень
                        Enchantment.leveledCost(15, 0),
                        Enchantment.leveledCost(35, 0),
                        2,
                        AttributeModifierSlot.HEAD,
                        AttributeModifierSlot.CHEST,
                        AttributeModifierSlot.LEGS,
                        AttributeModifierSlot.FEET
                ))
                .addEffect(
                        EnchantmentEffectComponentTypes.TICK,
                        new CelerityEnchantmentEffect()
                ));


        register(registerable, WIND_BOOST, Enchantment.builder(Enchantment.definition(
                        items.getOrThrow(ItemTags.SWORD_ENCHANTABLE),
                        items.getOrThrow(ItemTags.MACE_ENCHANTABLE),
                        500, // вес
                        3, // макс уровень
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

    }




    private static void register(Registerable<Enchantment> registry, RegistryKey<Enchantment> key, Enchantment.Builder builder) {
        registry.register(key, builder.build(key.getValue()));
    }
}
