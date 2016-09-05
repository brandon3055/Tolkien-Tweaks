package com.brandon3055.tolkientweaks.items;

import com.brandon3055.tolkientweaks.entity.EntityPalantir;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Created by Brandon on 13/01/2015.
 */
public class Palantir extends Item {
	public Palantir() {
	}

	@Override
	public void registerIcons(IIconRegister p_94581_1_) {

	}

	@Override
	public boolean hasCustomEntity(ItemStack stack) {
		return true;
	}

	@Override
	public Entity createEntity(World world, Entity location, ItemStack itemstack) {
		return new EntityPalantir(world, location, itemstack);
	}

	@Override
	public void onUpdate(ItemStack stack, World p_77663_2_, Entity p_77663_3_, int p_77663_4_, boolean p_77663_5_) {

		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Glow"))
		{
			if (stack.getTagCompound().getFloat("Glow") > 0f){
				stack.getTagCompound().setFloat("Glow", stack.getTagCompound().getFloat("Glow") - 0.01f);
				stack.getTagCompound().setInteger("ETicks", 0);
			}
			else
			{
				stack.setTagCompound(null);
			}
		}

		super.onUpdate(stack, p_77663_2_, p_77663_3_, p_77663_4_, p_77663_5_);
	}
}
