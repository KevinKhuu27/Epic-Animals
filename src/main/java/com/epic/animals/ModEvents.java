package com.epic.animals;

import com.epic.animals.entity.Capybara;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@EventBusSubscriber(modid = EpicAnimals.MODID)
public final class ModEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.CAPYBARA.get(), Capybara.createAttributes().build());
    }
    private ModEvents() {}
}
