package com.brandon3055.tolkientweaks.client.gui;

import com.brandon3055.tolkientweaks.TolkienTweaks;
import com.brandon3055.tolkientweaks.container.ContainerCamoChest;
import com.brandon3055.tolkientweaks.tileentity.TileCamoChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

/**
 * Created by Brandon on 8/03/2015.
 */
public class GuiHandler implements IGuiHandler {

    public static final int ID_CAMO_CHEST = 0;
    public static final int ID_KEYSTONE = 1;

    public GuiHandler() {
        NetworkRegistry.INSTANCE.registerGuiHandler(TolkienTweaks.instance, this);
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity tile = world.getTileEntity(pos);

        switch (ID) {
            case ID_CAMO_CHEST: {
                if (tile instanceof TileCamoChest) {
                    return new ContainerCamoChest((TileCamoChest) tile, player.inventory);
                }
            }
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity tile = world.getTileEntity(pos);

        switch (ID) {
            case ID_CAMO_CHEST: {
                if (tile instanceof TileCamoChest) {
                    return new GuiCamoChest((TileCamoChest) tile, player.inventory);
                }
            }
        }
        return null;
    }
}
