package com.brandon3055.tolkientweaks.client.rendering;

import codechicken.lib.render.item.IItemRenderer;
import codechicken.lib.texture.TextureUtils;
import codechicken.lib.util.TransformUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.model.IModelState;

/**
 * Created by Brandon on 13/01/2015.
 */
public class ItemLockableChestRenderer implements IItemRenderer {

    private final ModelChest simpleChest = new ModelChest();

    //region Unused
    @Override
    public boolean isAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return TextureUtils.getTexture(RenderTileLockableChest.TEXTURE_NORMAL);
    }

    //endregion

    @Override
    public void renderItem(ItemStack stack, ItemCameraTransforms.TransformType transformType) {
        GlStateManager.enableLighting();
        GlStateManager.pushMatrix();
        Minecraft.getMinecraft().renderEngine.bindTexture(RenderTileLockableChest.TEXTURE_NORMAL);
        GlStateManager.translate(0.5, 0.5, 0.5);
        GlStateManager.rotate(180, 0, 0, 1);
        GlStateManager.translate(-0.5, -0.5, -0.5);
        simpleChest.renderAll();
        GlStateManager.popMatrix();
    }

    @Override
    public IModelState getTransforms() {
        return TransformUtils.DEFAULT_BLOCK;
    }
}
