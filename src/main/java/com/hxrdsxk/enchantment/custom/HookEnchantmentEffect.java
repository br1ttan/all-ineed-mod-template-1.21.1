package com.hxrdsxk.enchantment.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HookEnchantmentEffect implements EnchantmentEntityEffect {
    public static final MapCodec<HookEnchantmentEffect> CODEC = MapCodec.unit(HookEnchantmentEffect::new);

    private static final int COOLDOWN = 10;

    private final Map<UUID, Boolean> hadBobber = new HashMap<>();
    private final Map<UUID, FishingBobberEntity> lastBobberRef = new HashMap<>();

    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d pos) {
        if (!(context.owner() instanceof PlayerEntity player)) return;

        boolean hasBobberNow = player.fishHook != null && player.fishHook.isAlive();
        boolean hadBobberBefore = hadBobber.getOrDefault(player.getUuid(), false);

        if (hasBobberNow && player.fishHook instanceof FishingBobberEntity hook) {
            lastBobberRef.put(player.getUuid(), hook);
        }

        if (hadBobberBefore && !hasBobberNow) {
            FishingBobberEntity hook = lastBobberRef.get(player.getUuid());
            if (hook != null) {
                boolean onBlock = hook.isTouchingWater() || hook.horizontalCollision || hook.verticalCollision;
                if (onBlock) {
                    Vec3d direction = hook.getPos().subtract(player.getPos()).normalize().multiply(1.5);
                    player.addVelocity(direction.x, direction.y + 0.5, direction.z);
                    player.velocityModified = true;
                }

                hook.remove(Entity.RemovalReason.DISCARDED);
                player.getItemCooldownManager().set(player.getStackInHand(Hand.MAIN_HAND).getItem(), COOLDOWN);

                lastBobberRef.remove(player.getUuid());
            }
        }
        hadBobber.put(player.getUuid(), hasBobberNow);
    }


    @Override
    public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
        return CODEC;
    }
}
