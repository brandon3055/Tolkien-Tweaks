package com.brandon3055.tolkientweaks.client.gui;

import com.brandon3055.tolkientweaks.container.ContainerCamoChest;
import com.brandon3055.tolkientweaks.tileentity.TileCamoChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

/**
 * Created by Brandon on 8/03/2015.
 */
public class GuiCamoChest extends GuiContainer
{
	private static final ResourceLocation texture = new ResourceLocation("textures/gui/container/generic_54.png");
	private TileCamoChest tile;
	private InventoryPlayer player;

	public GuiCamoChest(TileCamoChest tile, InventoryPlayer player) {
		super(new ContainerCamoChest(tile, player));
		this.tile = tile;
		this.player = player;
		this.ySize = 168;
		this.xSize = 176;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(texture);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, 3 * 18 + 17);
		this.drawTexturedModalRect(k, l + 3 * 18 + 17, 0, 126, this.xSize, 96);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
		this.fontRendererObj.drawString(StatCollector.translateToLocal(this.tile.getInventoryName()), 8, 6, 4210752);
		this.fontRendererObj.drawString(this.player.hasCustomInventoryName() ? this.player.getInventoryName() : I18n.format(this.player.getInventoryName(), new Object[0]), 8, this.ySize - 96 + 2, 4210752);
	}
}
