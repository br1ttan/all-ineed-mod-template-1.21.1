package com.hxrdsxk.model.custom;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;

public class BackpackModel extends BipedEntityModel<LivingEntity> {
    public BackpackModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData root = modelData.getRoot();

        // Создаём стандартные части для BipedEntityModel
        ModelPartData head = root.addChild("head",
                ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        root.addChild("hat", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData body = root.addChild("body",
                ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        root.addChild("right_arm",
                ModelPartBuilder.create(), ModelTransform.pivot(-5.0F, 2.0F, 0.0F));
        root.addChild("left_arm",
                ModelPartBuilder.create(), ModelTransform.pivot(5.0F, 2.0F, 0.0F));
        root.addChild("right_leg",
                ModelPartBuilder.create(), ModelTransform.pivot(-1.9F, 12.0F, 0.0F));
        root.addChild("left_leg",
                ModelPartBuilder.create(), ModelTransform.pivot(1.9F, 12.0F, 0.0F));

        // Добавляем рюкзак к телу
        body.addChild("backpack",
                ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(-4.0F, 0.0F, 2.0F, 8.0F, 10.0F, 3.0F),
                ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        return TexturedModelData.of(modelData, 64, 64);
    }
}
