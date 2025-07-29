package com.hxrdsxk.enchantment.custom;

import com.hxrdsxk.util.Helpers;
import com.mojang.serialization.MapCodec;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class SonicBoomEnchantmentEffect implements EnchantmentEntityEffect {
    public static final MapCodec<SonicBoomEnchantmentEffect> CODEC =
            MapCodec.unit(SonicBoomEnchantmentEffect::new);

    public static void applySonicBoomEffect(ServerWorld world, LivingEntity me, int level) {
        Vec3d start = me.getEyePos();
        Vec3d look = me.getRotationVec(1.0F);
        double maxDistance = 20.0;
        Vec3d direction = look.normalize();
        int steps = MathHelper.floor(maxDistance) + 7;

        // Проигрываем звук — слышно всем
        world.playSound(
                null,
                me.getBlockPos(),
                SoundEvents.ENTITY_WARDEN_SONIC_BOOM,
                SoundCategory.PLAYERS,
                3.0F,
                1.0F
        );

        for (int j = 1; j < steps; j++) {
            Vec3d point = start.add(direction.multiply(j));

            // Частицы
            world.spawnParticles(ParticleTypes.SONIC_BOOM, point.x, point.y, point.z, 1, 0, 0, 0, 0);

            // Проверка на энтити
            double radius = 1.5;
            List<LivingEntity> entities = world.getEntitiesByClass(
                    LivingEntity.class,
                    Box.of(point, radius, radius, radius),
                    e -> e != me && e.isAlive()
            );

            for (LivingEntity entity : entities) {
                if (entity.damage(world.getDamageSources().sonicBoom(me), 6.0F)) {
                    Vec3d knock = direction.multiply(2.5);
                    entity.addVelocity(knock.x, 0.5, knock.z);
                }
            }
        }
    }

    @Override
    public void apply(ServerWorld world, int level,
                      EnchantmentEffectContext context,
                      Entity user, Vec3d pos) {

        if (user.isAlive() && user instanceof LivingEntity livingEntity) {
            DamageSource damageSource = livingEntity.getRecentDamageSource();

            // Отменяем, если:
            // 1. Урона нет
            // 2. Урон не от трезубца
            // 3. Источник урона – не летящий трезубец (а игрок в ближнем бою)
            if (damageSource == null
                    || (!damageSource.isOf(DamageTypes.TRIDENT) && !damageSource.isOf(DamageTypes.ARROW))
                    || !(damageSource.getSource() instanceof TridentEntity || damageSource.getSource() instanceof ArrowEntity)) {
                return;
            }
        }

        Entity owner = context.owner();
        if (!(owner instanceof LivingEntity me)) return;

        if (!world.isClient) {
            applySonicBoomEffect(world, me, level);
        }
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
        return CODEC;
    }
}
