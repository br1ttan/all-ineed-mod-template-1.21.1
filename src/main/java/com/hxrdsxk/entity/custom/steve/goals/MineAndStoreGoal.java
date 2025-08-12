package com.hxrdsxk.entity.custom.steve.goals;

import com.hxrdsxk.entity.custom.steve.SteveNpcEntity;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.BlockBreakingProgressS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityAnimationS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.Random;

public class MineAndStoreGoal extends Goal {
    private final SteveNpcEntity npc;
    private BlockPos targetOrePos = null;
    private BlockPos chestPos = null;
    private int ticksMining = 0;
    private int delayTicks = 0;
    private int storingDelay = 0;
    private ItemStack minedOre = null;
    private final Random random = new Random();

    private enum State { SEARCH_ORE, MOVING_TO_ORE, MINING, MOVING_TO_CHEST, STORING }
    private State currentState = State.SEARCH_ORE;

    public MineAndStoreGoal(SteveNpcEntity npc) {
        this.npc = npc;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        if (!npc.hasPickaxe()) {
            npc.sendMessage(Text.literal("[MineGoal] Нет кирки в инвентаре"));
            return false;
        }

        findOre();
        if (targetOrePos != null) {
            chestPos = findNearbyChest(targetOrePos);
            currentState = State.MOVING_TO_ORE;
            npc.sendMessage(Text.literal("[MineGoal] Найдена руда: " + targetOrePos + ", сундук: " + chestPos));
            return true;
        }
        return false;
    }

    @Override
    public boolean shouldContinue() {
        return currentState != State.SEARCH_ORE && npc.hasPickaxe();
    }

    @Override
    public void start() {
        ticksMining = 0;
        delayTicks = 0;
        storingDelay = 0;
        npc.equipPickaxeInHand();
        npc.sendMessage(Text.literal("[MineGoal] Начинаем добычу руды на " + targetOrePos));
    }

    @Override
    public void stop() {
        targetOrePos = null;
        chestPos = null;
        currentState = State.SEARCH_ORE;
        minedOre = null;
    }

