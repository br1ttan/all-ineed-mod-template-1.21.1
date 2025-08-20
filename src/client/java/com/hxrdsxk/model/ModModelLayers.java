package com.hxrdsxk.model;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.entity.model.BookModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class ModModelLayers {
    public static final EntityModelLayer ENCHANTING_TABLE_BOOK_LAYER =
            new EntityModelLayer(Identifier.of("minecraft", "enchanting_table_book"), "main");

    public static void register() {
        EntityModelLayerRegistry.registerModelLayer(ENCHANTING_TABLE_BOOK_LAYER, BookModel::getTexturedModelData);
    }
}
