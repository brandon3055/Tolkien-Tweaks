package com.brandon3055.tolkientweaks.items;

import com.brandon3055.tolkientweaks.TolkienTweaks;
import com.brandon3055.tolkientweaks.entity.EntityIndestructibleItem;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Created by Brandon on 22/01/2015.
 */
public class Soul extends Item {
	public Soul() {
		this.setUnlocalizedName(TolkienTweaks.RPREFIX + "soul");
		this.setMaxStackSize(1);
		GameRegistry.registerItem(this, "soul");
	}

	@Override
	public void registerIcons(IIconRegister iconRegister) {
		itemIcon = iconRegister.registerIcon(TolkienTweaks.RPREFIX + "soul");
	}

	@Override
	public boolean hasEffect(ItemStack par1ItemStack, int pass) {
		return true;
	}

	@Override
	public boolean hasCustomEntity(ItemStack stack) {
		return true;
	}

	@Override
	public Entity createEntity(World world, Entity location, ItemStack itemstack) {
		return new EntityIndestructibleItem(world, location, itemstack);
	}
}
