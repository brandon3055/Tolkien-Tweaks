package com.brandon3055.tolkientweaks;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.item.ItemTossEvent;

/**
 * Created by Brandon on 22/01/2015.
 */
public class ForgeEventHandler {

	@SubscribeEvent
	public void itemTossEvent(ItemTossEvent event){
		if (event.entityItem != null && event.entityItem.getEntityItem() != null && event.entityItem.getEntityItem().getItem() == ModItems.ring && event.player != null)
		{
			ItemStack stack = event.entityItem.getEntityItem();
			if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setString("PlayerUUID", event.player.getUniqueID().toString());
		}
	}
}
