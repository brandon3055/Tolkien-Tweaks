package com.brandon3055.tolkientweaks;

import com.brandon3055.tolkientweaks.entity.EntityBackpack;
import com.brandon3055.tolkientweaks.entity.EntityIndestructibleItem;
import com.brandon3055.tolkientweaks.entity.EntityPalantir;
import com.brandon3055.tolkientweaks.entity.EntityRing;
import com.brandon3055.tolkientweaks.network.PacketMilestone;
import com.brandon3055.tolkientweaks.tileentity.TileCamoChest;
import com.brandon3055.tolkientweaks.tileentity.TileKeyStone;
import com.brandon3055.tolkientweaks.tileentity.TileMilestone;
import com.brandon3055.tolkientweaks.tileentity.TileSmoker;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created by Brandon on 13/01/2015.
 */
public class CommonProxy {

	public void registerRendering(){

	}

	public void registerEntities(){
		EntityRegistry.registerModEntity(EntityRing.class, "Ring", 0, TolkienTweaks.instance, 32, 5, true);
		EntityRegistry.registerModEntity(EntityIndestructibleItem.class, "PersistantItem", 1, TolkienTweaks.instance, 32, 5, true);
		EntityRegistry.registerModEntity(EntityPalantir.class, "Palantir", 2, TolkienTweaks.instance, 32, 5, true);
        EntityRegistry.registerModEntity(EntityBackpack.class, "Backpack", 3, TolkienTweaks.instance, 32, 5, true);
	}

	public void registerListeners(){
		MinecraftForge.EVENT_BUS.register(new ForgeEventHandler());
        FMLCommonHandler.instance().bus().register(new ForgeEventHandler());
	}

	public void registerTileEntities() {
		GameRegistry.registerTileEntity(TileSmoker.class, TolkienTweaks.RPREFIX + "tileSmoker");
		GameRegistry.registerTileEntity(TileCamoChest.class, TolkienTweaks.RPREFIX + "TileCamoChest");
		GameRegistry.registerTileEntity(TileKeyStone.class, TolkienTweaks.RPREFIX + "TileKeyStone");
        GameRegistry.registerTileEntity(TileMilestone.class, TolkienTweaks.RPREFIX + "TileMilestone");
	}

	public void initializeNetwork() {
		TolkienTweaks.network = NetworkRegistry.INSTANCE.newSimpleChannel(TolkienTweaks.networkChannelName);
        TolkienTweaks.network.registerMessage(PacketMilestone.Handler.class, PacketMilestone.class, 0, Side.SERVER);
        TolkienTweaks.network.registerMessage(PacketMilestone.Handler.class, PacketMilestone.class, 1, Side.CLIENT);
	}

	public boolean isOp(String paramString)
	{
		MinecraftServer localMinecraftServer = FMLCommonHandler.instance().getMinecraftServerInstance();
		paramString = paramString.trim();
		for (String str : localMinecraftServer.getConfigurationManager().func_152606_n()) {
			if (paramString.equalsIgnoreCase(str)) {
				return true;
			}
		}
		return false;
	}

	public boolean isDedicatedServer()
	{
		return true;
	}

	public EntityPlayer getClientPlayer()
	{
		return null;
	}

	public int getCammoChestRenderpass() {
		return -1;
	}
}
