package com.hxrdsxk.enchantment.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public record EnlightenedEnchantmentEffect() implements EnchantmentEntityEffect {
    public static final MapCodec<EnlightenedEnchantmentEffect> CODEC = MapCodec.unit(EnlightenedEnchantmentEffect::new);

    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d pos) {
        if (level > 0) {
            world.spawnParticles(
                    net.minecraft.particle.ParticleTypes.SOUL,
                    user.getX(), user.getY() + 1,user.getZ(),
                    5, 0.3, 0.3, 0.3, 0.1
            );

        }
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
        return CODEC;
    }
}
