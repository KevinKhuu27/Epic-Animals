package com.epic.animals.client.model;

import com.epic.animals.client.state.CapybaraRenderState;
import com.epic.animals.EpicAnimals;
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

    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart rightHindLeg;
    private final ModelPart leftHindLeg;
    private final ModelPart rightFrontLeg;
    private final ModelPart leftFrontLeg;

    public CapybaraModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.rightHindLeg = root.getChild("right_hind_leg");
        this.leftHindLeg = root.getChild("left_hind_leg");
        this.rightFrontLeg = root.getChild("right_front_leg");
        this.leftFrontLeg = root.getChild("left_front_leg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition head = root.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(0, 22).addBox(-2.5F, -6.0F, -7.0F, 5.0F, 7.0F, 8.0F),
                PartPose.offset(0.0F, 15.0F, -5.0F));

        head.addOrReplaceChild("right_ear",
                CubeListBuilder.create().texOffs(24, 38).addBox(0.0F, -2.0F, -1.0F, 1.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(-3.0F, -4.5F, -0.75F, 0.0F, 0.0F, -0.4363F));
        head.addOrReplaceChild("left_ear",
                CubeListBuilder.create().texOffs(38, 22).addBox(0.0F, -2.0F, -1.0F, 1.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(2.0F, -5.0F, -0.75F, 0.0F, 0.0F, 0.4363F));

        root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -8.0F, -7.0F, 8.0F, 8.0F, 14.0F),
                PartPose.offsetAndRotation(3.0F, 21.0F, 0.0F, -0.1222F, 0.0F, 0.0F));

        root.addOrReplaceChild("left_hind_leg",
                CubeListBuilder.create().texOffs(26, 22).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F),
                PartPose.offset(2.0F, 19.0F, 6.5F));
        root.addOrReplaceChild("right_hind_leg",
                CubeListBuilder.create().texOffs(12, 37).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F),
                PartPose.offset(-2.0F, 19.0F, 6.5F));
        root.addOrReplaceChild("left_front_leg",
                CubeListBuilder.create().texOffs(0, 37).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F),
                PartPose.offset(2.0F, 19.0F, -3.5F));
        root.addOrReplaceChild("right_front_leg",
                CubeListBuilder.create().texOffs(26, 30).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F),
                PartPose.offset(-2.0F, 19.0F, -3.5F));

        return LayerDefinition.create(mesh, 48, 48);
    }

    @Override
    public void setupAnim(CapybaraRenderState state) {
        super.setupAnim(state);
        this.head.xRot = state.xRot * ((float) Math.PI / 180F);
        this.head.yRot = state.yRot * ((float) Math.PI / 180F);

        if (state.isSitting) {
            this.body.xRot = -0.25F;
            this.body.y = 21.0F + 1.0F;

            this.rightHindLeg.y = 19.0F;
            this.leftHindLeg.y  = 19.0F;

            this.rightHindLeg.z = 4.0F;
            this.leftHindLeg.z = 4.0F;
        } else {
            float pos = state.walkAnimationPos;
            float speed = state.walkAnimationSpeed;
            this.rightHindLeg.xRot  = Mth.cos(pos) * 1.4F * speed;
            this.leftHindLeg.xRot   = Mth.cos(pos + (float) Math.PI) * 1.4F * speed;
            this.rightFrontLeg.xRot = this.leftHindLeg.xRot;
            this.leftFrontLeg.xRot  = this.rightHindLeg.xRot;
        }
    }
}