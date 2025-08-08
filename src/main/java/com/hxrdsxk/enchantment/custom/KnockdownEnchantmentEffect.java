package com.hxrdsxk.enchantment.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class KnockdownEnchantmentEffect implements EnchantmentEntityEffect {
    public static final MapCodec<KnockdownEnchantmentEffect> CODEC = MapCodec.unit(KnockdownEnchantmentEffect::new);

    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d pos) {
        if (!(user instanceof ServerPlayerEntity player)) return;

        if (player.isSleeping() || player.isSpectator() || !player.isAlive()) return;

        // Принудительно уложим игрока в "лёгкое состояние"
        BlockPos fakeBedPos = player.getBlockPos();
        player.sleep(fakeBedPos); // Засыпает на месте
        player.setSleepingPosition((fakeBedPos));
        player.setPose(EntityPose.SLEEPING);

        // Через 5 секунд вернуть обратно
        MinecraftServer server = player.getServer();
//        if (server != null) {
//            server.getTicks().schedule(() -> {
//                if (player.isSleeping()) {
//                    player.wakeUp(false, true);
//                    player.setPose(EntityPose.STANDING);
//                }
//            }, 100); // 100 тиков = 5 секунд
//        }
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
        return CODEC;
    }
}
