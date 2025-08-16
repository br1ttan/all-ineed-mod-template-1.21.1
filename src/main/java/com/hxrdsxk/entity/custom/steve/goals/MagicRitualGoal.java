//package com.hxrdsxk.entity.custom.steve.goals;
//
//import net.minecraft.block.Blocks;
//import net.minecraft.entity.ItemEntity;
//import net.minecraft.entity.ai.goal.Goal;
//import net.minecraft.entity.mob.PathAwareEntity;
//import net.minecraft.item.ItemStack;
//import net.minecraft.item.Items;
//import net.minecraft.particle.ParticleTypes;
//import net.minecraft.server.world.ServerWorld;
//import net.minecraft.sound.SoundEvents;
//import net.minecraft.util.Hand;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.Vec3d;
//import net.minecraft.world.World;
//
//import java.util.EnumSet;
//import java.util.Random;
//
//public class MagicRitualGoal extends Goal {
//    private final PathAwareEntity npc;
//    private BlockPos altarPos;
//    private int ritualTimer = 0;
//    private final Random random = new Random();
//
//    public MagicRitualGoal(PathAwareEntity npc) {
//        this.npc = npc;
//        this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
//    }
//
//    @Override
//    public boolean canStart() {
//        // Ищем золотой блок в радиусе 10 блоков
//        this.altarPos = findAltar(npc.getWorld(), npc.getBlockPos(), 10);
//        return altarPos != null;
//    }
//
//    @Override
//    public void start() {
//        npc.getNavigation().stop();
//        ritualTimer = 100; // 5 секунд (20 тиков = 1 секунда)
//        npc.swingHand(Hand.MAIN_HAND);
//    }
//
//    @Override
//    public void tick() {
//        if (ritualTimer > 0) {
//            ritualTimer--;
//
//            // 1. Поворачивается к алтарю
//            npc.getLookControl().lookAt(altarPos.getX(), altarPos.getY(), altarPos.getZ());
//
//            // 2. Ходит по кругу вокруг алтаря
//            if (ritualTimer % 20 == 0) {
//                Vec3d circlePos = getPointOnCircle(altarPos, ritualTimer / 20.0, 2.0);
//                npc.getNavigation().startMovingTo(circlePos.getX(), circlePos.getY(), circlePos.getZ(), 0.5);
//            }
//
//            // 3. Эффекты частиц и звука
//            if (npc.getWorld() instanceof ServerWorld world) {
//                world.spawnParticles(
//                        ParticleTypes.ENCHANT,
//                        altarPos.getX() + 0.5, altarPos.getY() + 1.5, altarPos.getZ() + 0.5,
//                        10, 0.5, 0.5, 0.5, 0.1
//                );
//            }
//
//            // 4. В конце ритуала – призыв предмета!
//            if (ritualTimer == 1) {
//                finishRitual();
//            }
//        }
//    }
//
//    private void finishRitual() {
//        World world = npc.getWorld();
//
//        // 1. Молния для драматизма!
//        if (world instanceof ServerWorld serverWorld) {
//            serverWorld.spawnParticles(ParticleTypes.LARGE_SMOKE, altarPos.getX(), altarPos.getY() + 1, altarPos.getZ(), 20, 0.5, 0.5, 0.5, 0.1);
//            serverWorld.playSound(null, altarPos, SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, net.minecraft.sound.SoundCategory.HOSTILE, 1.0F, 1.0F);
//        }
//
//        // 2. Случайный предмет появляется на алтаре
//        ItemStack reward = switch (random.nextInt(5)) {
//            case 0 -> new ItemStack(Items.DIAMOND);
//            case 1 -> new ItemStack(Items.GOLDEN_APPLE);
//            case 2 -> new ItemStack(Items.ENCHANTED_BOOK);
//            case 3 -> new ItemStack(Items.EXPERIENCE_BOTTLE, 3);
//            default -> new ItemStack(Items.EMERALD);
//        };
//
//        // 3. Выбрасываем предмет на алтарь
//        npc.getWorld().spawnEntity(new ItemEntity(
//                npc.getWorld(),
//                altarPos.getX() + 0.5,
//                altarPos.getY() + 1.0,
//                altarPos.getZ() + 0.5,
//                reward
//        ));
//    }
//
//    // Ищет золотой блок (можно заменить на кастомный)
//    private BlockPos findAltar(World world, BlockPos center, int radius) {
//        for (int x = -radius; x <= radius; x++) {
//            for (int y = -2; y <= 2; y++) {
//                for (int z = -radius; z <= radius; z++) {
//                    BlockPos pos = center.add(x, y, z);
//                    if (world.getBlockState(pos).isOf(Blocks.GOLD_BLOCK)) {
//                        return pos;
//                    }
//                }
//            }
//        }
//        return null;
//    }
//
//    // Точка на окружности для кругового движения
//    private Vec3d getPointOnCircle(BlockPos center, double angle, double radius) {
//        double x = center.getX() + 0.5 + Math.cos(angle) * radius;
//        double z = center.getZ() + 0.5 + Math.sin(angle) * radius;
//        return new Vec3d(x, center.getY(), z);
//    }
//}