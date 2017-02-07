package com.brandon3055.tolkientweaks.client.rendering.model;

import com.brandon3055.tolkientweaks.blocks.ChameleonBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IExtendedBlockState;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by brandon3055 on 28/10/2016.
 */
public class ModelChameleon implements IBakedModel {
    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        List<BakedQuad> override = null;
        if (state != null && state.getBlock() instanceof ChameleonBlock && (override = ((ChameleonBlock) state.getBlock()).getQuadOverrides(state, side)) != null) {
            return override;
        }

        IBlockState targetState;

        if (state instanceof IExtendedBlockState) {
            targetState = ((IExtendedBlockState)state).getValue(ChameleonBlock.TARGET_BLOCK_STATE);
        }
        else {
            targetState = Blocks.STONE.getDefaultState();
        }

//        MinecraftForgeClient.getRenderLayer()
        return Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(targetState).getQuads(targetState, side, rand);
    }

    @Override
    public boolean isAmbientOcclusion() {
        return true;
    }//this!

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return null;
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return ItemCameraTransforms.DEFAULT;
    }

    @Override
    public ItemOverrideList getOverrides() {
        return ItemOverrideList.NONE;
    }
}
