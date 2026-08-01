package com.epic.animals;

import com.epic.animals.config.Config;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(EpicAnimals.MODID)
public class EpicAnimals {
    public static final String MODID = "epicanimals";
    public static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Items which will all be registered under the "epicanimals" namespace
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "epicanimals" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredItem<SpawnEggItem> CAPYBARA_SPAWN_EGG =
            ITEMS.registerItem("capybara_spawn_egg",
                    properties -> new SpawnEggItem(
                            properties.spawnEgg(ModEntities.CAPYBARA.get())
                    ));

    public static final DeferredItem<SpawnEggItem> WOODPECKER_SPAWN_EGG =
            ITEMS.registerItem("woodpecker_spawn_egg",
                    properties -> new SpawnEggItem(
                            properties.spawnEgg(ModEntities.WOODPECKER.get())
                    ));

    public static final DeferredItem<SpawnEggItem> RHINO_BEETLE_SPAWN_EGG =
            ITEMS.registerItem("rhino_beetle_spawn_egg",
                    properties -> new SpawnEggItem(
                            properties.spawnEgg(ModEntities.RHINO_BEETLE.get())
                    ));

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EPICANIMALS_TAB =
            CREATIVE_MODE_TABS.register("epicanimals_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.epicanimals"))
                    .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
                    .icon(() -> CAPYBARA_SPAWN_EGG.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(CAPYBARA_SPAWN_EGG.get());
                        output.accept(WOODPECKER_SPAWN_EGG.get());
                        output.accept(RHINO_BEETLE_SPAWN_EGG.get());
                    }).build());

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public EpicAnimals(IEventBus modEventBus, ModContainer modContainer) {
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);
        ModEntities.ENTITY_TYPES.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (EpicAnimals) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
}
