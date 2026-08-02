package com.epic.animals.tag;

import com.epic.animals.EpicAnimals;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class ModItemTags {
    public static final TagKey<Item> CAPYBARA_FOOD = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(EpicAnimals.MODID, "capybara_food"));

    public static final TagKey<Item> CAPYBARA_TAMING_FOOD = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(EpicAnimals.MODID, "capybara_taming_food"));

    public static final TagKey<Item> WOODPECKER_FOOD =
            TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(EpicAnimals.MODID, "woodpecker_food"));

    public static final TagKey<Item> WOODPECKER_TAMING_FOOD =
            TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(EpicAnimals.MODID, "woodpecker_taming_food"));

    public static final TagKey<Item> RHINO_BEETLE_FOOD =
            TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(EpicAnimals.MODID, "rhino_beetle_food"));

    public static final TagKey<Item> RHINO_BEETLE_TAMING_FOOD =
            TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(EpicAnimals.MODID, "rhino_beetle_taming_food"));

    private ModItemTags() {
    }
}