    @Override
    public void tick() {
        World world = npc.getWorld();

        switch (currentState) {
            case MOVING_TO_ORE -> {
                if (targetOrePos == null) {
                    currentState = State.SEARCH_ORE;
                    return;
                }
                double dist = npc.squaredDistanceTo(targetOrePos.getX() + 0.5, targetOrePos.getY() + 0.5, targetOrePos.getZ() + 0.5);
                if (dist > 2 * 2) {
                    npc.getNavigation().startMovingTo(targetOrePos.getX() + 0.5, targetOrePos.getY() + 0.5, targetOrePos.getZ() + 0.5, 1.0);
                } else {
                    currentState = State.MINING;
                    ticksMining = 0;
                    npc.sendMessage(Text.literal("[MineGoal] Начинаем копать"));
                }
            }

            case MINING -> {
                if (targetOrePos == null) {
                    currentState = State.SEARCH_ORE;
                    return;
                }

                // Смотрим на блок
                double tx = targetOrePos.getX() + 0.5;
                double ty = targetOrePos.getY() + 0.5;
                double tz = targetOrePos.getZ() + 0.5;
                npc.getLookControl().lookAt(tx, ty, tz);

                ticksMining++;
                if (ticksMining % 4 == 0) {
                    npc.swingHand(Hand.MAIN_HAND, true);

                    if (!world.isClient) {
                        ServerWorld serverWorld = (ServerWorld) world;
                        EntityAnimationS2CPacket swingPacket = new EntityAnimationS2CPacket(npc, 0);
                        for (ServerPlayerEntity player : serverWorld.getPlayers()) {
                            if (player.squaredDistanceTo(npc) < 32 * 32) {
                                player.networkHandler.sendPacket(swingPacket);
                            }
                        }

                        int progress = MathHelper.clamp((int) ((ticksMining / (float) getRequiredTicks()) * 10), 0, 10);
                        BlockBreakingProgressS2CPacket breakingPacket = new BlockBreakingProgressS2CPacket(npc.getId(), targetOrePos, progress);
                        for (ServerPlayerEntity player : serverWorld.getPlayers()) {
                            if (player.squaredDistanceTo(npc) < 32 * 32) {
                                player.networkHandler.sendPacket(breakingPacket);
                            }
                        }
                    }
                }

                int required = getRequiredTicks();
                if (ticksMining >= required) {
                    BlockState blockState = world.getBlockState(targetOrePos);
                    Block block = blockState.getBlock();

                    world.removeBlock(targetOrePos, false);
                    this.minedOre = getItemStackFromOreBlock(block);

                    if (!this.minedOre.isEmpty()) {
                        npc.sendMessage(Text.literal("[MineGoal] Добыча завершена, добыто: " + this.minedOre));
                        currentState = State.MOVING_TO_CHEST;
                        storingDelay = 0;
                        this.ticksMining = 0;
                        npc.equipPickaxeInHand();
                    } else {
                        currentState = State.SEARCH_ORE;
                    }
                }
            }

            case MOVING_TO_CHEST -> {
                if (chestPos == null) {
                    npc.sendMessage(Text.literal("[MineGoal] Сундук не найден, выкидываем руду"));
                    npc.dropStack(minedOre);
                    currentState = State.SEARCH_ORE;
                    return;
                }

                double dist = npc.squaredDistanceTo(chestPos.getX() + 0.5, chestPos.getY() + 0.5, chestPos.getZ() + 0.5);
                if (dist > 2 * 2) {
                    npc.getNavigation().startMovingTo(chestPos.getX() + 0.5, chestPos.getY() + 0.5, chestPos.getZ() + 0.5, 1.0);
                    npc.getLookControl().lookAt(chestPos.getX() + 0.5, chestPos.getY() + 0.5, chestPos.getZ() + 0.5);
                } else {
                    currentState = State.STORING;
                    storingDelay = 20;
                    npc.getLookControl().lookAt(chestPos.getX() + 0.5, chestPos.getY() + 0.5, chestPos.getZ() + 0.5);
                    npc.playSound(SoundEvents.BLOCK_CHEST_OPEN, 1f, 1f);
                }
            }

            case STORING -> {
                storingDelay--;
                if (storingDelay <= 0) {
                    if (minedOre != null && !minedOre.isEmpty()) {
                        storeInChest(chestPos, minedOre);
                        npc.sendMessage(Text.literal("[MineGoal] Пытаемся положить руду в сундук"));
                    }
                    minedOre = null;
                    currentState = State.SEARCH_ORE;
                } else {
                    npc.getLookControl().lookAt(chestPos.getX() + 0.5, chestPos.getY() + 0.5, chestPos.getZ() + 0.5);
                }
            }
        }
    }

