package com.hxrdsxk.entity.custom.item;

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
    public void render(FlyingSwordEntity entity, float yawUnused, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light) {
        ItemStack sword = entity.getSwordStack();
        if (sword.isEmpty()) return;

        matrices.push();

        matrices.translate(0, 0.25, 0);
        matrices.scale(1.5f, 1.5f, 1.5f);

        Vec3d velocity = entity.getVelocity();
        if (velocity.lengthSquared() < 0.0001) {
            velocity = entity.getInitialDirection();
        }

        float yaw = 0f;
        float pitch = 0f;

        if (velocity.lengthSquared() > 0.0001) {
            yaw = (float) Math.toDegrees(Math.atan2(velocity.x, velocity.z));
            double horizontalLength = Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z);
            pitch = (float) Math.toDegrees(Math.atan2(velocity.y, horizontalLength));

            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yaw));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(pitch + 180f));
        }

        int spinDuration = FlyingSwordEntity.SPIN_DURATION;
        if (entity.age < spinDuration) {
            float spinAngle = ((entity.age + tickDelta) / spinDuration) * 360f;

            // Вращаем вокруг оси меча (Z, если меч «лежит» в руке)
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(spinAngle));
        }

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
