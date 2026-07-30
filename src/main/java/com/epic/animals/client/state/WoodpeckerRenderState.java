package com.epic.animals.client.state;

import com.epic.animals.client.model.WoodpeckerModel;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public class WoodpeckerRenderState extends LivingEntityRenderState {
    public WoodpeckerModel.Pose pose = WoodpeckerModel.Pose.FLYING;
    public float flapAngle;
}