    private void findOre() {
        targetOrePos = null;
        World world = npc.getWorld();
        BlockPos npcPos = npc.getBlockPos();
        int radius = 10;

        for (int y = -2; y <= 2; y++) {
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos pos = npcPos.add(x, y, z);
                    BlockState state = world.getBlockState(pos);
                    Block block = state.getBlock();
                    if (isOre(block)) {
                        targetOrePos = pos;
                        break;
                    }
                }
                if (targetOrePos != null) break;
            }
            if (targetOrePos != null) break;
        }
    }

    private BlockPos findNearbyChest(BlockPos fromPos) {
        World world = npc.getWorld();
        int radius = 5;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos pos = fromPos.add(x, y, z);
                    BlockEntity blockEntity = world.getBlockEntity(pos);
                    if (blockEntity instanceof ChestBlockEntity) {
                        return pos;
                    }
                }
            }
        }
        return null;
    }

    private int getRequiredTicks() {
        ItemStack pickaxe = npc.getPickaxe();
        float durabilityRatio = 1f;
        if (!pickaxe.isEmpty()) {
            durabilityRatio = MathHelper.clamp(
                    ((float) (pickaxe.getMaxDamage() - pickaxe.getDamage()) / pickaxe.getMaxDamage()), 0.2f, 1f);
        }
        return (int) (40 / durabilityRatio);
    }

    private boolean isOre(Block block) {
        return block == Blocks.COAL_ORE
                || block == Blocks.IRON_ORE
                || block == Blocks.GOLD_ORE
                || block == Blocks.DIAMOND_ORE
                || block == Blocks.EMERALD_ORE
                || block == Blocks.REDSTONE_ORE
                || block == Blocks.LAPIS_ORE
                || block == Blocks.NETHER_QUARTZ_ORE
                || block == Blocks.NETHER_GOLD_ORE;
    }

    private ItemStack getItemStackFromOreBlock(Block oreBlock) {
        if (oreBlock == Blocks.COAL_ORE) return new ItemStack(net.minecraft.item.Items.COAL);
        if (oreBlock == Blocks.IRON_ORE) return new ItemStack(net.minecraft.item.Items.RAW_IRON);
        if (oreBlock == Blocks.GOLD_ORE) return new ItemStack(net.minecraft.item.Items.RAW_GOLD);
        if (oreBlock == Blocks.DIAMOND_ORE) return new ItemStack(net.minecraft.item.Items.DIAMOND);
        if (oreBlock == Blocks.EMERALD_ORE) return new ItemStack(net.minecraft.item.Items.EMERALD);
        if (oreBlock == Blocks.REDSTONE_ORE) return new ItemStack(net.minecraft.item.Items.REDSTONE);
        if (oreBlock == Blocks.LAPIS_ORE) return new ItemStack(net.minecraft.item.Items.LAPIS_LAZULI);
        if (oreBlock == Blocks.NETHER_QUARTZ_ORE) return new ItemStack(net.minecraft.item.Items.QUARTZ);
        if (oreBlock == Blocks.NETHER_GOLD_ORE) return new ItemStack(net.minecraft.item.Items.GOLD_NUGGET);
        return ItemStack.EMPTY;
    }

    private void storeInChest(BlockPos chestPos, ItemStack stack) {
        World world = npc.getWorld();
        BlockEntity blockEntity = world.getBlockEntity(chestPos);
        if (!(blockEntity instanceof ChestBlockEntity chest)) {
            npc.sendMessage(Text.literal("[MineGoal] Ошибка: сундук не найден по позиции " + chestPos));
            npc.dropStack(stack);
            return;
        }

        Inventory inv = chest;
        for (int i = 0; i < inv.size(); i++) {
            ItemStack slotStack = inv.getStack(i);
            if (slotStack.isEmpty()) {
                inv.setStack(i, stack.copy());
                npc.playSound(SoundEvents.BLOCK_CHEST_CLOSE, 1f, 1f);
                npc.sendMessage(Text.literal("[MineGoal] Предмет положен в пустой слот " + i));
                return;
            } else if (ItemStack.areEqual(slotStack, stack) && slotStack.getCount() < slotStack.getMaxCount()) {
                int combinedCount = slotStack.getCount() + stack.getCount();
                int maxCount = slotStack.getMaxCount();

                if (combinedCount <= maxCount) {
                    slotStack.increment(stack.getCount());
                    npc.playSound(SoundEvents.BLOCK_CHEST_CLOSE, 1f, 1f);
                    npc.sendMessage(Text.literal("[MineGoal] Предмет добавлен в существующий стек в слоте " + i));
                    return;
                } else {
                    int toAdd = maxCount - slotStack.getCount();
                    slotStack.increment(toAdd);
                    stack.decrement(toAdd);
                    npc.playSound(SoundEvents.BLOCK_CHEST_CLOSE, 1f, 1f);
                    npc.sendMessage(Text.literal("[MineGoal] Часть предметов добавлена в слот " + i));
                }
            }
        }
        npc.sendMessage(Text.literal("[MineGoal] Сундук полный, предмет выброшен в мир"));
        npc.dropStack(stack);
    }
}