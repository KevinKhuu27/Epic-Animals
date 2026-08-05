package com.epic.animals.tag;

import com.epic.animals.EpicAnimals;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModBlockTags {
    public static final TagKey<Block> CAPYBARA_SPAWNABLE_ON =
            TagKey.create(Registries.BLOCK,
                    Identifier.fromNamespaceAndPath(EpicAnimals.MODID, "capybara_spawnable_on"));

    public static final TagKey<Block> WOODPECKER_SPAWNABLE_ON =
            TagKey.create(Registries.BLOCK,
                    Identifier.fromNamespaceAndPath(EpicAnimals.MODID, "woodpecker_spawnable_on"));

    public static final TagKey<Block> RHINO_BEETLE_SPAWNABLE_ON =
            TagKey.create(Registries.BLOCK,
                    Identifier.fromNamespaceAndPath(EpicAnimals.MODID, "rhino_beetle_spawnable_on"));

    public static final TagKey<Block> JUMPING_SPIDER_SPAWNABLE_ON =
            TagKey.create(Registries.BLOCK,
                    Identifier.fromNamespaceAndPath(EpicAnimals.MODID, "jumping_spider_spawnable_on"));
}
