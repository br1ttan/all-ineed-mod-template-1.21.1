package com.hxrdsxk.enchantment.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HookEnchantmentEffect implements EnchantmentEntityEffect {
    public static final MapCodec<HookEnchantmentEffect> CODEC = MapCodec.unit(HookEnchantmentEffect::new);
    private final Map<UUID, Boolean> wasUsingItem = new HashMap<>();

    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d pos) {
        if (!(context.owner() instanceof LivingEntity me)) return;

        boolean currentlyUsing = me.isUsingItem();
        boolean previouslyUsing = wasUsingItem.getOrDefault(me.getUuid(), false);
        System.out.println(currentlyUsing);
        if (currentlyUsing && !previouslyUsing) {
            // Это "начало" использования предмета (первый тик удержания)
            System.out.println("ПКМ нажата - старт использования удочки");
        }

        wasUsingItem.put(me.getUuid(), currentlyUsing);
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
        return CODEC;
    }
}