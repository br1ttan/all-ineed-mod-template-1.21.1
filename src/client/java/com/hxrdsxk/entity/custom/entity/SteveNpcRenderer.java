package com.hxrdsxk.entity.custom.entity;


import com.hxrdsxk.entity.custom.steve.SteveNpcEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.util.Identifier;
import net.minecraft.client.util.DefaultSkinHelper;

public class SteveNpcRenderer extends LivingEntityRenderer<SteveNpcEntity, PlayerEntityModel<SteveNpcEntity>> {

    public SteveNpcRenderer(EntityRendererFactory.Context context) {
        super(context, new PlayerEntityModel<>(context.getPart(EntityModelLayers.PLAYER), false), 0.5f);

        this.addFeature(new HeldItemFeatureRenderer<>(this, context.getHeldItemRenderer()));
    }

    @Override
    public Identifier getTexture(SteveNpcEntity entity) {
        return DefaultSkinHelper.getTexture();
    }
}
