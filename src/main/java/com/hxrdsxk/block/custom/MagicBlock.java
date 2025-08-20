package com.hxrdsxk.block.custom;

import com.hxrdsxk.block.entity.MagicBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class MagicBlock extends BlockWithEntity {
    public static final MapCodec<MagicBlock> CODEC = createCodec(MagicBlock::new);
    public static final BooleanProperty BOOK = BooleanProperty.of("book");
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    public static final BooleanProperty CLICKED = BooleanProperty.of("clicked");

    public MagicBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(BOOK, false)
                .with(FACING, Direction.NORTH)
                .with(CLICKED, false));
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(BOOK, FACING, CLICKED);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
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
    public ActionResult onUse(BlockState state, World world, BlockPos pos,
                              PlayerEntity player, BlockHitResult hit) {
        if (world.isClient) return ActionResult.SUCCESS;

        BlockEntity be = world.getBlockEntity(pos);
        if (!(be instanceof MagicBlockEntity magicBlockEntity)) return ActionResult.PASS;

        ItemStack held = player.getMainHandStack();

        ItemStack bookStack = magicBlockEntity.getItems().get(0);
        ItemStack itemStack = magicBlockEntity.getItems().get(1);

        boolean hasBook = !bookStack.isEmpty();
        boolean hasItem = !itemStack.isEmpty();

        if (!hasBook && held.getItem() == Items.ENCHANTED_BOOK) {
            magicBlockEntity.getItems().set(0, held.split(1));
            magicBlockEntity.sync();

            Direction playerFacing = player.getHorizontalFacing().getOpposite();
            world.setBlockState(pos, state.with(BOOK, true).with(FACING, playerFacing).with(CLICKED, false), Block.NOTIFY_ALL);
            world.playSound(null, pos, SoundEvents.ITEM_BOOK_PUT, SoundCategory.BLOCKS, 1f, 1f);
            return ActionResult.SUCCESS;
        }

        if (hasBook && !hasItem && !held.isEmpty()) {
            Item item = held.getItem();
            if (item instanceof ToolItem || item instanceof ArmorItem) {
                magicBlockEntity.getItems().set(1, held.split(1));
                magicBlockEntity.sync();

                world.playSound(null, pos, SoundEvents.ITEM_BOOK_PUT, SoundCategory.BLOCKS, 1f, 1f);
                world.setBlockState(pos, state.with(CLICKED, false), Block.NOTIFY_ALL);
                return ActionResult.SUCCESS;
            } else {
                player.sendMessage(Text.literal("Можно поставить только инструмент или броню!"), true);
                return ActionResult.SUCCESS;
            }
        }

        if (held.isEmpty()) {
            if (hasBook) {
                if (hasItem) {
                    world.playSound(null, pos, SoundEvents.ITEM_BOOK_PUT, SoundCategory.BLOCKS, 1f, 1f);
                    player.sendMessage(Text.literal("Сначала заберите инструмент!"), true);
                    magicBlockEntity.spawnEnchantingParticles((ServerWorld) world, pos, state);
                    world.scheduleBlockTick(pos, state.getBlock(), 33);
                    return ActionResult.SUCCESS;
                }

                ItemStack removedBook = magicBlockEntity.getItems().get(0);
                magicBlockEntity.getItems().set(0, ItemStack.EMPTY);
                magicBlockEntity.sync();

                if (!player.getInventory().insertStack(removedBook)) {
                    player.dropItem(removedBook, false);
                }

                world.setBlockState(pos, state.with(BOOK, false).with(CLICKED, false), Block.NOTIFY_ALL);
                return ActionResult.SUCCESS;
            }

            if (hasItem) {
                ItemStack removedItem = magicBlockEntity.getItems().get(1);
                magicBlockEntity.getItems().set(1, ItemStack.EMPTY);
                magicBlockEntity.sync();

                if (!player.getInventory().insertStack(removedItem)) {
                    player.dropItem(removedItem, false);
                }

                world.setBlockState(pos, state.with(CLICKED, false), Block.NOTIFY_ALL);
                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.PASS;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof MagicBlockEntity) {
                ItemScatterer.spawn(world, pos, (MagicBlockEntity) blockEntity);
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        BlockEntity be = world.getBlockEntity(pos);
        if (!(be instanceof MagicBlockEntity magicBlockEntity)) return;

        ItemStack bookStack = magicBlockEntity.getItems().get(0);
        ItemStack itemStack = magicBlockEntity.getItems().get(1);

        if (bookStack.isEmpty() || itemStack.isEmpty()) return;

        var bookEnchantments = bookStack.getOrDefault(DataComponentTypes.STORED_ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT);

        boolean compatible = true;
        for (var enchantmentEntry : bookEnchantments.getEnchantments()) {
            if (!enchantmentEntry.value().isAcceptableItem(itemStack)) {
                compatible = false;
                break;
            }
        }

        if (compatible) {
            ItemStack enchanted = itemStack.copy();
            EnchantmentHelper.set(enchanted, bookEnchantments);

            magicBlockEntity.getItems().set(1, enchanted);
            magicBlockEntity.getItems().set(0, ItemStack.EMPTY);

            world.setBlockState(pos, state.with(BOOK, false).with(CLICKED, true), Block.NOTIFY_ALL);
            world.playSound(null, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1f, 1f);
        } else {
            world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXPLODE.value(), SoundCategory.BLOCKS, 1f, 1f);
            world.spawnParticles(ParticleTypes.EXPLOSION, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 1, 0.1, 0.1, 0.1, 0.0);

            magicBlockEntity.getItems().set(0, ItemStack.EMPTY);
            magicBlockEntity.getItems().set(1, ItemStack.EMPTY);

            world.setBlockState(pos, state.with(BOOK, false).with(CLICKED, false), Block.NOTIFY_ALL);
        }

        magicBlockEntity.sync();
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new MagicBlockEntity(pos, state);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    public static final VoxelShape SHAPE = Block.createCuboidShape(
            0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D
    );
}