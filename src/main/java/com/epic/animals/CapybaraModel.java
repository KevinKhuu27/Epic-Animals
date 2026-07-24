package com.epic.animals;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public class CapybaraModel extends EntityModel<CapybaraRenderState> {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath(EpicAnimals.MODID, "capybara"), "main");
    public static final ModelLayerLocation BABY_LAYER = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath(EpicAnimals.MODID, "capybara_baby"), "main");

    private final ModelPart head;
    private final ModelPart rightHindLeg;
    private final ModelPart leftHindLeg;
    private final ModelPart rightFrontLeg;
    private final ModelPart leftFrontLeg;

    public CapybaraModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.rightHindLeg = root.getChild("right_hind_leg");
        this.leftHindLeg = root.getChild("left_hind_leg");
        this.rightFrontLeg = root.getChild("right_front_leg");
        this.leftFrontLeg = root.getChild("left_front_leg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition head = root.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -5.0F, -5.0F, 5.0F, 7.0F, 8.0F),
                PartPose.offset(0.0F, 13.0F, -7.0F));

        CubeListBuilder ear = CubeListBuilder.create()
                .texOffs(20, 24).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 2.0F);

        root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(26, 0).addBox(-4.0F, -7.0F, -4.0F, 8.0F, 14.0F, 8.0F),
                PartPose.offsetAndRotation(0.0F, 16.0F, 1.0F, 1.45F, 0.0F, 0.0F));

        CubeListBuilder leg = CubeListBuilder.create()
                .texOffs(0, 16).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F);

        head.addOrReplaceChild("right_ear", ear,
                PartPose.offsetAndRotation(-2.5F, -4.0F, 1.5F, 0.0F, 0.0F, -0.436F));
        head.addOrReplaceChild("left_ear", ear,
                PartPose.offsetAndRotation( 2.5F, -4.0F, 1.5F, 0.0F, 0.0F,  0.436F));

        root.addOrReplaceChild("right_hind_leg",  leg, PartPose.offset(-2.0F, 19.0F,  7.0F));
        root.addOrReplaceChild("left_hind_leg",   leg, PartPose.offset( 2.0F, 19.0F,  7.0F));
        root.addOrReplaceChild("right_front_leg", leg, PartPose.offset(-2.0F, 19.0F, -3.0F));
        root.addOrReplaceChild("left_front_leg",  leg, PartPose.offset( 2.0F, 19.0F, -3.0F));

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(CapybaraRenderState state) {
        super.setupAnim(state);
        this.head.xRot = state.xRot * ((float) Math.PI / 180F);
        this.head.yRot = state.yRot * ((float) Math.PI / 180F);

        float pos = state.walkAnimationPos;
        float speed = state.walkAnimationSpeed;
        this.rightHindLeg.xRot  = Mth.cos(pos) * 1.4F * speed;
        this.leftHindLeg.xRot   = Mth.cos(pos + (float) Math.PI) * 1.4F * speed;
        this.rightFrontLeg.xRot = this.leftHindLeg.xRot;
        this.leftFrontLeg.xRot  = this.rightHindLeg.xRot;
    }

}
