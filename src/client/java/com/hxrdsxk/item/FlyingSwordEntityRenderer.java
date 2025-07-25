package com.hxrdsxk.item;

import com.hxrdsxk.item.entity.FlyingSwordEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;

public class FlyingSwordEntityRenderer extends EntityRenderer<FlyingSwordEntity> {
    public FlyingSwordEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public void render(FlyingSwordEntity entity, float yaw, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light) {
        ItemStack sword = entity.getSwordStack();
        if (sword.isEmpty()) return;

        matrices.push();

        // Позиция и масштаб
        matrices.translate(0, 0.25, 0);
        matrices.scale(1.5f, 1.5f, 1.5f);

        Vec3d velocity = entity.getVelocity();

        // 1. Поворот в сторону полёта (Yaw по вектору)
        if (velocity.lengthSquared() > 0.0001) {
            float angleY = (float) Math.toDegrees(Math.atan2(velocity.x, velocity.z));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(angleY));
        }

        // 2. Вращение лезвием вверх (X)
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));

        // 3. Анимация вращения при спавне (локально по Y)
        int spinDuration = FlyingSwordEntity.SPIN_DURATION;
        if (entity.age < spinDuration) {
            float spinAngle = ((entity.age + tickDelta) / spinDuration) * 360f;
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(spinAngle));
        }

        // 4. Рендер меча
        MinecraftClient.getInstance().getItemRenderer().renderItem(
                sword,
                ModelTransformationMode.THIRD_PERSON_RIGHT_HAND,
                light,
                OverlayTexture.DEFAULT_UV,
                matrices,
                vertexConsumers,
                entity.getWorld(),
                entity.getId()
        );

        matrices.pop();
    }

    @Override
    public Identifier getTexture(FlyingSwordEntity entity) {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
    }
}
