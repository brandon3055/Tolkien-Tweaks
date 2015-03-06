package com.brandon3055.tolkientweaks.client;

import com.brandon3055.tolkientweaks.CommonProxy;
import com.brandon3055.tolkientweaks.ModItems;
import com.brandon3055.tolkientweaks.client.rendering.ItemRingRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created by Brandon on 13/01/2015.
 */
public class ClientProxy extends CommonProxy {

	@Override
	public void registerRendering() {
		MinecraftForgeClient.registerItemRenderer(ModItems.ring, new ItemRingRenderer());

	}

	@Override
	public void registerListeners() {
		super.registerListeners();
		MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
	}


	public boolean isOp(String paramString)
	{
		return Minecraft.getMinecraft().theWorld.getWorldInfo().getGameType().isCreative();
	}

	@Override
	public boolean isDedicatedServer() {
		return false;
	}

	@Override
	public EntityPlayer getClientPlayer() {
		return Minecraft.getMinecraft().thePlayer;
	}
}
