package com.hxrdsxk.enchantment.custom;

import com.hxrdsxk.util.Helpers;
import com.mojang.serialization.MapCodec;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public record BoomEnchantmentEffect() implements EnchantmentEntityEffect {
    public static final MapCodec<BoomEnchantmentEffect> CODEC =
            MapCodec.unit(BoomEnchantmentEffect::new);

    @Override
    public void apply(ServerWorld world, int level,
                      EnchantmentEffectContext context,
                      Entity user, Vec3d pos) {

        if (world.random.nextFloat() > Helpers.getRandomChanceByLevel(world, level)) {
            return; // Взрыва нет — выходим
        }

        // центр взрыва на блоке, который сломали:
        double x = pos.x;
        double y = pos.y;
        double z = pos.z;

        float power = 3f;
        world.createExplosion(
                /*entity*/   user,
                /*x*/        x + 0.5, /*y*/ y + 0.5, /*z*/ z + 0.5,
                /*power*/    power,
                /*fire*/     false,
                /*source*/   World.ExplosionSourceType.BLOCK
        );
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
        return CODEC;
    }
}
