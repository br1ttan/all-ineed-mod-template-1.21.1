package com.hxrdsxk.enchantment.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public record MagnetismEnchantmentEffect() implements EnchantmentEntityEffect {
    public static final MapCodec<MagnetismEnchantmentEffect> CODEC = MapCodec.unit(MagnetismEnchantmentEffect::new);

    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d pos) {
        if (!(context.owner() instanceof LivingEntity)) return;
        if (context.owner().isBlocking()) {
            Vec3d playerPos = user.getPos();
            double radius = 5.0;

            List<ItemEntity> nearbyItems = world.getEntitiesByClass(
                    ItemEntity.class,
                    new Box(playerPos.add(-radius, -radius, -radius), playerPos.add(radius, radius, radius)),
                    item -> item.isAlive() && !item.cannotPickup()
            );

            for (ItemEntity item : nearbyItems) {
                Vec3d direction = playerPos.subtract(item.getPos()).normalize().multiply(0.4 + 0.1 * level);
                item.setVelocity(direction);
                item.velocityModified = true;
            }
        }
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
        return CODEC;
    }
}
