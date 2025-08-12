package com.hxrdsxk.enchantment.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public record DesertStormEnchantmentEffect() implements EnchantmentEntityEffect {
    public static final MapCodec<DesertStormEnchantmentEffect> CODEC = MapCodec.unit(DesertStormEnchantmentEffect::new);

    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d pos) {
        if (!(user instanceof LivingEntity living)) return;

        // Условие активации — блокируем щитом
        if (living.isBlocking()) {
            double radius = 4.0 + level * 1.5; // Радиус бури
            List<MobEntity> mobs = world.getEntitiesByClass(MobEntity.class,
                    new Box(pos.add(-radius, -radius, -radius), pos.add(radius, radius, radius)),
                    e -> e.isAlive() && !e.isTeammate(user));

            // Эффекты для мобов
            for (MobEntity mob : mobs) {
                mob.addStatusEffect(new net.minecraft.entity.effect.StatusEffectInstance(
                        net.minecraft.entity.effect.StatusEffects.BLINDNESS, 40 + level * 20, 0, false, true));
                mob.addStatusEffect(new net.minecraft.entity.effect.StatusEffectInstance(
                        net.minecraft.entity.effect.StatusEffects.SLOWNESS, 60 + level * 20, 1, false, true));
            }

            // Частицы песка вокруг игрока
            for (int i = 0; i < 50; i++) {
                double offsetX = (world.random.nextDouble() - 0.5) * radius * 2;
                double offsetY = world.random.nextDouble() * 2;
                double offsetZ = (world.random.nextDouble() - 0.5) * radius * 2;

                world.spawnParticles(
                        new BlockStateParticleEffect(ParticleTypes.BLOCK, net.minecraft.block.Blocks.SAND.getDefaultState()),
                        pos.x + offsetX, pos.y + offsetY, pos.z + offsetZ,
                        1, 0, 0, 0, 0.0
                );
            }

            // Звук ветра + песка
            // Звук ветра + песка
            world.playSound(
                    null,
                    BlockPos.ofFloored(pos), // конвертируем Vec3d -> BlockPos
                    SoundEvents.WEATHER_RAIN, // или WIND, если в твоей версии есть
                    living.getSoundCategory(),
                    0.8f,
                    0.8f + world.random.nextFloat() * 0.4f
            );

        }
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
        return CODEC;
    }
}
