package com.hxrdsxk.components;

import net.minecraft.component.ComponentType;
import net.minecraft.enchantment.effect.EnchantmentEffectEntry;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

import static com.hxrdsxk.AllINeedMod.MOD_ID;

public class ModComponents {

    // Объявляем поле, но не инициализируем сразу
    public static ComponentType<List<EnchantmentEffectEntry<EnchantmentEntityEffect>>> ON_USE;

    // Метод, который нужно вызвать ОДИН РАЗ ранним этапом инициализации
    public static void register() {
        System.out.println("Приветттт 1");

        if (ON_USE == null) { // предотвращаем повторную регистрацию

            System.out.println("Приветттт 2");
            ON_USE = Registry.register(
                    Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE,
                    Identifier.of(MOD_ID, "use"),
                    ComponentType.<List<EnchantmentEffectEntry<EnchantmentEntityEffect>>>builder()
                            .codec(EnchantmentEffectEntry.<EnchantmentEntityEffect>createCodec(EnchantmentEntityEffect.CODEC, LootContextTypes.ENCHANTED_ITEM).listOf())
                            .build()
            );
        }
    }
}
