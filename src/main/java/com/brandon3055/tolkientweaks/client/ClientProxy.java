package com.brandon3055.tolkientweaks.client;

import com.brandon3055.tolkientweaks.CommonProxy;
import com.brandon3055.tolkientweaks.ModItems;
import com.brandon3055.tolkientweaks.client.rendering.*;
import com.brandon3055.tolkientweaks.entity.EntityBackpack;
import com.brandon3055.tolkientweaks.tileentity.TileMilestone;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created by Brandon on 13/01/2015.
 */
public class ClientProxy extends CommonProxy {

	public static int renderPass;
	private static int cammoChestRenderpass;

	@Override
	public void registerRendering() {
		MinecraftForgeClient.registerItemRenderer(ModItems.ring, new ItemRingRenderer());
		cammoChestRenderpass = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(cammoChestRenderpass, new RenderCamoChest());
		MinecraftForgeClient.registerItemRenderer(ModItems.palantir, new RenderPalantir());
        RenderingRegistry.registerEntityRenderingHandler(EntityBackpack.class, new RenderEntityBackpack());

        ClientRegistry.bindTileEntitySpecialRenderer(TileMilestone.class, new RenderTileMilestone());
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

	@Override
	public int getCammoChestRenderpass() {
		return cammoChestRenderpass;
	}
}
