package com.brandon3055.tolkientweaks.items;

import com.brandon3055.tolkientweaks.entity.EntityPersistentItem;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Created by Brandon on 22/01/2015.
 */
public class Soul extends Item {
	public Soul() {
		this.setMaxStackSize(1);
	}

//	@Override
//	public void registerIcons(IIconRegister iconRegister) {
//		itemIcon = iconRegister.registerIcon(TolkienTweaks.RPREFIX + "soul");
//	}



	@Override
	public boolean hasEffect(ItemStack stack) {
		return true;
	}

	@Override
	public boolean hasCustomEntity(ItemStack stack) {
		return true;
	}

	@Override
	public Entity createEntity(World world, Entity location, ItemStack itemstack) {
		return new EntityPersistentItem(world, location, itemstack);
	}
}
