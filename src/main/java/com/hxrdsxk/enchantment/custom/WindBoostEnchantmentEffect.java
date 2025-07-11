package com.hxrdsxk.enchantment.custom;

import com.hxrdsxk.util.Helpers;
import com.mojang.serialization.MapCodec;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public record WindBoostEnchantmentEffect() implements EnchantmentEntityEffect {

    public static final MapCodec<WindBoostEnchantmentEffect> CODEC = MapCodec.unit(WindBoostEnchantmentEffect::new);

    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity attacker, Vec3d pos) {
           HitResult hit = attacker.raycast(5.0, 0.0f, false);

        if (!(attacker instanceof LivingEntity livingAttacker)) return;

        LivingEntity target = livingAttacker.getAttacker().getAttacking();

        if (target == null) return;

        float chance = Helpers.getRandomChanceByLevel(world, level);
//        if (world.random.nextFloat() > chance) return;

        // Сила подкидывания зависит от уровня
        double upwardVelocity = 1.0 + (0.5 * level); // например, на 1 уровне ~1.5 блока, на 3 уровне ~2.5

        Vec3d currentVelocity = target.getVelocity();
        target.setVelocity(currentVelocity.x, upwardVelocity, currentVelocity.z);
//
        target.velocityModified = true; // нужно, чтобы скорость применилась на клиенте

        for (int i = 0; i < 10 + level * 5; i++) {
            double offsetX = (world.random.nextDouble() - 0.5) * 0.5;
            double offsetY = world.random.nextDouble() * 1.0;
            double offsetZ = (world.random.nextDouble() - 0.5) * 0.5;

            world.spawnParticles(
                    ParticleTypes.EXPLOSION,
                    target.getX() + offsetX,
                    target.getY() + offsetY,
                    target.getZ() + offsetZ,
                    1,
                    0, 0, 0, 0
            );
        }

        // 🌬️ Звук ветра
        world.playSound(
                null,
                target.getBlockPos(),
                net.minecraft.sound.SoundEvents.ENTITY_PHANTOM_FLAP, // Можно заменить на свой
                net.minecraft.sound.SoundCategory.PLAYERS,
                1.0f,
                1.0f
        );
        // Можно сразу дать урон падения при посадке — Minecraft сам посчитает урон, когда сущность упадёт
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
        return CODEC;
    }
}
