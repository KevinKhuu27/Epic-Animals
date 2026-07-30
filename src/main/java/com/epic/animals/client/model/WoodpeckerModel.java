package com.epic.animals.client.model;

import com.epic.animals.EpicAnimals;
import com.epic.animals.client.state.WoodpeckerRenderState;
import com.epic.animals.entity.Woodpecker;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public class WoodpeckerModel extends EntityModel<WoodpeckerRenderState> {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath(EpicAnimals.MODID, "woodpecker"), "main");

    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart leftWing;
    private final ModelPart rightWing;
    private final ModelPart tail;
    private final ModelPart leftLeg;
    private final ModelPart rightLeg;

    public WoodpeckerModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.leftWing = root.getChild("left_wing");
        this.rightWing = root.getChild("right_wing");
        this.tail = root.getChild("tail");
        this.leftLeg = root.getChild("left_leg");
        this.rightLeg = root.getChild("right_leg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -7.0F, -1.75F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 9).addBox(-2.5F, -6.25F, -0.25F, 4.0F, 4.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(18, 9).addBox(-2.0F, -5.75F, 0.5F, 3.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 24.0F, 0.0F));

        PartDefinition tail = partdefinition.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(20, 0).addBox(-1.0F, -2.25F, 0.0F, 2.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(20, 6).addBox(-0.5F, -1.25F, 3.75F, 1.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 23.0F, 3.0F, -0.1745F, 0.0F, 0.0F));

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 18).addBox(-2.0F, -8.0F, -3.5F, 3.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(34, 7).addBox(-1.5F, -5.0F, -7.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(16, 18).addBox(-2.5F, -7.5F, -3.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 20.0F, -1.0F));

        PartDefinition feather_r1 = head.addOrReplaceChild("feather_r1", CubeListBuilder.create().texOffs(28, 26).addBox(0.0F, -14.5F, -1.5F, 0.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 0.0F, -7.0F, -0.7418F, 0.0F, 0.0F));

        PartDefinition right_wing = partdefinition.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(0, 28).addBox(-1.0F, 0.0F, -1.0F, 1.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 18.25F, 0.0F));

        PartDefinition right_wing_tip_r1 = right_wing.addOrReplaceChild("right_wing_tip_r1", CubeListBuilder.create().texOffs(34, 0).addBox(-5.0F, -3.0F, -1.0F, 0.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.25F, 4.0F, 0.75F, -0.2182F, 0.0F, 0.0F));

        PartDefinition left_wing = partdefinition.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(16, 26).addBox(0.0F, 0.0F, -1.0F, 1.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 18.25F, 0.0F));

        PartDefinition left_wing_tip_r1 = left_wing.addOrReplaceChild("left_wing_tip_r1", CubeListBuilder.create().texOffs(32, 18).addBox(-5.0F, -3.0F, -1.0F, 0.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.75F, 4.0F, 0.75F, -0.2182F, 0.0F, 0.0F));

        PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(12, 32).addBox(-0.25F, -1.25F, 0.25F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, 22.25F, 0.5F));

        PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(12, 28).addBox(0.25F, -1.25F, 0.25F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 22.25F, 0.5F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(WoodpeckerRenderState state) {
        super.setupAnim(state);
        this.prepare(state.pose);
        this.head.yRot = state.yRot * ((float) Math.PI / 180F);
        this.head.xRot = state.xRot * ((float) Math.PI / 180F);

        switch (state.pose) {
            case FLYING -> applyFlap(state);
            case STANDING -> {
                this.leftLeg.xRot += Mth.cos(state.walkAnimationPos * 0.6662F)
                        * 1.4F * state.walkAnimationSpeed;
                this.rightLeg.xRot += Mth.cos(state.walkAnimationPos * 0.6662F + (float) Math.PI)
                        * 1.4F * state.walkAnimationSpeed;
                applyFlap(state);
            }
            case SITTING -> { }
        }
    }

    private void applyFlap(WoodpeckerRenderState state) {
        this.rightWing.zRot = 0.0873F + state.flapAngle;
        this.leftWing.zRot = -0.0873F - state.flapAngle;
        this.tail.xRot += state.flapAngle * 0.1F;
    }

    private void prepare(Pose pose) {
        switch (pose) {
            case FLYING -> {
                this.leftLeg.xRot += 0.6981317F;
                this.rightLeg.xRot += 0.6981317F;
            }
            case SITTING -> {
                this.body.y += 1.0F;
                this.head.y += 1.0F;
                this.tail.y += 1.0F;
                this.tail.xRot += (float) Math.PI / 6F;
                this.leftWing.zRot = 0.0F;
                this.rightWing.zRot = 0.0F;
                this.leftWing.y += 1.0F;
                this.rightWing.y += 1.0F;
                this.leftLeg.y += 1.0F;
                this.rightLeg.y += 1.0F;
            }
            case STANDING -> {}
        }
    }

    public static Pose getPose(Woodpecker entity) {
        if (entity.isInSittingPose()) {
            return Pose.SITTING;
        }
        return entity.isFlying() ? Pose.FLYING : Pose.STANDING;
    }

    public enum Pose {
        FLYING,
        STANDING,
        SITTING
        // TODO Phase 7: ON_SHOULDER
    }
}