package com.brandon3055.tolkientweaks.client;

import codechicken.lib.texture.TextureUtils;
import com.brandon3055.tolkientweaks.CommonProxy;
import com.brandon3055.tolkientweaks.client.rendering.RenderEntityBackpack;
import com.brandon3055.tolkientweaks.client.rendering.TTTextureCache;
import com.brandon3055.tolkientweaks.entity.EntityBackpack;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by Brandon on 13/01/2015.
 */
public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		registerRendering();
		TextureUtils.addIconRegister(new TTTextureCache());
		KeyInputHandler.init();
	}

	@Override
	public void init() {
		super.init();
	}

	public void registerRendering() {
		RenderingRegistry.registerEntityRenderingHandler(EntityBackpack.class, RenderEntityBackpack::new);
	}

	@Override
	public void registerListeners() {
		super.registerListeners();
		MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
	}

	public boolean isOp(String paramString)
	{
		return Minecraft.getMinecraft().world.getWorldInfo().getGameType().isCreative();
	}

	@Override
	public boolean isDedicatedServer() {
		return false;
	}

	@Override
	public EntityPlayer getClientPlayer() {
		return Minecraft.getMinecraft().player;
	}
}
