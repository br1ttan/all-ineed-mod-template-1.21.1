package com.hxrdsxk.item;

import com.hxrdsxk.AllINeedMod;
import com.hxrdsxk.item.entity.FlyingSwordEntity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItemEntities {
    public static final EntityType<FlyingSwordEntity> FLYING_SWORD = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(AllINeedMod.MOD_ID, "flying_sword"),
            EntityType.Builder.<FlyingSwordEntity>create(FlyingSwordEntity::new, SpawnGroup.MISC)
                    .dimensions(0.75f, 0.25f)
                    .maxTrackingRange(80)
                    .trackingTickInterval(2)
                    .build()
    );

    public static void registerItemEntities() {
        AllINeedMod.LOGGER.info("Registering Mod Item Entities for " + AllINeedMod.MOD_ID);
    }
}