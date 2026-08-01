package com.epic.animals.client.renderer;

import com.epic.animals.EpicAnimals;
import com.epic.animals.client.model.RhinoBeetleModel;
import com.epic.animals.client.state.RhinoBeetleRenderState;
import com.epic.animals.entity.RhinoBeetle;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class RhinoBeetleRenderer
        extends MobRenderer<RhinoBeetle, RhinoBeetleRenderState, RhinoBeetleModel> {

    private static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(EpicAnimals.MODID, "textures/entity/rhino_beetle.png");

    public RhinoBeetleRenderer(EntityRendererProvider.Context context) {
        super(context, new RhinoBeetleModel(context.bakeLayer(RhinoBeetleModel.LAYER)), 0.4F);
    }

    @Override
    public RhinoBeetleRenderState createRenderState() {
        return new RhinoBeetleRenderState();
    }

    @Override
    protected void scale(RhinoBeetleRenderState state, PoseStack poseStack) {
        float s = state.ageScale;
        poseStack.scale(s, s, s);
    }

    @Override
    public Identifier getTextureLocation(RhinoBeetleRenderState state) {
        return TEXTURE;
    }
}
