package com.epic.animals;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class BabyCapybaraModel extends CapybaraModel {
    public BabyCapybaraModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();


        PartDefinition head = root.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -4.5F, -4.0F, 4.0F, 5.0F, 6.0F),
                PartPose.offset(0.0F, 19.0F, -5.0F));

        CubeListBuilder ear = CubeListBuilder.create()
                .texOffs(20, 24).addBox(-0.5F, -3.5F, -0.75F, 1.0F, 1.5F, 1.5F);

        head.addOrReplaceChild("right_ear", ear,
                PartPose.offsetAndRotation(-1.0F, -2.0F, 1.0F, 0.0F, 0.0F, -0.436F));
        head.addOrReplaceChild("left_ear", ear,
                PartPose.offsetAndRotation( 1.0F, -2.0F, 1.0F, 0.0F, 0.0F,  0.436F));

        root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(26, 0).addBox(-3.0F, -4.5F, -3.0F, 6.0F, 8.0F, 5.0F),
                PartPose.offsetAndRotation(0.0F, 19.0F, 1.0F, 1.45F, 0.0F, 0.0F));

        CubeListBuilder leg = CubeListBuilder.create()
                .texOffs(0, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F);

        root.addOrReplaceChild("right_hind_leg",  leg, PartPose.offset(-1.5F, 21.0F,  3.0F));
        root.addOrReplaceChild("left_hind_leg",   leg, PartPose.offset( 1.5F, 21.0F,  3.0F));
        root.addOrReplaceChild("right_front_leg", leg, PartPose.offset(-1.5F, 21.0F, -2.0F));
        root.addOrReplaceChild("left_front_leg",  leg, PartPose.offset( 1.5F, 21.0F, -2.0F));

        return LayerDefinition.create(mesh, 64, 32);
    }
}
