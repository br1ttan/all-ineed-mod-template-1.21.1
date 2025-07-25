package com.hxrdsxk.item;

import com.hxrdsxk.AllINeedMod;
import com.hxrdsxk.item.entity.FlyingSwordEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItemEntities {
    public static final EntityType<FlyingSwordEntity> FLYING_SWORD = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(AllINeedMod.MOD_ID, "flying_sword"),
            FabricEntityTypeBuilder.<FlyingSwordEntity>create(SpawnGroup.MISC, FlyingSwordEntity::new)
                    .dimensions(EntityDimensions.fixed(0.75f, 0.25f))
                    .trackRangeBlocks(80)
                    .trackedUpdateRate(2)
                    .build()
    );

    public static void registerItemEntities() {
        AllINeedMod.LOGGER.info("Registering Mod Item Entities for " + AllINeedMod.MOD_ID);
    }
}