package com.brandon3055.tolkientweaks.client;

import codechicken.lib.model.loader.CCBakedModelLoader;
import com.brandon3055.tolkientweaks.CommonProxy;
import com.brandon3055.tolkientweaks.TolkienTweaks;
import com.brandon3055.tolkientweaks.client.rendering.RenderEntityBackpack;
import com.brandon3055.tolkientweaks.entity.EntityBackpack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by Brandon on 13/01/2015.
 */
public class ClientProxy extends CommonProxy {

//	public static int renderPass;
//	private static int cammoChestRenderpass;


	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		registerRendering();
		TolkienTweaks.featureParser.registerRendering();
	}

	@Override
	public void init() {
		super.init();
	}

	public void registerRendering() {
//		MinecraftForgeClient.registerItemRenderer(ModItems.ring, new ItemRingRenderer());
//		cammoChestRenderpass = RenderingRegistry.getNextAvailableRenderId();
//		RenderingRegistry.registerBlockHandler(cammoChestRenderpass, new RenderCamoChest());
//		MinecraftForgeClient.registerItemRenderer(ModItems.palantir, new RenderPalantir());

		CCBakedModelLoader.registerLoader(TTBakedModelProvider.INSTANCE);
		RenderingRegistry.registerEntityRenderingHandler(EntityBackpack.class, new IRenderFactory<EntityBackpack>() {
			@Override
			public Render<? super EntityBackpack> createRenderFor(RenderManager manager) {
				return new RenderEntityBackpack(manager);
			}
		});

//        ClientRegistry.bindTileEntitySpecialRenderer(TileMilestone.class, new RenderTileMilestone());
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

//	@Override
//	public int getCammoChestRenderpass() {
//		return cammoChestRenderpass;
//	}
}
