package com.hxrdsxk.enchantment.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public record CaptainShieldEnchantmentEffect() implements EnchantmentEntityEffect {
    public static final MapCodec<CaptainShieldEnchantmentEffect> CODEC = MapCodec.unit(CaptainShieldEnchantmentEffect::new);

    // Кулдауны для каждого игрока
    private static final Map<UUID, Integer> cooldowns = new HashMap<>();

    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d pos) {
        if (!(user instanceof LivingEntity defender)) return;
        if (!defender.isBlocking()) {
            cooldowns.remove(defender.getUuid()); // очистим если отпустил блок
            return;
        }

        UUID uuid = defender.getUuid();
        int currentCooldown = cooldowns.getOrDefault(uuid, 0);
        if (currentCooldown > 0) {
            cooldowns.put(uuid, currentCooldown - 1); // уменьшаем кулдаун
            return;
        }

        LivingEntity attacker = defender.getAttacker();
        if (attacker == null) return;

        // Отразим урон
        attacker.damage(defender.getDamageSources().thorns(defender), 2.0f + level * 2.0f);

        // Отброс назад
        Vec3d knockback = attacker.getPos().subtract(defender.getPos()).normalize().multiply(1.0 + 0.5 * level);
        attacker.addVelocity(knockback.x, 0.2, knockback.z);
        attacker.velocityModified = true;

        // Частицы
        world.spawnParticles(
                ParticleTypes.CRIT,
                attacker.getX(), attacker.getY() + 1, attacker.getZ(),
                10 + level * 5,
                0.2, 0.2, 0.2,
                0.1
        );

        // Звук удара щитом
        world.playSound(
                null,
                defender.getBlockPos(),
                SoundEvents.ITEM_SHIELD_BLOCK,
                SoundCategory.PLAYERS,
                1.0f, 1.0f
        );

        // Устанавливаем кулдаун в 20 тиков (1 секунда)
        cooldowns.put(uuid, 20);

    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
        return CODEC;
    }
}
