package com.epic.animals;

import com.epic.animals.entity.Capybara;
import com.epic.animals.entity.Woodpecker;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

@EventBusSubscriber(modid = EpicAnimals.MODID)
public final class ModEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.CAPYBARA.get(), Capybara.createAttributes().build());
        event.put(ModEntities.WOODPECKER.get(), Woodpecker.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(
                ModEntities.CAPYBARA.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Capybara::checkCapybaraSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.REPLACE
        );
        event.register(
                ModEntities.WOODPECKER.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Woodpecker::checkWoodpeckerSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }

    private ModEvents() {
    }
}
