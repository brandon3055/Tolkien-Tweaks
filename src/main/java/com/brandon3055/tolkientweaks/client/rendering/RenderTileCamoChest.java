package com.brandon3055.tolkientweaks.client.rendering;

import codechicken.lib.texture.TextureUtils;
import com.brandon3055.brandonscore.client.ResourceHelperBC;
import com.brandon3055.brandonscore.lib.Vec3D;
import com.brandon3055.tolkientweaks.tileentity.TileCamoChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraftforge.client.MinecraftForgeClient;
import org.lwjgl.opengl.GL11;

/**
 * Created by Brandon on 8/03/2015.
 * //
 */
public class RenderTileCamoChest extends TileEntitySpecialRenderer<TileCamoChest> //TODO Ask Covers
{
    private static TileEntityChest chest;

    public RenderTileCamoChest() {
        chest = new TileEntityChest();
    }

    @Override
    public void renderTileEntityAt(TileCamoChest te, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);

        double minxz = 0.0625;
        double maxxz = 0.9375;
        double maxy = 0.875;
        double tminy = 1 - 0.875;
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer buffer = tessellator.getBuffer();

        GlStateManager.disableLighting();

        //@formatter:off
        if (MinecraftForgeClient.getRenderPass() == 1) { //Transparent

            ResourceHelperBC.bindTexture(ResourceHelperBC.getResourceRAW("tolkientweaks:textures/blocks/camo_chest_top.png"));
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            //bottom
            buffer.pos(minxz, 0, minxz).tex(minxz, minxz).endVertex();
            buffer.pos(maxxz, 0, minxz).tex(maxxz, minxz).endVertex();
            buffer.pos(maxxz, 0, maxxz).tex(maxxz, maxxz).endVertex();
            buffer.pos(minxz, 0, maxxz).tex(minxz, maxxz).endVertex();
            //Top
            buffer.pos(minxz, maxy, minxz).tex(minxz, minxz).endVertex();
            buffer.pos(minxz, maxy, maxxz).tex(minxz, maxxz).endVertex();
            buffer.pos(maxxz, maxy, maxxz).tex(maxxz, maxxz).endVertex();
            buffer.pos(maxxz, maxy, minxz).tex(maxxz, minxz).endVertex();
            tessellator.draw();
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            ResourceHelperBC.bindTexture(ResourceHelperBC.getResourceRAW("tolkientweaks:textures/blocks/camo_chest_side.png"));
            //North
            buffer.pos(minxz, 0,    minxz).tex(maxxz, 1).endVertex();
            buffer.pos(minxz, maxy, minxz).tex(maxxz, tminy).endVertex();
            buffer.pos(maxxz, maxy, minxz).tex(minxz, tminy).endVertex();
            buffer.pos(maxxz, 0,    minxz).tex(minxz, 1).endVertex();
            //South
            buffer.pos(maxxz, 0,    maxxz).tex(maxxz, 1).endVertex();
            buffer.pos(maxxz, maxy, maxxz).tex(maxxz, tminy).endVertex();
            buffer.pos(minxz, maxy, maxxz).tex(minxz, tminy).endVertex();
            buffer.pos(minxz, 0,    maxxz).tex(minxz, 1).endVertex();
            //East
            buffer.pos(maxxz, 0,    minxz).tex(maxxz, 1).endVertex();
            buffer.pos(maxxz, maxy, minxz).tex(maxxz, tminy).endVertex();
            buffer.pos(maxxz, maxy, maxxz).tex(minxz, tminy).endVertex();
            buffer.pos(maxxz, 0,    maxxz).tex(minxz, 1).endVertex();
            //West
            buffer.pos(minxz, 0,    maxxz).tex(maxxz, 1).endVertex();
            buffer.pos(minxz, maxy, maxxz).tex(maxxz, tminy).endVertex();
            buffer.pos(minxz, maxy, minxz).tex(minxz, tminy).endVertex();
            buffer.pos(minxz, 0,    minxz).tex(minxz, 1).endVertex();
            tessellator.draw();
        }
        else {
            TextureAtlasSprite sprite = TextureUtils.getParticleIconForBlock(te.getChameleonBlockState());
            if (sprite == null) {
                sprite = TextureUtils.getMissingSprite();
            }
            TextureUtils.bindBlockTexture();

            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            //bottom
            buffer.pos(minxz, 0, minxz).tex(sprite.getMinU(), sprite.getMinV()).endVertex();
            buffer.pos(maxxz, 0, minxz).tex(sprite.getMaxU(), sprite.getMinV()).endVertex();
            buffer.pos(maxxz, 0, maxxz).tex(sprite.getMaxU(), sprite.getMaxV()).endVertex();
            buffer.pos(minxz, 0, maxxz).tex(sprite.getMinU(), sprite.getMaxV()).endVertex();
            //Top
            buffer.pos(minxz, maxy, minxz).tex(sprite.getMinU(), sprite.getMinV()).endVertex();
            buffer.pos(minxz, maxy, maxxz).tex(sprite.getMinU(), sprite.getMaxV()).endVertex();
            buffer.pos(maxxz, maxy, maxxz).tex(sprite.getMaxU(), sprite.getMaxV()).endVertex();
            buffer.pos(maxxz, maxy, minxz).tex(sprite.getMaxU(), sprite.getMinV()).endVertex();
            //North
            buffer.pos(minxz, 0,    minxz).tex(sprite.getMaxU(), sprite.getMaxV()).endVertex();
            buffer.pos(minxz, maxy, minxz).tex(sprite.getMaxU(), sprite.getMinV()).endVertex();
            buffer.pos(maxxz, maxy, minxz).tex(sprite.getMinU(), sprite.getMinV()).endVertex();
            buffer.pos(maxxz, 0,    minxz).tex(sprite.getMinU(), sprite.getMaxV()).endVertex();
            //South
            buffer.pos(maxxz, 0,    maxxz).tex(sprite.getMaxU(), sprite.getMaxV()).endVertex();
            buffer.pos(maxxz, maxy, maxxz).tex(sprite.getMaxU(), sprite.getMinV()).endVertex();
            buffer.pos(minxz, maxy, maxxz).tex(sprite.getMinU(), sprite.getMinV()).endVertex();
            buffer.pos(minxz, 0,    maxxz).tex(sprite.getMinU(), sprite.getMaxV()).endVertex();
            //East
            buffer.pos(maxxz, 0,    minxz).tex(sprite.getMaxU(), sprite.getMaxV()).endVertex();
            buffer.pos(maxxz, maxy, minxz).tex(sprite.getMaxU(), sprite.getMinV()).endVertex();
            buffer.pos(maxxz, maxy, maxxz).tex(sprite.getMinU(), sprite.getMinV()).endVertex();
            buffer.pos(maxxz, 0,    maxxz).tex(sprite.getMinU(), sprite.getMaxV()).endVertex();
            //West
            buffer.pos(minxz, 0,    maxxz).tex(sprite.getMaxU(), sprite.getMaxV()).endVertex();
            buffer.pos(minxz, maxy, maxxz).tex(sprite.getMaxU(), sprite.getMinV()).endVertex();
            buffer.pos(minxz, maxy, minxz).tex(sprite.getMinU(), sprite.getMinV()).endVertex();
            buffer.pos(minxz, 0,    minxz).tex(sprite.getMinU(), sprite.getMaxV()).endVertex();
            tessellator.draw();
        }
        //@formatter:on

