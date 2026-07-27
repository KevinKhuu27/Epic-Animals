package com.epic.animals.client.renderer;

import com.epic.animals.client.state.CapybaraRenderState;
import com.epic.animals.EpicAnimals;
import com.epic.animals.client.model.CapybaraModel;
import com.epic.animals.entity.Capybara;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

public class CapybaraRenderer extends MobRenderer<Capybara, CapybaraRenderState, CapybaraModel> {
    private static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(EpicAnimals.MODID, "textures/entity/capybara.png");
    private static final Identifier BABY_TEXTURE =
            Identifier.fromNamespaceAndPath(EpicAnimals.MODID, "textures/entity/capybara_baby.png");

    public CapybaraRenderer(EntityRendererProvider.Context context) {
        super(context, new CapybaraModel(context.bakeLayer(CapybaraModel.LAYER)), 0.5F);
    }

    @Override
    protected void scale(CapybaraRenderState state, PoseStack poseStack) {
        float s = state.ageScale;
        poseStack.scale(s, s, s);
    }

    @Override
    public CapybaraRenderState createRenderState() {
        return new CapybaraRenderState();
    }

    @Override
    public Identifier getTextureLocation(CapybaraRenderState state) {
        return state.isBaby ? BABY_TEXTURE : TEXTURE;
    }

    @Override
    public void extractRenderState(Capybara entity, CapybaraRenderState state, float particalTick) {
        super.extractRenderState(entity, state, particalTick);
        state.isSitting = entity.isInSittingPose();
    }
}
