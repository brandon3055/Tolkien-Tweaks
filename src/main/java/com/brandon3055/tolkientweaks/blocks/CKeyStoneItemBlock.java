package com.brandon3055.tolkientweaks.blocks;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Brandon on 27/08/2014.
 */
public class CKeyStoneItemBlock extends ItemBlock {

	public CKeyStoneItemBlock(Block block) {
		super(block);
		setHasSubtypes(true);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (isInCreativeTab(tab)) {
			items.add(new ItemStack(this, 1, 0));
			items.add(new ItemStack(this, 1, 1));
			items.add(new ItemStack(this, 1, 2));
			items.add(new ItemStack(this, 1, 3));
		}
	}

	@Override
	public int getMetadata(int par1) {
		return par1;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> list, ITooltipFlag flagIn) {
		int meta = stack.getItemDamage();
		switch (meta){
			case 0:
				list.add("Permanent Activation (Consume Key)");
				break;
			case 1:
				list.add("Button Activation");
				break;
			case 2:
				list.add("Toggle Activation");
				break;
			case 3:
				list.add("Button Activation (Consume Key)");
				break;
		}
	}
}
