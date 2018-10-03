package com.brandon3055.tolkientweaks.client.rendering;

import codechicken.lib.render.item.IItemRenderer;
import codechicken.lib.util.TransformUtils;
import com.brandon3055.brandonscore.client.BCClientEventHandler;
import com.brandon3055.brandonscore.client.ResourceHelperBC;
import com.brandon3055.brandonscore.utils.ModelUtils;
import com.brandon3055.tolkientweaks.TTFeatures;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelState;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * Created by Brandon on 13/01/2015.
 */
public class CustomBlockItemRenderer implements IItemRenderer {

    private static final List<Block> BLOCKS = Lists.newArrayList(Blocks.STONE, Blocks.SAND, Blocks.GRASS, Blocks.COBBLESTONE, Blocks.LOG, Blocks.GLASS, Blocks.MYCELIUM, Blocks.CRAFTING_TABLE, Blocks.BOOKSHELF, Blocks.DIAMOND_ORE, Blocks.OBSIDIAN, Blocks.DIRT, Blocks.DISPENSER, Blocks.FURNACE, Blocks.HAY_BLOCK);
    private static final ItemStack CHEST = new ItemStack(Blocks.CHEST);
    private static final ItemStack BUCKET = new ItemStack(Items.BUCKET);
    private static final ItemStack GLOWSTONE = new ItemStack(Blocks.GLOWSTONE);
    private static final ItemStack KEY = new ItemStack(TTFeatures.key);
    private static final ItemStack BLAZE = new ItemStack(Items.BLAZE_POWDER);
    private static final ItemStack SPAWNER = new ItemStack(Blocks.MOB_SPAWNER);

    public static ResourceLocation texture = new ResourceLocation("tolkientweaks:textures/blocks/milestone.png");
    private Block type;

    public CustomBlockItemRenderer(Block type) {
        this.type = type;
    }

    //region Unused
    @Override
    public boolean isAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    //endregion

    @Override
    public void renderItem(ItemStack stack, ItemCameraTransforms.TransformType transformType) {
        GlStateManager.enableLighting();
        if (type == TTFeatures.milestone) {
            renderMilestone();
        }
        else if (type == TTFeatures.fluidSource) {
            renderCamoBlock(BUCKET, true);
        }
        else if (type == TTFeatures.camoChest) {
            renderCamoBlock(CHEST, false);
        }
        else if (type == TTFeatures.camoGlowstone) {
            renderCamoBlock(GLOWSTONE, false);
        }
        else if (type == TTFeatures.camoKeystone) {
            renderCamoBlock(KEY, true);
        }
        else if (type == TTFeatures.smoker) {
            renderCamoBlock(BLAZE, true);
        }
        else if (type == TTFeatures.camoSpawner) {
            renderCamoBlock(SPAWNER, false);
        }
    }

    private static void renderMilestone() {
        GL11.glPushMatrix();
        GL11.glTranslated(0.5, 1, 0.5);
        GL11.glScalef(0.8F, 0.8F, 0.8F);
        GL11.glRotated(180, 0, 0, 1);
        ResourceHelperBC.bindTexture(texture);

        RenderTileMilestone.model.render(null, 0, 0, 0, 0, 0, 1F / 16F);

        FontRenderer fr = Minecraft.getMinecraft().standardGalacticFontRenderer;

        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslated(0.5, 0.1, 0.5);
        double scale = (1D / 28D) * 0.7;
        GL11.glScaled(scale, scale, scale);
        GL11.glRotated(90, 0, 0, 1);
        GL11.glDisable(GL11.GL_LIGHTING);

        for (int i = 0; i < 4; i++) {
            GL11.glPushMatrix();
            GL11.glRotated(i * 90, 1, 0, 0);
            GL11.glRotatef(-3, 0, 1, 0);
            GL11.glTranslated(2, -3.5, -10.2);

            fr.drawString("TheOneRing", 0, 0, 0xFFFFFF);

            GL11.glPopMatrix();
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }

    private static void renderCamoBlock(ItemStack type, boolean item) {
        Minecraft mc = Minecraft.getMinecraft();
        GlStateManager.pushMatrix();
        List<BakedQuad> quads = ModelUtils.getModelQuads(BLOCKS.get((BCClientEventHandler.elapsedTicks / 20) % BLOCKS.size()).getDefaultState());
        GlStateManager.disableDepth();
        ModelUtils.renderQuadsARGB(quads, 0x50FFFFFF);
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();


        GlStateManager.pushMatrix();
        GlStateManager.translate(0.5, 0.5, 0.5);
        if (item){
            GlStateManager.scale(1.5, 1.5, 1.5);
        }
        else {
            GlStateManager.scale(0.9, 0.9, 0.9);
        }
        GlStateManager.rotate(20, 1, 0, 1);
        GlStateManager.rotate((BCClientEventHandler.elapsedTicks + mc.getRenderPartialTicks()) * 1.5F, 0, 1, 0);
        GlStateManager.disableDepth();
        mc.getRenderItem().renderItem(type, ItemCameraTransforms.TransformType.NONE);
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
    }

    @Override
    public IModelState getTransforms() {
        return TransformUtils.DEFAULT_BLOCK;
    }
}
