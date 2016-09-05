package com.brandon3055.tolkientweaks.client.gui;

import com.brandon3055.tolkientweaks.TolkienTweaks;
import com.brandon3055.tolkientweaks.container.ContainerCamoChest;
import com.brandon3055.tolkientweaks.tileentity.TileCamoChest;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Created by Brandon on 8/03/2015.
 */
public class GuiHandler implements IGuiHandler {

	public static final int ID_CAMO_CHEST = 0;

	public GuiHandler() {
		NetworkRegistry.INSTANCE.registerGuiHandler(TolkienTweaks.instance, this);
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID)
		{
			case ID_CAMO_CHEST:
			{
				TileCamoChest tile = world.getTileEntity(x, y, z) instanceof TileCamoChest ? (TileCamoChest) world.getTileEntity(x, y, z) : null;
				if (tile != null) return new ContainerCamoChest(tile, player.inventory);
			}
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID)
		{
			case ID_CAMO_CHEST:
			{
				TileCamoChest tile = world.getTileEntity(x, y, z) instanceof TileCamoChest ? (TileCamoChest) world.getTileEntity(x, y, z) : null;
				if (tile != null) return new GuiCamoChest(tile, player.inventory);
			}
		}
		return null;
	}
}
