package com.brandon3055.tolkientweaks.client;

import com.brandon3055.tolkientweaks.ModItems;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.client.event.RenderPlayerEvent;

/**
 * Created by Brandon on 14/01/2015.
 */
@SideOnly(Side.CLIENT)
public class ClientEventHandler {

	@SubscribeEvent
	public void renderPlayerPre(RenderPlayerEvent.Pre event) {
		if (event.entityPlayer.getHeldItem() != null && event.entityPlayer.getHeldItem().getItem() == ModItems.ring)
			event.setCanceled(true);
	}

}
