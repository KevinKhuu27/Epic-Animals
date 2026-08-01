package com.epic.animals.client.model;

import com.epic.animals.EpicAnimals;
import com.epic.animals.client.state.RhinoBeetleRenderState;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.Identifier;

public class RhinoBeetleModel extends EntityModel<RhinoBeetleRenderState> {

    public static final ModelLayerLocation LAYER = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath(EpicAnimals.MODID, "rhino_beetle"), "main");

    private final ModelPart body;
    private final ModelPart head;

    public RhinoBeetleModel(ModelPart root) {
        super(root);
        this.body = root.getChild("body");
        this.head = root.getChild("head");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition parts = mesh.getRoot();

        parts.addOrReplaceChild("body",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-4.0F, -4.0F, -6.0F, 8.0F, 4.0F, 12.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition head = parts.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 16)
                        .addBox(-2.5F, -2.5F, -3.0F, 5.0F, 3.0F, 3.0F),
                PartPose.offset(0.0F, 22.0F, -6.0F));

        head.addOrReplaceChild("horn",
                CubeListBuilder.create()
                        .texOffs(22, 16)
                        .addBox(-0.5F, -4.0F, -2.0F, 1.0F, 3.0F, 1.0F),
                PartPose.offset(0.0F, -2.0F, -1.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(RhinoBeetleRenderState state) {
        super.setupAnim(state);
    }
}
