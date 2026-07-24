package com.epic.animals;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

public class CapybaraRenderer extends MobRenderer<Capybara, CapybaraRenderState, CapybaraModel> {
    private static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(EpicAnimals.MODID, "textures/entity/capybara.png");

    public CapybaraRenderer(EntityRendererProvider.Context context) {
        super(context, new CapybaraModel(context.bakeLayer(CapybaraModel.LAYER)), 0.5F);
    }

    @Override
    public CapybaraRenderState createRenderState() {
        return new CapybaraRenderState();
    }

    @Override
    public Identifier getTextureLocation(CapybaraRenderState state) {
        return TEXTURE;
    }
}
