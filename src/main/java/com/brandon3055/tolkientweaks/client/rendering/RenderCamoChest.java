package com.brandon3055.tolkientweaks.client.rendering;

import com.brandon3055.tolkientweaks.ModBlocks;
import com.brandon3055.tolkientweaks.TolkienTweaks;
import com.brandon3055.tolkientweaks.client.ClientProxy;
import com.brandon3055.tolkientweaks.tileentity.TileCamoChest;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

/**
 * Created by Brandon on 8/03/2015.
 */
public class RenderCamoChest implements ISimpleBlockRenderingHandler
{
	private static TileEntityChest chest;

	public RenderCamoChest()
	{
		chest = new TileEntityChest();
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {

		GL11.glPushMatrix();
		GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		GL11.glDepthMask(true);
		TileEntityRendererDispatcher.instance.renderTileEntityAt(chest, 0.0D, 0.0D, 0.0D, 0.0F);
		GL11.glPopAttrib();
		GL11.glPopMatrix();
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		if(ClientProxy.renderPass == 0)
		{
			renderer.renderAllFaces = true;
			TileCamoChest tile = world.getTileEntity(x, y, z) instanceof TileCamoChest ? (TileCamoChest) world.getTileEntity(x, y, z) : null;
			if (tile != null && tile.block != 0)
			{
				renderer.renderStandardBlock(Block.getBlockById(tile.block), x, y, z);
			}
			else renderer.renderStandardBlock(Blocks.planks, x, y, z);
			renderer.renderAllFaces = false;
		}
		else
		{
			renderer.renderStandardBlock(ModBlocks.camoChest, x, y, z);
		}
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

	@Override
	public int getRenderId() {
		return TolkienTweaks.proxy.getCammoChestRenderpass();
	}
}
