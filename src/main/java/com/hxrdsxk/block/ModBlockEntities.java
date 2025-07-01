package com.hxrdsxk.block;

import com.hxrdsxk.AllINeedMod;
import com.hxrdsxk.block.entity.MagicBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
    public static final BlockEntityType<MagicBlockEntity> MAGIC_BLOCK_ENTITY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.of(AllINeedMod.MOD_ID, "magic_block"),
            BlockEntityType.Builder.create(MagicBlockEntity::new, ModBlocks.MAGIC_BLOCK).build(null)
    );
}
