package com.brandon3055.tolkientweaks;

import com.brandon3055.tolkientweaks.entity.EntityIndestructibleItem;
import com.brandon3055.tolkientweaks.entity.EntityRing;
import com.brandon3055.tolkientweaks.network.PacketClientList;
import com.brandon3055.tolkientweaks.network.PacketFileTransfer;
import com.brandon3055.tolkientweaks.network.PacketSchematicClient;
import com.brandon3055.tolkientweaks.schematics.FileReceiver;
import com.brandon3055.tolkientweaks.tileentity.TileSmoker;
import com.brandon3055.tolkientweaks.tileentity.TileStructureBuilder;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetHandlerPlayServer;
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
		GameRegistry.registerTileEntity(TileStructureBuilder.class, TolkienTweaks.RPREFIX + "TileStructureBuilder");
	}

	public void initializeNetwork() {
		TolkienTweaks.network = NetworkRegistry.INSTANCE.newSimpleChannel(TolkienTweaks.networkChannelName);
		TolkienTweaks.network.registerMessage(PacketClientList.Handler.class, PacketClientList.class, 0, Side.CLIENT);
		TolkienTweaks.network.registerMessage(PacketFileTransfer.Handler.class, PacketFileTransfer.class, 1, Side.CLIENT);
		TolkienTweaks.network.registerMessage(PacketFileTransfer.Handler.class, PacketFileTransfer.class, 2, Side.SERVER);
		TolkienTweaks.network.registerMessage(PacketSchematicClient.Handler.class, PacketSchematicClient.class, 3, Side.CLIENT);
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

	public void sendFile(String file, int port)
	{

	}

	public void receiveFile(String fileName, NetHandlerPlayServer netHandler)
	{
		FileReceiver.instance.receiveFile(fileName, netHandler);
	}

	public boolean isTransferInProgress() { return FileReceiver.instance.getTransferInProgress(); }
}
