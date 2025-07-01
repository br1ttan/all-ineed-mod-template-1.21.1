package com.hxrdsxk.block;

import com.hxrdsxk.AllINeedModClient;
import com.hxrdsxk.block.custom.MagicBlock;
import com.hxrdsxk.block.entity.MagicBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.model.BookModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;

public class MagicBlockEntityRenderer implements BlockEntityRenderer<MagicBlockEntity> {
    private final BookModel bookModel;
    private static final Identifier BOOK_TEXTURE = Identifier.of("minecraft", "textures/entity/enchanting_table_book.png");

    public MagicBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        this.bookModel = new BookModel(context.getLayerModelPart(AllINeedModClient.ENCHANTING_TABLE_BOOK_LAYER));
    }

    @Override
    public void render(MagicBlockEntity entity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {

        BlockState blockState = entity.getCachedState();
        boolean hasBook = blockState.get(MagicBlock.BOOK);
        if (!hasBook) return;

        Direction facing = blockState.get(MagicBlock.FACING);
        float rotationY = facing.rotateYClockwise().asRotation();

        matrices.push();
        matrices.translate(0.5F, 0.75F, 0.5F);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-rotationY));

        matrices.translate(0.0F, 0.06F, 0.0F);

        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(85.0F));
        matrices.translate(0.0F, 0.0F, 0.0F);

        double time = System.nanoTime() / 1_000_000_000.0;
        float flip = 0.0F;
        float open = 0.1F + 0.9F * (float)(Math.sin(time * Math.PI * 2 * 0.1) * 0.5 + 0.5);
        float pageFlip = (float)(Math.sin(time * 0.5) * 0.1 + 0.1);
        float pageFlipPrev = pageFlip + 0.55F;


        this.bookModel.setPageAngles(flip, open, pageFlip, pageFlipPrev);

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntitySolid(BOOK_TEXTURE));
        this.bookModel.renderBook(matrices, vertexConsumer, light, overlay, -1);

        matrices.pop();
    }
}
