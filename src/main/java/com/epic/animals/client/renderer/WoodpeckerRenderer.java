package com.epic.animals.client.renderer;

import com.epic.animals.EpicAnimals;
import com.epic.animals.client.model.WoodpeckerModel;
import com.epic.animals.client.state.WoodpeckerRenderState;
import com.epic.animals.entity.Woodpecker;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public class WoodpeckerRenderer
        extends MobRenderer<Woodpecker, WoodpeckerRenderState, WoodpeckerModel> {

    private static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(EpicAnimals.MODID, "textures/entity/woodpecker.png");

    public WoodpeckerRenderer(EntityRendererProvider.Context context) {
        super(context, new WoodpeckerModel(context.bakeLayer(WoodpeckerModel.LAYER)), 0.3F);
    }

    @Override
    public WoodpeckerRenderState createRenderState() {
        return new WoodpeckerRenderState();
    }

    @Override
    public void extractRenderState(Woodpecker entity, WoodpeckerRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        float flap = Mth.lerp(partialTicks, entity.oFlap, entity.flap);
        float flapSpeed = Mth.lerp(partialTicks, entity.oFlapSpeed, entity.flapSpeed);
        state.flapAngle = (Mth.sin((double)flap) + 1.0F) * flapSpeed;
        state.pose = WoodpeckerModel.getPose(entity);
    }

    @Override
    public Identifier getTextureLocation(WoodpeckerRenderState state) {
        return TEXTURE;
    }

    @Override
    protected void scale(WoodpeckerRenderState state, PoseStack poseStack) {
        if (state.isBaby) {
            poseStack.scale(0.5F, 0.5F, 0.5F);
        }
    }
}
