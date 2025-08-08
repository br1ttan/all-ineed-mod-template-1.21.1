package com.hxrdsxk.enchantment.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TelekinesisEnchantmentEffect implements EnchantmentEntityEffect {
    public static final MapCodec<TelekinesisEnchantmentEffect> CODEC = MapCodec.unit(TelekinesisEnchantmentEffect::new);

    private final Map<UUID, Vec3d> playerTargetPoints = new HashMap<>();
    private final Map<UUID, ArmorStandEntity> shulkerHeads = new HashMap<>();
    private static final Random random = Random.create();

    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d pos) {
        if (!(user instanceof PlayerEntity player)) return;
        UUID playerId = player.getUuid();

        // Если не блокирует — удалить сущность
        if (!player.isBlocking()) {
            ArmorStandEntity stand = shulkerHeads.remove(playerId);
            if (stand != null) {
                stand.discard();
            }
            playerTargetPoints.remove(playerId);
            return;
        }

        // Вычисляем позицию партикла
        Vec3d lookVec = getLookVector(player);
        Vec3d currentTarget = playerTargetPoints.getOrDefault(playerId, player.getEyePos().add(lookVec.multiply(2)));
        Vec3d newTarget = currentTarget.add(lookVec.multiply(0.3));
        playerTargetPoints.put(playerId, newTarget);

        // Пускаем партиклы
        world.spawnParticles(ParticleTypes.END_ROD, newTarget.x, newTarget.y, newTarget.z, 1, 0, 0, 0, 0.0);

        // Получаем или создаём шалькер-голову
        ArmorStandEntity stand = shulkerHeads.computeIfAbsent(playerId, id -> {
            ArmorStandEntity armorStand = new ArmorStandEntity(world, newTarget.x, newTarget.y, newTarget.z);
            armorStand.setInvisible(true);
            armorStand.setInvulnerable(true);
            armorStand.setNoGravity(true);
            armorStand.setHeadYaw(player.getYaw());
            armorStand.equipStack(net.minecraft.entity.EquipmentSlot.HEAD, Blocks.SHULKER_BOX.asItem().getDefaultStack());
            world.spawnEntity(armorStand);
            return armorStand;
        });

        // Перемещаем шалькер-голову к партиклу
        stand.refreshPositionAndAngles(newTarget.x, newTarget.y, newTarget.z, player.getYaw(), 0);
        stand.setHeadYaw(player.getYaw());

        // Наносим урон мобам, с которыми столкнулась
        for (Entity target : world.getOtherEntities(stand, stand.getBoundingBox().expand(0.4), e -> e != player && e.isAlive())) {
            target.damage(player.getDamageSources().cactus(), 2.0f + level);
        }
    }

    private Vec3d getLookVector(PlayerEntity player) {
        float pitch = player.getPitch();
        float yaw = player.getYaw();
        float pitchRad = (float) Math.toRadians(-pitch);
        float yawRad = (float) Math.toRadians(-yaw);

        double x = Math.sin(yawRad) * Math.cos(pitchRad);
        double y = Math.sin(pitchRad);
        double z = Math.cos(yawRad) * Math.cos(pitchRad);

        return new Vec3d(x, y, z).normalize();
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
        return CODEC;
    }
}
