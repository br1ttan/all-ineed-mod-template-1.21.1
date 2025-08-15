package com.hxrdsxk.block.entity;

import com.hxrdsxk.block.ModBlockEntities;
import com.hxrdsxk.block.custom.PinkGarnetBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.DustColorTransitionParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.random.Random;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class PinkGarnetBlockEntity extends BlockEntity {
    private ItemStack animatingItem = ItemStack.EMPTY;
    private int animationTicks = 0;
    private static final int TOTAL_ANIMATION_TICKS = 140;

    private static final List<Item> REWARD_POOL = List.of(
            Items.GOLDEN_APPLE,
            Items.DIAMOND_SWORD,
            Items.ENDER_PEARL,
            Items.EMERALD,
            Items.NETHERITE_SCRAP,
            Items.EXPERIENCE_BOTTLE,
            Items.ENCHANTED_GOLDEN_APPLE
    );

    private static final Map<Item, Text> ITEM_MESSAGES = new HashMap<>();
    static {
        ITEM_MESSAGES.put(Items.GOLDEN_APPLE, Text.literal("§6Золотое яблоко! Отличная находка!"));
        ITEM_MESSAGES.put(Items.DIAMOND_SWORD, Text.literal("§bАлмазный меч! Вооружен и опасен!"));
        ITEM_MESSAGES.put(Items.ENDER_PEARL, Text.literal("§5Жемчуг Эндера! Телепортируйся с стиле!"));
        ITEM_MESSAGES.put(Items.EMERALD, Text.literal("§aИзумруд! Богатство и процветание!"));
        ITEM_MESSAGES.put(Items.NETHERITE_SCRAP, Text.literal("§4Незеритовый скрап! Почти самое крепкое!"));
        ITEM_MESSAGES.put(Items.EXPERIENCE_BOTTLE, Text.literal("§eБутылочка опыта! Знания - сила!"));
        ITEM_MESSAGES.put(Items.ENCHANTED_GOLDEN_APPLE, Text.literal("§6§lЗолотое яблоко счастья! Невероятная удача!"));
    }

    public PinkGarnetBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PINK_GARNET_BLOCK_ENTITY, pos, state);
    }

    public void startAnimation(ItemStack stack) {
        this.animatingItem = stack;
        this.animationTicks = 0;
        markDirty();
    }

    public void tickAnimation(ServerWorld world, BlockPos pos, BlockState state) {
        if (animatingItem.isEmpty()) return;

        animationTicks++;
        Vec3d center = Vec3d.ofCenter(pos);
        Random random = world.random;

        // Phase 1: Rising with particles (0-20 ticks)
        if (animationTicks <= 20) {
            double progress = (double) animationTicks / 20;
            double yOffset = progress * 1.5;

            if (animationTicks % 5 == 0) {
                world.spawnParticles(ParticleTypes.GLOW,
                        center.x, center.y + yOffset, center.z,
                        3, 0.1, 0.1, 0.1, 0.02);
            }

            if (animationTicks % 2 == 0) {
                world.spawnParticles(ParticleTypes.ELECTRIC_SPARK,
                        center.x, center.y + yOffset * 0.8, center.z,
                        2, 0.15, 0.05, 0.15, 0.01);
            }

            if (animationTicks == 1) {
                world.playSound(null, pos, SoundEvents.BLOCK_BEACON_ACTIVATE,
                        SoundCategory.BLOCKS, 0.5f, 0.8f);
            }
        }
        // Phase 2: Spiral formation (20-40 ticks)
        else if (animationTicks <= 40) {
            double phaseProgress = (double)(animationTicks - 20) / 20;
            double radius = phaseProgress * 0.8;

            for (int i = 0; i < 3; i++) {
                double angle = animationTicks * 0.2 + i * Math.PI * 2 / 3;
                world.spawnParticles(ParticleTypes.WAX_OFF,
                        center.x + Math.sin(angle) * radius,
                        center.y + 1.25,
                        center.z + Math.cos(angle) * radius,
                        1, 0, 0, 0, 0.01);
            }

            if (animationTicks % 10 == 0) {
                world.spawnParticles(ParticleTypes.GLOW,
                        center.x, center.y + 1.5, center.z,
                        5, 0.1, 0.1, 0.1, 0.05);
            }
        }
        // Phase 3: Fast spinning with color changes (40-80 ticks)
        else if (animationTicks <= 80) {
            double speed = 0.3 + (animationTicks - 40) * 0.01;
            double radius = 0.6 + Math.sin(animationTicks * 0.1) * 0.1;

            // Используем DustColorTransitionParticleEffect вместо Vec3f
            for (int i = 0; i < 5; i++) {
                double angle = animationTicks * speed + i * Math.PI * 2 / 5;
                float hue = (animationTicks + i * 20) % 360 / 360f;
                int rgb = java.awt.Color.HSBtoRGB(hue, 0.9f, 1.0f);
                float r = ((rgb >> 16) & 0xFF) / 255f;
                float g = ((rgb >> 8) & 0xFF) / 255f;
                float b = (rgb & 0xFF) / 255f;

                ParticleEffect effect = new DustColorTransitionParticleEffect(
                        new org.joml.Vector3f(r, g, b),
                        new org.joml.Vector3f(1, 1, 1),
                        1.0f);

                world.spawnParticles(effect,
                        center.x + Math.sin(angle) * radius,
                        center.y + 1.25 + Math.cos(animationTicks * 0.05) * 0.2,
                        center.z + Math.cos(angle) * radius,
                        1, 0, 0, 0, 0);
            }

            if (animationTicks % 15 == 0) {
                world.playSound(null, pos, SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME,
                        SoundCategory.BLOCKS, 0.4f, 0.7f + random.nextFloat() * 0.6f);
            }
        }
        // Phase 4: Preparation for finale (80-110 ticks)
        else if (animationTicks <= 110) {
            double progress = (double)(animationTicks - 80) / 30;
            double radius = 0.8 * (1 - progress);

            if (animationTicks % 5 == 0) {
                for (int i = 0; i < 8; i++) {
                    double angle = i * Math.PI * 2 / 8;
                    double x = center.x + Math.sin(angle) * radius;
                    double z = center.z + Math.cos(angle) * radius;

                    world.spawnParticles(ParticleTypes.FIREWORK,
                            x, center.y + 1.25, z,
                            1, 0, 0, 0, 0.1);

                    world.spawnParticles(ParticleTypes.ELECTRIC_SPARK,
                            x, center.y + 1.25, z,
                            1,
                            (center.x - x) * 0.1,
                            0.05,
                            (center.z - z) * 0.1,
                            0.05);
                }
            }

            world.spawnParticles(ParticleTypes.GLOW,
                    center.x, center.y + 1.5, center.z,
                    (int)(progress * 10), 0.1, 0.1, 0.1, 0.05);

            if (animationTicks == 90) {
                world.playSound(null, pos, SoundEvents.BLOCK_BEACON_POWER_SELECT,
                        SoundCategory.BLOCKS, 0.7f, 1.0f);
            }
        }
        // Phase 5: Finale explosion (110-140 ticks)
        else {
            if (animationTicks == 111) {
                world.spawnParticles(ParticleTypes.EXPLOSION_EMITTER,
                        center.x, center.y + 1.5, center.z,
                        15, 0.3, 0.3, 0.3, 0.5);

                world.spawnParticles(ParticleTypes.FIREWORK,
                        center.x, center.y + 1.5, center.z,
                        50, 0.5, 0.5, 0.5, 0.3);

                world.playSound(null, pos, SoundEvents.ENTITY_FIREWORK_ROCKET_BLAST,
                        SoundCategory.BLOCKS, 1.0f, 0.8f);
                world.playSound(null, pos, SoundEvents.ENTITY_PLAYER_LEVELUP,
                        SoundCategory.BLOCKS, 0.7f, 1.2f);

                Item reward = REWARD_POOL.get(random.nextInt(REWARD_POOL.size()));
                ItemScatterer.spawn(world, center.x, center.y + 1.5, center.z, new ItemStack(reward));

                sendRewardMessage(world, pos, reward);
            }

            if (animationTicks % 5 == 0 && animationTicks < 130) {
                world.spawnParticles(ParticleTypes.GLOW_SQUID_INK,
                        center.x, center.y + 1.5, center.z,
                        10, 0.3, 0.2, 0.3, 0.1);
            }
        }

        if (animationTicks >= TOTAL_ANIMATION_TICKS) {
            animatingItem = ItemStack.EMPTY;
            world.setBlockState(pos, state.with(PinkGarnetBlock.ANIMATING, false), 3);
            return;
        }

        world.scheduleBlockTick(pos, state.getBlock(), 1);
    }

    private void sendRewardMessage(ServerWorld world, BlockPos pos, Item reward) {
        PlayerEntity nearestPlayer = world.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 15, false);
        if (nearestPlayer != null) {
            Text message = ITEM_MESSAGES.getOrDefault(reward,
                    Text.literal("§fПолучен предмет: ").append(reward.getName()));
            nearestPlayer.sendMessage(message, false);

            if (reward == Items.ENCHANTED_GOLDEN_APPLE) {
                nearestPlayer.sendMessage(Text.literal("§6§l!!! НЕВЕРОЯТНАЯ УДАЧА !!!"), false);
                world.playSound(null, nearestPlayer.getX(), nearestPlayer.getY(), nearestPlayer.getZ(),
                        SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.PLAYERS, 1.0f, 1.0f);
            }
        }
    }
}