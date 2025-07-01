package com.hxrdsxk.block.custom;

import com.hxrdsxk.block.entity.MagicBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class MagicBlock extends BlockWithEntity {
    public static final MapCodec<MagicBlock> CODEC = createCodec(MagicBlock::new);

    public static final BooleanProperty BOOK = BooleanProperty.of("book");
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

    public MagicBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(BOOK, false)
                .with(FACING, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(BOOK, FACING);
    }

    @Override
    public MapCodec<MagicBlock> getCodec() {
        return CODEC;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState()
                .with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world.isClient) return ActionResult.SUCCESS;

        var be = world.getBlockEntity(pos);
        if (!(be instanceof MagicBlockEntity magicBlockEntity)) return ActionResult.PASS;

        ItemStack held = player.getMainHandStack();

        if (magicBlockEntity.getBook().isEmpty() && held.getItem() == Items.ENCHANTED_BOOK) {
            magicBlockEntity.setBook(held.split(1));

            Direction playerFacing = player.getHorizontalFacing().getOpposite();
            world.setBlockState(pos, state.with(BOOK, true).with(FACING, playerFacing), Block.NOTIFY_ALL);
            world.playSound(null, pos, SoundEvents.ITEM_BOOK_PUT, SoundCategory.BLOCKS, 1f, 1f);

            return ActionResult.SUCCESS;
        }

        if (!magicBlockEntity.getBook().isEmpty() && held.isEmpty()) {
            ItemStack book = magicBlockEntity.removeBook();
            if (!player.getInventory().insertStack(book)) {
                player.dropItem(book, false);
            }
            world.setBlockState(pos, state.with(BOOK, false), Block.NOTIFY_ALL);
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new MagicBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return null; // если не нужен тикер
    }

    public static final VoxelShape SHAPE = Block.createCuboidShape(
            0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D // ниже 16 по Y, чтобы была «столешница»
    );

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }
}
