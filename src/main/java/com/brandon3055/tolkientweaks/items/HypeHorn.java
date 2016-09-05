package com.brandon3055.tolkientweaks.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Created by brandon3055 on 30/9/2015.
 */
public class HypeHorn extends Item {

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		player.playSound("tolkientweaks:hypeHorn", 5F, 0.95F + world.rand.nextFloat() * 0.1F);
		return super.onItemRightClick(stack, world, player);
	}
}
