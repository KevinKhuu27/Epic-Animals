package com.epic.animals.block;

import com.epic.animals.ModEntities;
import com.epic.animals.entity.RhinoBeetle;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RhinoBeetleEggBlock extends Block {
    public static final MapCodec<RhinoBeetleEggBlock> CODEC = simpleCodec(RhinoBeetleEggBlock::new);
    public static final IntegerProperty HATCH = BlockStateProperties.HATCH;
    public static final int MAX_HATCH_LEVEL = 2;
    private static final VoxelShape SHAPE = Block.box(4.0, 0.0, 3.0, 12.0, 7.0, 13.0);

    @Override
    public MapCodec<RhinoBeetleEggBlock> codec() {
        return CODEC;
    }

    public RhinoBeetleEggBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(HATCH, 0));
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!this.shouldUpdateHatchLevel(level)) {
            return;
        }
        int hatch = state.getValue(HATCH);
        if (hatch < MAX_HATCH_LEVEL) {
            level.playSound(null, pos, SoundEvents.TURTLE_EGG_CRACK, SoundSource.BLOCKS, 0.7F, 0.9F + random.nextFloat() * 0.2F);
            level.setBlock(pos, state.setValue(HATCH, hatch + 1), 2);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(state));
        } else {
            level.playSound(null, pos, SoundEvents.TURTLE_EGG_HATCH, SoundSource.BLOCKS, 0.7F, 0.9F + random.nextFloat() * 0.2F);
            level.levelEvent(2001, pos, Block.getId(state));
            level.removeBlock(pos, false);
            level.gameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(state));

            RhinoBeetle baby = ModEntities.RHINO_BEETLE.get().create(level, EntitySpawnReason.BREEDING);
            if (baby != null) {
                baby.setBaby(true);
                baby.snapTo(pos.getX() + 0.3, pos.getY(), pos.getZ() + 0.3, 0.0F, 0.0F);
                level.addFreshEntity(baby);
            }
        }
    }

    private boolean shouldUpdateHatchLevel(ServerLevel level) {
        float chance = level.getSkyDarken() < 4 ? 0.02F : 0.005F;
        return level.getRandom().nextFloat() < chance;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HATCH);
    }
}
