package com.epic.animals;

import com.epic.animals.entity.Capybara;
import com.epic.animals.entity.JumpingSpider;
import com.epic.animals.entity.RhinoBeetle;
import com.epic.animals.entity.Woodpecker;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import net.minecraft.world.entity.EntityType;

public final class ModEntities {
    public static final DeferredRegister.Entities ENTITY_TYPES = DeferredRegister.createEntities(EpicAnimals.MODID);

    public static final Supplier<EntityType<Capybara>> CAPYBARA =
            ENTITY_TYPES.registerEntityType(
                    "capybara",
                    Capybara::new,
                    MobCategory.CREATURE,
                    builder -> builder.sized(0.9f, 0.8f).clientTrackingRange(10)
            );

    public static final Supplier<EntityType<Woodpecker>> WOODPECKER =
            ENTITY_TYPES.registerEntityType(
                    "woodpecker",
                    Woodpecker::new,
                    MobCategory.CREATURE,
                    builder -> builder.sized(0.5F, 0.9F).clientTrackingRange(10));

    public static final Supplier<EntityType<RhinoBeetle>> RHINO_BEETLE =
            ENTITY_TYPES.registerEntityType(
                    "rhino_beetle",
                    RhinoBeetle::new,
                    MobCategory.CREATURE,
                    builder -> builder.sized(0.9F, 0.6F).clientTrackingRange(10));

    public static final Supplier<EntityType<JumpingSpider>> JUMPING_SPIDER =
            ENTITY_TYPES.registerEntityType(
                    "jumping_spider",
                    JumpingSpider::new,
                    MobCategory.CREATURE,
                    builder -> builder.sized(0.7F, 0.5F).clientTrackingRange(10)
            );

    private ModEntities() {
    }
}
