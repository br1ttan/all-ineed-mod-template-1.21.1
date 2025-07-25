package com.hxrdsxk.enchantment.custom;

import com.hxrdsxk.item.entity.FlyingSwordEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public record FlyingSwordEnchantmentEffect() implements EnchantmentEntityEffect {
    public static final MapCodec<FlyingSwordEnchantmentEffect> CODEC =
            MapCodec.unit(FlyingSwordEnchantmentEffect::new);

    // FlyingSwordEnchantmentEffect.java
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d pos) {
        // 1. Проверяем, что user - это LivingEntity (игрок или моб)
        if (!(user instanceof LivingEntity attacker)) return;

        // 2. Проверяем, что есть цель атаки
        LivingEntity target = null;
        if (context.owner() != null && context.owner() instanceof LivingEntity owner) {
            target = owner.getAttacking();
        }
        if (!(target instanceof LivingEntity)) return;

        // 3. Берём меч из руки атакующего
        ItemStack sword = attacker.getAttacker().getMainHandStack();
        if (sword.isEmpty()) {
            System.out.println("Меч не найден в руке!"); // Дебаг-логирование
            return;
        }

        // 4. Вычисляем позицию появления меча (3 блока сбоку от цели)
        Vec3d toAttacker = attacker.getAttacker().getPos().subtract(target.getPos()).normalize();
        Vec3d side = new Vec3d(-toAttacker.z, 0, toAttacker.x).normalize();
        if (world.random.nextBoolean()) side = side.multiply(-1);

        // Позиция спавна: вбок и вверх
        Vec3d spawnPos = target.getPos()
                .add(side.multiply(3.0)) // дальше сбоку
                .add(0, 4.0, 0);         // выше цели

        // 5. Создаём entity меча
        if (!world.isClient) {
            FlyingSwordEntity swordEntity = new FlyingSwordEntity(world, attacker.getAttacker(), sword.copy(), target);
            swordEntity.setPosition(spawnPos);
            swordEntity.setSwordStack(sword.copy());

            // Начальная скорость (медленная)
            Vec3d toTarget = target.getPos()
                    .add(0, target.getHeight() / 2.0, 0)
                    .subtract(spawnPos)
                    .normalize()
                    .multiply(0.2); // Очень медленный старт

            swordEntity.setVelocity(toTarget);
            swordEntity.setInitialDirection(toTarget);

            world.spawnEntity(swordEntity);

            System.out.println("Меч создан на позиции: " + spawnPos); // Дебаг
        }
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
        return CODEC;
    }
}
