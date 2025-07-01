package com.hxrdsxk.enchantment.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public class CelerityEnchantmentEffect implements EnchantmentEntityEffect {
    public static final MapCodec<CelerityEnchantmentEffect> CODEC = MapCodec.unit(CelerityEnchantmentEffect::new);

    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d pos) {
        if (user instanceof LivingEntity living) {
            // Ставим эффект скорости 1 уровня
            // Duration 40 тиков = 2 секунды, чтобы не спадал, пока броня надета
            StatusEffectInstance speed = new StatusEffectInstance(
                    StatusEffects.SPEED,
                    40,   // duration
                    level - 1,    // amplifier (0 = Speed I)
                    true, // ambient (не показывает частицы, если хочешь)
                    false // showParticles
            );
            living.addStatusEffect(speed);
        }
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
        return CODEC;
    }
}