        GlStateManager.enableLighting();

        GlStateManager.popMatrix();
    }

    private void putQuad(VertexBuffer buffer, Vec3D p1, Vec3D p2, Vec3D p3, Vec3D p4, double uMin, double uMax, double vMin, double vMax) {
//        buffer.pos(p1.x, p1.y, p1.z).tex()
    }


    //	@Override
//	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
//
//		GL11.glPushMatrix();
//		GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
//		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
//		GL11.glDepthMask(true);
//		TileEntityRendererDispatcher.instance.renderTileEntityAt(chest, 0.0D, 0.0D, 0.0D, 0.0F);
//		GL11.glPopAttrib();
//		GL11.glPopMatrix();
//	}
//
//	@Override
//	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
//		if(ClientProxy.renderPass == 0)
//		{
//			renderer.renderAllFaces = true;
//			TileCamoChest tile = world.getTileEntity(x, y, z) instanceof TileCamoChest ? (TileCamoChest) world.getTileEntity(x, y, z) : null;
//			if (tile != null && tile.block != 0)
//			{
//				renderer.renderStandardBlock(Block.getBlockById(tile.block), x, y, z);
//			}
//			else renderer.renderStandardBlock(Blocks.planks, x, y, z);
//			renderer.renderAllFaces = false;
//		}
//		else
//		{
//			renderer.renderStandardBlock(ModBlocks.camoChest, x, y, z);
//		}
//		return true;
//	}
//
//	@Override
//	public boolean shouldRender3DInInventory(int modelId) {
//		return true;
//	}
//
//	@Override
//	public int getRenderId() {
//		return TolkienTweaks.proxy.getCammoChestRenderpass();
//	}
}
