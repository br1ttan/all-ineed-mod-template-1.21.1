package com.hxrdsxk.block;

import com.hxrdsxk.AllINeedModClient;
import com.hxrdsxk.block.custom.MagicBlock;
import com.hxrdsxk.block.entity.MagicBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.model.BookModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

public class MagicBlockEntityRenderer implements BlockEntityRenderer<MagicBlockEntity> {
    private final BookModel bookModel;
    private static final Identifier BOOK_TEXTURE = Identifier.of("minecraft", "textures/entity/enchanting_table_book.png");
    private static final float CLOSING_SPEED = 0.05F; // Скорость закрытия книги

    public MagicBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        this.bookModel = new BookModel(context.getLayerModelPart(AllINeedModClient.ENCHANTING_TABLE_BOOK_LAYER));
    }

    @Override
    public void render(MagicBlockEntity entity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {

        BlockState blockState = entity.getCachedState();
        if (!blockState.get(MagicBlock.BOOK)) {
            return;
        }

        boolean clicked = blockState.get(MagicBlock.CLICKED);

        Direction facing = blockState.get(MagicBlock.FACING);
        float rotationY = facing.rotateYClockwise().asRotation();

        matrices.push();
        matrices.translate(0.5F, 0.75F, 0.5F);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-rotationY));
        matrices.translate(0.0F, 0.06F, 0.0F);
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(85.0F));

        double time = System.nanoTime() / 1_000_000_000.0;

        float targetOpen;
        float targetPageFlip;
        float targetPageFlipPrev;

        if (clicked) {
            // Плавное закрытие книги
            targetOpen = 0.0F;
            targetPageFlip = 0.0F;
            targetPageFlipPrev = 0.0F;
            VertexConsumer glintConsumer = vertexConsumers.getBuffer(RenderLayer.getGlint());
            this.bookModel.renderBook(matrices, glintConsumer, light, overlay, -1);

            // Уменьшаем прогресс открытия до 0 с заданной скоростью
            entity.openProgress = Math.max(0, entity.openProgress - CLOSING_SPEED);
            entity.pageFlip = Math.max(0, entity.pageFlip - CLOSING_SPEED);
            entity.pageFlipPrev = Math.max(0, entity.pageFlipPrev - CLOSING_SPEED);
        } else {
            // Обычная анимация книги
            targetOpen = 0.1F + 0.9F * (float) (Math.sin(time * Math.PI * 2 * 0.1) * 0.5 + 0.5);
            targetPageFlip = (float) (Math.sin(time * 0.5) * 0.1 + 0.1);
            targetPageFlipPrev = targetPageFlip + 0.55F;

            // Плавное приближение к цели
            float speed = 0.1F;
            entity.openProgress += (targetOpen - entity.openProgress) * speed;
            entity.pageFlip += (targetPageFlip - entity.pageFlip) * speed;
            entity.pageFlipPrev += (targetPageFlipPrev - entity.pageFlipPrev) * speed;
        }

        float flip = 0.0F;
        float open = entity.openProgress;
        float pageFlip = entity.pageFlip;
        float pageFlipPrev = entity.pageFlipPrev;

        this.bookModel.setPageAngles(flip, open, pageFlip, pageFlipPrev);
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntitySolid(BOOK_TEXTURE));
        this.bookModel.renderBook(matrices, vertexConsumer, light, overlay, -1);
        matrices.pop();

        // Рендер предмета
        ItemStack item = entity.getItems().get(1);

        System.out.println(item);
        if (!item.isEmpty()) {
            ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
            matrices.push();
            matrices.translate(0.5F, 1.5F, 0.5F);
            matrices.scale(0.5f, 0.5f, 0.5f);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(entity.getRenderingRotation()));
            itemRenderer.renderItem(item, ModelTransformationMode.GUI, light, OverlayTexture.DEFAULT_UV,
                    matrices, vertexConsumers, entity.getWorld(), 0);
            matrices.pop();
        }
    }

    private int getLightLevel(World world, BlockPos pos) {
        int bLight = world.getLightLevel(LightType.BLOCK, pos);
        int sLight = world.getLightLevel(LightType.SKY, pos);
        return LightmapTextureManager.pack(bLight, sLight);
    }
}