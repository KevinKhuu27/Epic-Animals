package com.epic.animals;

import com.epic.animals.client.model.CapybaraModel;
import com.epic.animals.client.model.JumpingSpiderModel;
import com.epic.animals.client.model.RhinoBeetleModel;
import com.epic.animals.client.model.WoodpeckerModel;
import com.epic.animals.client.renderer.CapybaraRenderer;
import com.epic.animals.client.renderer.JumpingSpiderRenderer;
import com.epic.animals.client.renderer.RhinoBeetleRenderer;
import com.epic.animals.client.renderer.WoodpeckerRenderer;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = EpicAnimals.MODID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = EpicAnimals.MODID, value = Dist.CLIENT)
public class EpicAnimalsClient {
    public EpicAnimalsClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
        EpicAnimals.LOGGER.info("HELLO FROM CLIENT SETUP");
        EpicAnimals.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    }

    @SubscribeEvent
    static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.CAPYBARA.get(), CapybaraRenderer::new);
        event.registerEntityRenderer(ModEntities.WOODPECKER.get(), WoodpeckerRenderer::new);
        event.registerEntityRenderer(ModEntities.RHINO_BEETLE.get(), RhinoBeetleRenderer::new);
        event.registerEntityRenderer(ModEntities.JUMPING_SPIDER.get(), JumpingSpiderRenderer::new);
    }

    @SubscribeEvent
    static void registerLayerDefinition(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(CapybaraModel.LAYER, CapybaraModel::createBodyLayer);
        event.registerLayerDefinition(WoodpeckerModel.LAYER, WoodpeckerModel::createBodyLayer);
        event.registerLayerDefinition(RhinoBeetleModel.LAYER, RhinoBeetleModel::createBodyLayer);
        event.registerLayerDefinition(JumpingSpiderModel.LAYER, JumpingSpiderModel::createBodyLayer);
    }
}
