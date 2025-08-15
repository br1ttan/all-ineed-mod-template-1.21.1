package com.hxrdsxk.block.custom;

import com.hxrdsxk.block.entity.PinkGarnetBlockEntity;
import com.hxrdsxk.item.ModItems;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PinkGarnetBlock extends BlockWithEntity {
    public static final MapCodec<PinkGarnetBlock> CODEC = createCodec(PinkGarnetBlock::new);
    public static final BooleanProperty ANIMATING = BooleanProperty.of("animating");

    public PinkGarnetBlock(Settings settings) {
        super(settings);
        setDefaultState(this.stateManager.getDefaultState().with(ANIMATING, false));
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    protected void appendProperties(StateManager.Builder<net.minecraft.block.Block, BlockState> builder) {
        builder.add(ANIMATING);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos,
                              PlayerEntity player, BlockHitResult hit) {
        if (world.isClient) return ActionResult.SUCCESS;

        ItemStack held = player.getStackInHand(Hand.MAIN_HAND);

        if (held.getItem() == ModItems.CORE_KEY && !state.get(ANIMATING)) {
            held.decrement(1);

            if (world.getBlockEntity(pos) instanceof PinkGarnetBlockEntity be) {
                be.startAnimation(held.copy());
                world.setBlockState(pos, state.with(ANIMATING, true), 3);
                world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1f, 1f);
                world.scheduleBlockTick(pos, this, 1);
            }
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, net.minecraft.util.math.random.Random random) {
        if (state.get(ANIMATING)) {
            if (world.getBlockEntity(pos) instanceof PinkGarnetBlockEntity be) {
                be.tickAnimation(world, pos, state);
            } else {
                world.setBlockState(pos, state.with(ANIMATING, false), 3);
            }
        }
    }

    @Override
    public net.minecraft.block.entity.BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PinkGarnetBlockEntity(pos, state);
    }
}