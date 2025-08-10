package com.hxrdsxk;

import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.client.render.VertexConsumer;

public class BackpackRenderer implements ArmorRenderer {
    private final BackpackModel model;
    private static final Identifier TEXTURE = Identifier.of(AllINeedMod.MOD_ID, "textures/backpack/diamond_backpack.png");

    public BackpackRenderer() {
        model = new BackpackModel(BackpackModel.getTexturedModelData().createModel());
    }

    @Override
    public void render(
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            ItemStack stack,
            LivingEntity entity,
            EquipmentSlot slot,
            int light,
            net.minecraft.client.render.entity.model.BipedEntityModel<LivingEntity> contextModel
    ) {
        if (slot != EquipmentSlot.CHEST) return;

        // Копируем позу тела
        contextModel.copyBipedStateTo(model);

        matrices.push();
        // Если нужно сместить рюкзак назад, раскомментируй:
        // matrices.translate(0.0F, 0.0F, 0.1F);

        renderPart(matrices, vertexConsumers, light, stack, model, TEXTURE);
        matrices.pop();
    }

    private static void renderPart(MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                                   int light, ItemStack stack, Model model, Identifier texture) {
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(model.getLayer(texture));
        model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV,
                6233);
    }
}
