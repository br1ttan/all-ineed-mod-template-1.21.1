package com.hxrdsxk.block;

import com.hxrdsxk.block.custom.MagicBlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class ModBlockEntityRenderers {
    public static void register() {
        BlockEntityRendererFactories.register(ModBlockEntities.MAGIC_BLOCK_ENTITY, MagicBlockEntityRenderer::new);
    }
}
