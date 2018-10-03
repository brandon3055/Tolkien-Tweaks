package com.brandon3055.tolkientweaks.items;

import com.brandon3055.tolkientweaks.TTSounds;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

/**
 * Created by brandon3055 on 30/9/2015.
 */
public class HypeHorn extends Item {

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		if (!world.isRemote){
			world.playSound(null, player.posX, player.posY, player.posZ, TTSounds.hypeHorn, SoundCategory.PLAYERS, 5F, 0.95F + world.rand.nextFloat() * 0.1F);
		}
		return super.onItemRightClick(world, player, hand);
	}
}
