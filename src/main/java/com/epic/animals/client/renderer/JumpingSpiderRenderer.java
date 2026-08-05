package com.epic.animals.client.renderer;

import com.epic.animals.EpicAnimals;
import com.epic.animals.client.model.JumpingSpiderModel;
import com.epic.animals.client.state.JumpingSpiderRenderState;
import com.epic.animals.entity.JumpingSpider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

public class JumpingSpiderRenderer
        extends MobRenderer<JumpingSpider, JumpingSpiderRenderState, JumpingSpiderModel> {

    private static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(EpicAnimals.MODID, "textures/entity/jumping_spider.png");

    public JumpingSpiderRenderer(EntityRendererProvider.Context context) {
        super(context, new JumpingSpiderModel(context.bakeLayer(JumpingSpiderModel.LAYER)), 0.4F);
    }

    @Override
    public JumpingSpiderRenderState createRenderState() {
        return new JumpingSpiderRenderState();
    }

    @Override
    public Identifier getTextureLocation(JumpingSpiderRenderState state) {
        return TEXTURE;
    }
}
