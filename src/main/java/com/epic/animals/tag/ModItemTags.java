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

    private ModItemTags() {}
}
