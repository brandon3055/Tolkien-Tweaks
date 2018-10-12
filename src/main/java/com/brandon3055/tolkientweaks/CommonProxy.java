package com.brandon3055.tolkientweaks;

import com.brandon3055.tolkientweaks.entity.EntityBackpack;
import com.brandon3055.tolkientweaks.entity.EntityPersistentItem;
import com.brandon3055.tolkientweaks.entity.EntityPalantir;
import com.brandon3055.tolkientweaks.entity.EntityRing;
import com.brandon3055.tolkientweaks.network.PacketMilestone;
import com.brandon3055.tolkientweaks.network.PacketPlaceItem;
import com.brandon3055.tolkientweaks.network.PacketSetKey;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by Brandon on 13/01/2015.
 */
public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		registerEntities();
	}

	public void init() {
		registerListeners();
		initializeNetwork();
	}

	public void registerEntities() {
		EntityRegistry.registerModEntity(new ResourceLocation(TolkienTweaks.MODID, "ring"), EntityRing.class, "Ring", 0, TolkienTweaks.instance, 256, 5, true);
		EntityRegistry.registerModEntity(new ResourceLocation(TolkienTweaks.MODID, "persistant_item"), EntityPersistentItem.class, "PersistantItem", 1, TolkienTweaks.instance, 32, 5, true);
		EntityRegistry.registerModEntity(new ResourceLocation(TolkienTweaks.MODID, "palantir"), EntityPalantir.class, "Palantir", 2, TolkienTweaks.instance, 256, 5, true);
        EntityRegistry.registerModEntity(new ResourceLocation(TolkienTweaks.MODID, "backpack"), EntityBackpack.class, "Backpack", 3, TolkienTweaks.instance, 32, 5, true);
	}

	public void registerListeners() {
		MinecraftForge.EVENT_BUS.register(new ForgeEventHandler());
	}

	public void initializeNetwork() {
		TolkienTweaks.network = NetworkRegistry.INSTANCE.newSimpleChannel(TolkienTweaks.networkChannelName);
        TolkienTweaks.network.registerMessage(PacketMilestone.Handler.class, PacketMilestone.class, 0, Side.SERVER);
        TolkienTweaks.network.registerMessage(PacketMilestone.Handler.class, PacketMilestone.class, 1, Side.CLIENT);
		TolkienTweaks.network.registerMessage(PacketSetKey.Handler.class, PacketSetKey.class, 2, Side.SERVER);
		TolkienTweaks.network.registerMessage(PacketPlaceItem.Handler.class, PacketPlaceItem.class, 3, Side.SERVER);
	}

	public boolean isOp(String paramString) {
		MinecraftServer localMinecraftServer = FMLCommonHandler.instance().getMinecraftServerInstance();
		paramString = paramString.trim();
		for (String str : localMinecraftServer.getPlayerList().getOppedPlayerNames()) {
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
