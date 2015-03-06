package com.brandon3055.tolkientweaks;

import com.brandon3055.tolkientweaks.entity.EntityIndestructibleItem;
import com.brandon3055.tolkientweaks.entity.EntityRing;
import com.brandon3055.tolkientweaks.tileentity.TileSmoker;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
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
	}

	public void registerListeners(){
		MinecraftForge.EVENT_BUS.register(new ForgeEventHandler());
	}

	public void registerTileEntities() {
		GameRegistry.registerTileEntity(TileSmoker.class, TolkienTweaks.RPREFIX + "tileSmoker");
	}

	public void initializeNetwork() {
		TolkienTweaks.network = NetworkRegistry.INSTANCE.newSimpleChannel(TolkienTweaks.networkChannelName);

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
}
