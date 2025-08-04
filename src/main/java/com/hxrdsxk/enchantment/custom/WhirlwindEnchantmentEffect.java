package com.hxrdsxk.enchantment.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public record WhirlwindEnchantmentEffect() implements EnchantmentEntityEffect {
    public static final MapCodec<WhirlwindEnchantmentEffect> CODEC = MapCodec.unit(WhirlwindEnchantmentEffect::new);
    private static int tickCounter = 0;

    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d pos) {
        if (!(user instanceof LivingEntity living)) return;
        if (living.isBlocking()) {
            // Направление взгляда
            Vec3d lookVec = living.getRotationVec(1.0F).normalize();
            Vec3d origin = living.getPos().add(0, 1.5, 0);
            float range = 8 * 1.5F;

            // Конус в 60 градусов
            List<MobEntity> entities = world.getEntitiesByClass(
                    MobEntity.class,
                    new Box(origin.add(-range, -range, -range), origin.add(range, range, range)),
                    e -> e.isAlive() && !e.isTeammate(user) && e.squaredDistanceTo(user) <= range * range
            );

            for (MobEntity target : entities) {
                Vec3d toTarget = target.getPos().subtract(origin).normalize();
                double dot = lookVec.dotProduct(toTarget);

                if (dot > Math.cos(Math.toRadians(30))) { // в 60° конусе
                    Vec3d push = lookVec.multiply(0.1 + 0.05 * level);
                    target.addVelocity(push.x, push.y * 0.05, push.z);
                    target.velocityModified = true;
                }
            }

            // Эффект: частицы ветра (Dust)
            for (int i = 0; i < 10; i++) {
                Vec3d offset = lookVec.multiply(i * 0.5);
                Vec3d particlePos = origin.add(offset);
                world.spawnParticles(
                        new DustParticleEffect(Vec3d.unpackRgb(0xC2EFFF).toVector3f(), 1.0f),
                        particlePos.x, particlePos.y, particlePos.z,
                        1, 0.05, 0.05, 0.05, 0.01
                );
            }
            tickCounter++;

            if (tickCounter % 10 == 0) {
                float pitch = MathHelper.clamp(0.5f + (tickCounter / 100f), 0.5f, 1.5f);
                float volume = MathHelper.clamp(0.3f + (tickCounter / 150f), 0.3f, 1.0f);

                world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_WIND_CHARGE_THROW, user.getSoundCategory(), volume, pitch);
            }
        } else {
            tickCounter = 0;
        }
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
        return CODEC;
    }
}
