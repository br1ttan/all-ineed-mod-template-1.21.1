package com.hxrdsxk.entity;

import com.hxrdsxk.entity.custom.entity.SteveNpcRenderer;
import com.hxrdsxk.item.ModItemEntities;
import com.hxrdsxk.entity.custom.item.FlyingSwordEntityRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class ModEntityRenderers {
    public static void register() {
        EntityRendererRegistry.register(ModItemEntities.FLYING_SWORD, FlyingSwordEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.STEVE_NPC, SteveNpcRenderer::new);
    }
}
