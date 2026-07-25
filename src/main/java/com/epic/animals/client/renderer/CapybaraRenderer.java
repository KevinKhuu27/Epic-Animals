package com.epic.animals.client.renderer;

import com.epic.animals.client.state.CapybaraRenderState;
import com.epic.animals.EpicAnimals;
import com.epic.animals.client.model.BabyCapybaraModel;
import com.epic.animals.client.model.CapybaraModel;
import com.epic.animals.entity.Capybara;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.AdultAndBabyModelPair;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;

public class CapybaraRenderer extends MobRenderer<Capybara, CapybaraRenderState, CapybaraModel> {
    private static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(EpicAnimals.MODID, "textures/entity/capybara.png");
    private static final Identifier BABY_TEXTURE =
            Identifier.fromNamespaceAndPath(EpicAnimals.MODID, "textures/entity/capybara_baby.png");

    private final AdultAndBabyModelPair<CapybaraModel> models;

    public CapybaraRenderer(EntityRendererProvider.Context context) {
        super(context, new CapybaraModel(context.bakeLayer(CapybaraModel.LAYER)), 0.5F);
        this.models = new AdultAndBabyModelPair<>(
                this.model,
                new BabyCapybaraModel(context.bakeLayer(CapybaraModel.BABY_LAYER)));
    }

    @Override
    protected void scale(CapybaraRenderState state, PoseStack poseStack) {
        float s = state.ageScale;
        poseStack.scale(s, s, s);
    }

    @Override
    public void submit(CapybaraRenderState state, PoseStack poseStack,
                       SubmitNodeCollector collector, CameraRenderState camera) {
        this.model = this.models.getModel(state.isBaby);
        super.submit(state, poseStack, collector, camera);
    }

    @Override
    public CapybaraRenderState createRenderState() {
        return new CapybaraRenderState();
    }

    @Override
    public Identifier getTextureLocation(CapybaraRenderState state) {
        return state.isBaby ? BABY_TEXTURE : TEXTURE;
    }
}
