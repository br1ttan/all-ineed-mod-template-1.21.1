package com.hxrdsxk.armor.custom;

import com.hxrdsxk.AllINeedMod;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class SuitArmorRenderer implements ArmorRenderer {
    private BipedEntityModel<LivingEntity> model;

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ItemStack stack,
                       LivingEntity entity, EquipmentSlot slot, int light, BipedEntityModel<LivingEntity> contextModel) {

        if (model == null) {
            model = new BipedEntityModel<>(
                    MinecraftClient.getInstance().getEntityModelLoader().getModelPart(EntityModelLayers.PLAYER_INNER_ARMOR)
            );
        }

        contextModel.setVisible(false);

        switch (slot) {
            case HEAD -> {
                contextModel.head.visible = true;
                contextModel.hat.visible = true;
            }
            case CHEST -> {
                contextModel.body.visible = true;
                contextModel.rightArm.visible = true;
                contextModel.leftArm.visible = true;

                ((PlayerEntityModel<LivingEntity>) contextModel).jacket.visible = true;
                ((PlayerEntityModel<LivingEntity>) contextModel).rightSleeve.visible = true;
                ((PlayerEntityModel<LivingEntity>) contextModel).leftSleeve.visible = true;
            }
            case LEGS -> {
                contextModel.rightLeg.visible = true;
                contextModel.leftLeg.visible = true;

                ((PlayerEntityModel<LivingEntity>) contextModel).rightPants.visible = true;
                ((PlayerEntityModel<LivingEntity>) contextModel).leftPants.visible = true;
            }
            case FEET -> {
                contextModel.rightLeg.visible = true;
                contextModel.leftLeg.visible = true;

                ((PlayerEntityModel<LivingEntity>) contextModel).rightPants.visible = true;
                ((PlayerEntityModel<LivingEntity>) contextModel).leftPants.visible = true;
            }
        }

        // Apply the texture
        Identifier texture = Identifier.of(AllINeedMod.MOD_ID, "textures/entity/suits/dr_doom.png");
        ArmorRenderer.renderPart(matrices, vertexConsumers, light, stack, contextModel, texture);
    }
}