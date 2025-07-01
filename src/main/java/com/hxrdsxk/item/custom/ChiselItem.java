package com.hxrdsxk.item.custom;

import com.hxrdsxk.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;

public class ChiselItem extends Item {
    private static final Map<Block, Block> CHISEL_MAP = Map.of(
            Blocks.STONE, Blocks.STONE_BRICKS,
            Blocks.END_STONE, Blocks.END_STONE_BRICKS,
            Blocks.OAK_LOG, ModBlocks.PINK_GARNET_BLOCK,
            Blocks.GOLD_BLOCK, Blocks.NETHER_BRICK_FENCE
    );

    public ChiselItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            Vec3d start = user.getEyePos();
            Vec3d look = user.getRotationVec(1.0F);
            double maxDistance = 20.0;
            Vec3d direction = look.normalize();
            int steps = MathHelper.floor(maxDistance) + 7;

            // Проигрываем звук — слышно всем
            world.playSound(
                    null,
                    user.getBlockPos(),
                    SoundEvents.ENTITY_WARDEN_SONIC_BOOM,
                    SoundCategory.PLAYERS,
                    3.0F,
                    1.0F
            );

            for (int j = 1; j < steps; j++) {
                Vec3d point = start.add(direction.multiply(j));

                // Частицы
                ((ServerWorld) world).spawnParticles(ParticleTypes.SONIC_BOOM, point.x, point.y, point.z, 1, 0, 0, 0, 0);

                // Проверка на энтити
                double radius = 1.5; // радиус зацепа
                List<LivingEntity> entities = world.getEntitiesByClass(
                        LivingEntity.class,
                        Box.of(point, radius, radius, radius),
                        e -> e != user && e.isAlive()
                );

                for (LivingEntity entity : entities) {
                    if (entity.damage(world.getDamageSources().sonicBoom(user), 6.0F)) {
                        Vec3d knock = direction.multiply(2.5);
                        entity.addVelocity(knock.x, 0.5, knock.z);
                    }
                }
            }

            user.getItemCooldownManager().set(this, 60);
        }

        return TypedActionResult.success(user.getStackInHand(hand), world.isClient());
    }




    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        Block clickedBlock = world.getBlockState(context.getBlockPos()).getBlock();

        if (CHISEL_MAP.containsKey(clickedBlock)) {
            if (!world.isClient()) {
                world.setBlockState(context.getBlockPos(), CHISEL_MAP.get(clickedBlock).getDefaultState());

                context.getStack().damage(
                        1,
                        ((ServerWorld) world),
                        ((ServerPlayerEntity) context.getPlayer()),
                        item -> context.getPlayer().sendEquipmentBreakStatus(item, EquipmentSlot.MAINHAND));

                world.playSound(null, context.getBlockPos(), SoundEvents.BLOCK_GRINDSTONE_USE, SoundCategory.BLOCKS);

            }
        }

        return ActionResult.SUCCESS;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("tooltip.all-ineed-mod.chisel"));

        super.appendTooltip(stack, context, tooltip, type);
    }
}
