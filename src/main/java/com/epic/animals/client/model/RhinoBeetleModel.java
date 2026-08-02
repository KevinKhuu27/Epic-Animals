package com.epic.animals.client.model;

import com.epic.animals.EpicAnimals;
import com.epic.animals.client.animation.RhinoBeetleAnimation;
import com.epic.animals.client.state.RhinoBeetleRenderState;

import net.minecraft.client.animation.KeyframeAnimation;
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

public class RhinoBeetleModel extends EntityModel<RhinoBeetleRenderState> {

    public static final ModelLayerLocation LAYER = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath(EpicAnimals.MODID, "rhino_beetle"), "main");

    private final ModelPart abdomen;
    private final ModelPart head;
    private final ModelPart thorax;
    private final ModelPart frontR;
    private final ModelPart frontL;
    private final ModelPart middleR;
    private final ModelPart middleL;
    private final ModelPart backR;
    private final ModelPart backL;

    private final KeyframeAnimation walkAnimation;
    private final KeyframeAnimation idleAnimation;

    public RhinoBeetleModel(ModelPart root) {
        super(root);
        this.abdomen = root.getChild("abdomen");
        this.head = root.getChild("head");
        this.thorax = root.getChild("thorax");
        this.frontR = root.getChild("FRONT R");
        this.frontL = root.getChild("FRONT L");
        this.middleR = root.getChild("MIDDLE R");
        this.middleL = root.getChild("MIDDLE L");
        this.backR = root.getChild("BACK R");
        this.backL = root.getChild("BACK L");
        this.walkAnimation = RhinoBeetleAnimation.WALK.bake(root);
        this.idleAnimation = RhinoBeetleAnimation.IDLE.bake(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("abdomen", CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-5.0F, -3.0F, 6.5F, 10.0F, 4.0F, 10.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 25).addBox(-4.0F, -2.0F, 9.5F, 8.0F, 3.0F, 8.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 14).addBox(-6.0F, -2.0F, 7.5F, 12.0F, 3.0F, 8.0F, new CubeDeformation(0.0F))
                        .texOffs(32, 25).addBox(-4.0F, -4.0F, 7.5F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
                        .texOffs(40, 0).addBox(-3.0F, -5.0F, 8.5F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 23.0F, -4.0F));

        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create()
                        .texOffs(40, 17).addBox(-3.0F, -4.0F, -6.0F, 6.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        head.addOrReplaceChild("cube_r1", CubeListBuilder.create()
                        .texOffs(62, 8).addBox(-1.0F, -2.0F, -1.0F, 1.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(1.75F, 0.5F, -5.0F, 0.4909F, -0.7193F, 0.0502F));
        head.addOrReplaceChild("cube_r2", CubeListBuilder.create()
                        .texOffs(58, 61).addBox(0.0F, -2.0F, -1.0F, 1.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.75F, 0.5F, -5.0F, 0.4909F, 0.7193F, -0.0502F));
        head.addOrReplaceChild("tipR_r1", CubeListBuilder.create()
                        .texOffs(52, 61).mirror().addBox(-0.5F, -0.0872F, -2.0F, 0.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offsetAndRotation(-0.75F, -9.6628F, -7.4364F, 1.013F, -0.2804F, -0.4353F));
        head.addOrReplaceChild("horn_r1", CubeListBuilder.create()
                        .texOffs(0, 50).mirror().addBox(-0.5F, -0.0872F, -2.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offsetAndRotation(0.0F, -5.4128F, -7.4364F, 1.9569F, 0.0146F, 0.0028F));
        head.addOrReplaceChild("horn_r2", CubeListBuilder.create()
                        .texOffs(0, 44).mirror().addBox(-2.5F, -1.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offsetAndRotation(0.5F, -3.4128F, -4.6864F, 2.8732F, 0.0146F, 0.0028F));
        head.addOrReplaceChild("horn_r3", CubeListBuilder.create()
                        .texOffs(0, 50).addBox(-0.5F, -0.0872F, -2.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -5.4128F, -7.4364F, 1.9569F, -0.0146F, -0.0028F));
        head.addOrReplaceChild("tipR_r2", CubeListBuilder.create()
                        .texOffs(52, 61).addBox(0.5F, -0.0872F, -2.0F, 0.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.75F, -9.6628F, -7.4364F, 1.013F, 0.2804F, 0.4353F));
        head.addOrReplaceChild("horn_r4", CubeListBuilder.create()
                        .texOffs(0, 44).addBox(-1.5F, -1.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-0.5F, -3.4128F, -4.6864F, 2.8732F, -0.0146F, -0.0028F));

        PartDefinition thorax = root.addOrReplaceChild("thorax", CubeListBuilder.create(),
                PartPose.offset(0.0F, 24.0F, -4.0F));
        thorax.addOrReplaceChild("horn 3_r1", CubeListBuilder.create()
                        .texOffs(28, 40).addBox(0.0F, -3.0F, 0.0F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -8.5F, 2.25F, 1.1781F, 0.0F, 0.0F));
        thorax.addOrReplaceChild("horn2_r1", CubeListBuilder.create()
                        .texOffs(30, 60).addBox(-1.0F, -4.0F, 0.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.5F, -6.0F, 3.25F, 0.6545F, 0.0F, 0.0F));
        thorax.addOrReplaceChild("horn_r5", CubeListBuilder.create()
                        .texOffs(44, 45).addBox(-1.0F, -4.0F, -1.0F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-0.5F, -4.0F, 4.25F, 0.3054F, 0.0F, 0.0F));
        thorax.addOrReplaceChild("main3 (sides)_r1", CubeListBuilder.create()
                        .texOffs(0, 36).addBox(-4.0F, -3.0F, -2.0F, 9.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-0.5F, -1.0F, 3.5F, -0.2776F, 0.0F, 0.0F));
        thorax.addOrReplaceChild("main2_r1", CubeListBuilder.create()
                        .texOffs(40, 8).addBox(-2.0F, -4.0F, -2.0F, 6.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.0F, -3.0F, 3.5F, -0.3213F, 0.0F, 0.0F));
        thorax.addOrReplaceChild("main1_r1", CubeListBuilder.create()
                        .texOffs(32, 35).addBox(-5.0F, -3.0F, -1.0F, 8.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(1.0F, -2.0F, 2.0F, -0.2618F, 0.0F, 0.0F));

        PartDefinition frontR = root.addOrReplaceChild("FRONT R", CubeListBuilder.create(),
                PartPose.offset(-3.9675F, 21.2513F, -1.6843F));
        frontR.addOrReplaceChild("cube_r3", CubeListBuilder.create()
                        .texOffs(0, 56).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-4.0325F, -0.2513F, -4.5657F, 0.4368F, -0.0357F, -0.025F));
        frontR.addOrReplaceChild("tarsal_r1", CubeListBuilder.create()
                        .texOffs(60, 38).addBox(-2.0F, 0.0F, -1.0F, 3.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-2.5F, 2.0F, -9.25F, 0.9532F, -1.2408F, 0.4167F));
        frontR.addOrReplaceChild("SEGMENT 3_r1", CubeListBuilder.create()
                        .texOffs(28, 52).addBox(-6.0F, -2.0F, -1.0F, 9.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.5325F, 0.2487F, -3.3157F, 0.4962F, -1.1054F, -1.7629F));
        frontR.addOrReplaceChild("SEGMENT 2_r1", CubeListBuilder.create()
                        .texOffs(60, 42).addBox(-2.0F, -2.0F, 0.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.7825F, 0.7487F, -1.3157F, 0.2828F, -0.2266F, 0.3938F));
        frontR.addOrReplaceChild("SEGMENT 1_r1", CubeListBuilder.create()
                        .texOffs(56, 45).addBox(-2.0F, -2.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-0.0325F, 0.9987F, -0.3157F, -0.0808F, -0.4293F, 0.1922F));

        PartDefinition frontL = root.addOrReplaceChild("FRONT L", CubeListBuilder.create(),
                PartPose.offset(3.9675F, 21.2513F, -1.6843F));
        frontL.addOrReplaceChild("cube_r4", CubeListBuilder.create()
                        .texOffs(24, 54).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(4.0325F, -0.2513F, -4.5657F, 0.4368F, 0.0357F, 0.025F));
        frontL.addOrReplaceChild("tarsal_r2", CubeListBuilder.create()
                        .texOffs(60, 38).mirror().addBox(-1.0F, 0.0F, -1.0F, 3.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offsetAndRotation(2.5F, 2.0F, -9.25F, 0.9532F, 1.2408F, -0.4167F));
        frontL.addOrReplaceChild("S2_r1", CubeListBuilder.create()
                        .texOffs(36, 61).addBox(-1.0F, -2.0F, 0.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(1.7825F, 0.7487F, -1.3157F, 0.2828F, 0.2266F, -0.3938F));
        frontL.addOrReplaceChild("S3_r1", CubeListBuilder.create()
                        .texOffs(12, 51).addBox(-3.0F, -2.0F, -1.0F, 9.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(1.5325F, 0.2487F, -3.3157F, 0.4962F, 1.1054F, 1.7629F));
        frontL.addOrReplaceChild("S1_r1", CubeListBuilder.create()
                        .texOffs(46, 57).addBox(-1.0F, -2.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0325F, 0.9987F, -0.3157F, -0.0808F, 0.4293F, -0.1922F));

        PartDefinition middleR = root.addOrReplaceChild("MIDDLE R", CubeListBuilder.create(),
                PartPose.offset(-4.5F, 22.75F, -0.25F));
        middleR.addOrReplaceChild("tarsal_r3", CubeListBuilder.create()
                        .texOffs(60, 38).addBox(-2.0F, 0.0F, -1.0F, 3.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-6.0F, 0.75F, 2.5F, 1.1781F, 0.4363F, 0.0F));
        middleR.addOrReplaceChild("cube_r5", CubeListBuilder.create()
                        .texOffs(28, 38).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-5.0F, -2.0F, 1.5F, 0.6109F, 0.0F, 0.3927F));
        middleR.addOrReplaceChild("S3_r2", CubeListBuilder.create()
                        .texOffs(12, 58).addBox(-3.0F, -1.0F, -1.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-3.5F, -1.0F, 1.0F, 0.0F, 0.6545F, -0.9599F));
        middleR.addOrReplaceChild("S2_r2", CubeListBuilder.create()
                        .texOffs(44, 61).addBox(-4.0F, -2.0F, -1.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.75F, 0.0F, -1.0F, -1.7204F, -0.0837F, 0.472F));
        middleR.addOrReplaceChild("S1_r2", CubeListBuilder.create()
                        .texOffs(56, 53).addBox(-2.0F, -2.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.074F, -0.1247F, 0.1677F));

        PartDefinition middleL = root.addOrReplaceChild("MIDDLE L", CubeListBuilder.create(),
                PartPose.offset(4.5F, 22.75F, -0.25F));
        middleL.addOrReplaceChild("cube_r6", CubeListBuilder.create()
                        .texOffs(60, 40).addBox(-1.0F, 0.0F, -1.0F, 3.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(6.0F, 0.75F, 2.5F, 1.1781F, -0.4363F, 0.0F));
        middleL.addOrReplaceChild("S3_r3", CubeListBuilder.create()
                        .texOffs(24, 58).addBox(-2.0F, -1.0F, -1.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(3.5F, -1.0F, 1.0F, 0.0F, -0.6545F, 0.9599F));
        middleL.addOrReplaceChild("cube_r7", CubeListBuilder.create()
                        .texOffs(28, 36).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(5.0F, -2.0F, 1.5F, 0.6109F, 0.0F, -0.3927F));
        middleL.addOrReplaceChild("S2_r3", CubeListBuilder.create()
                        .texOffs(36, 54).addBox(1.0F, -2.0F, -1.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-0.75F, 0.0F, -1.0F, -1.7204F, 0.0837F, -0.472F));
        middleL.addOrReplaceChild("S1_r3", CubeListBuilder.create()
                        .texOffs(56, 57).addBox(-1.0F, -2.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.074F, 0.1247F, -0.1677F));

        PartDefinition backR = root.addOrReplaceChild("BACK R", CubeListBuilder.create(),
                PartPose.offset(-5.0F, 24.0F, 2.0F));
        backR.addOrReplaceChild("cube_r8", CubeListBuilder.create()
                        .texOffs(20, 60).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-5.0F, 0.25F, 2.25F, -0.2184F, 0.0426F, -0.0094F));
        backR.addOrReplaceChild("cube_r9", CubeListBuilder.create()
                        .texOffs(60, 38).addBox(-2.0F, 0.0F, -1.0F, 3.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-4.0F, -0.25F, 7.5F, -0.6329F, 0.0725F, 0.0588F));
        backR.addOrReplaceChild("S2_r4", CubeListBuilder.create()
                        .texOffs(0, 60).addBox(-2.0F, -2.0F, -1.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.0F, -1.0F, 1.75F, 1.0036F, 0.4363F, 0.0F));
        backR.addOrReplaceChild("S3_r4", CubeListBuilder.create()
                        .texOffs(16, 44).addBox(0.0F, -2.0F, -1.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-4.0F, -0.25F, 2.0F, -0.2578F, 0.0799F, -0.0379F));
        backR.addOrReplaceChild("S1_r4", CubeListBuilder.create()
                        .texOffs(36, 57).addBox(-2.0F, -2.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.5F, -0.75F, -0.25F, -0.0771F, 0.3104F, 0.1349F));

        PartDefinition backL = root.addOrReplaceChild("BACK L", CubeListBuilder.create(),
                PartPose.offset(5.0F, 24.0F, 2.0F));
        backL.addOrReplaceChild("cube_r10", CubeListBuilder.create()
                        .texOffs(60, 35).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(5.0F, 0.25F, 2.25F, -0.2184F, -0.0426F, 0.0094F));
        backL.addOrReplaceChild("cube_r11", CubeListBuilder.create()
                        .texOffs(60, 40).addBox(-1.0F, 0.0F, -1.0F, 3.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(4.0F, -0.25F, 7.5F, -0.6329F, -0.0725F, -0.0588F));
        backL.addOrReplaceChild("S1_r5", CubeListBuilder.create()
                        .texOffs(56, 49).addBox(-1.0F, -2.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-0.5F, -0.75F, -0.25F, -0.0771F, -0.3104F, -0.1349F));
        backL.addOrReplaceChild("S3_r5", CubeListBuilder.create()
                        .texOffs(30, 45).addBox(-1.0F, -2.0F, -1.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(4.0F, -0.25F, 2.0F, -0.2578F, -0.0799F, 0.0379F));
        backL.addOrReplaceChild("S2_r5", CubeListBuilder.create()
                        .texOffs(10, 60).addBox(-1.0F, -2.0F, -1.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(1.0F, -1.0F, 1.75F, 1.0036F, -0.4363F, 0.0F));

        return LayerDefinition.create(mesh, 128, 128);
    }

    @Override
    public void setupAnim(RhinoBeetleRenderState state) {
        super.setupAnim(state);
        this.walkAnimation.applyWalk(state.walkAnimationPos, state.walkAnimationSpeed, 4.0F, 2.5F);
        this.idleAnimation.apply(state.idleAnimationState, state.ageInTicks);
    }
}