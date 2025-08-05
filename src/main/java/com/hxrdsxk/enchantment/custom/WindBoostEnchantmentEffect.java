package com.hxrdsxk.enchantment.custom;

import com.hxrdsxk.util.Helpers;
import com.mojang.serialization.MapCodec;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
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

        if (!(attacker instanceof LivingEntity livingAttacker)) return;

        LivingEntity me = context.owner();

        if (me == null) return;

        LivingEntity target = livingAttacker.getAttacker().getAttacking();

        int duration = 60;

        // Используем эффект Slowness с высоким уровнем, чтобы цель не могла двигаться

        if (target != null) {
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, duration, 10));

            // Добавим Mining Fatigue, чтобы цель не могла бить
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, duration, 10));
        }


        // Сила подкидывания зависит от уровня
        double upwardVelocity = 1.0 + (0.5 * level); // например, на 1 уровне ~1.5 блока, на 3 уровне ~2.5

        Vec3d currentVelocity = me.getVelocity();
        me.setVelocity(currentVelocity.x, upwardVelocity, currentVelocity.z);
//
        me.velocityModified = true; // нужно, чтобы скорость применилась на клиенте

        for (int i = 0; i < 10 + level * 5; i++) {
            double offsetX = (world.random.nextDouble() - 0.5) * 0.5;
            double offsetY = world.random.nextDouble() * 1.0;
            double offsetZ = (world.random.nextDouble() - 0.5) * 0.5;

            world.spawnParticles(
                    ParticleTypes.EXPLOSION,
                    me.getX() + offsetX,
                    me.getY() + offsetY,
                    me.getZ() + offsetZ,
                    1,
                    0, 0, 0, 0
            );
        }

        world.playSound(
                null,
                me.getBlockPos(),
                net.minecraft.sound.SoundEvents.ENTITY_PHANTOM_FLAP,
                net.minecraft.sound.SoundCategory.PLAYERS,
                1.0f,
                1.0f
        );
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
        return CODEC;
    }
}
