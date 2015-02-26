package com.brandon3055.tolkientweaks.blocks;

import com.brandon3055.tolkientweaks.ModItems;
import com.brandon3055.tolkientweaks.TolkienTweaks;
import com.brandon3055.tolkientweaks.items.HouseBuilderItemBlock;
import com.brandon3055.tolkientweaks.schematics.HobbitHole;
import com.brandon3055.tolkientweaks.schematics.LargeHouse;
import com.brandon3055.tolkientweaks.schematics.MediumHouse;
import com.brandon3055.tolkientweaks.schematics.SmallHouse;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Brandon on 24/01/2015.
 */
public class HouseBuilder extends Block {
	public HouseBuilder() {
		super(Material.rock);
		this.setBlockTextureName(TolkienTweaks.RPREFIX + "houseBuilder");
		this.setBlockName(TolkienTweaks.RPREFIX + "houseBuilder");
		this.setHardness(10F);
		this.setResistance(100F);

		GameRegistry.registerBlock(this, HouseBuilderItemBlock.class, "houseBuilder");
	}

	@SuppressWarnings("unchecked")
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubBlocks(Item item, CreativeTabs p_149666_2_, List list) {
		list.add(new ItemStack(item, 1, 0));
		list.add(new ItemStack(item, 1, 1));
		list.add(new ItemStack(item, 1, 2));
		list.add(new ItemStack(item, 1, 3));

		list.add(new ItemStack(item, 1, 4));
		list.add(new ItemStack(item, 1, 5));
		list.add(new ItemStack(item, 1, 6));
		list.add(new ItemStack(item, 1, 7));

		list.add(new ItemStack(item, 1, 8));
		list.add(new ItemStack(item, 1, 9));
		list.add(new ItemStack(item, 1, 10));
		list.add(new ItemStack(item, 1, 11));

		list.add(new ItemStack(item, 1, 12));
		list.add(new ItemStack(item, 1, 13));
		list.add(new ItemStack(item, 1, 14));
		list.add(new ItemStack(item, 1, 15));
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
		if (player.getHeldItem() != null && player.getHeldItem().getItem() == ModItems.houseKey)
		{
			ItemStack stack = player.getHeldItem();
			if (!stack.hasTagCompound() || !stack.getTagCompound().hasKey("Set"))
			{
				NBTTagCompound compound = new NBTTagCompound();
				compound.setBoolean("Set", true);
				compound.setInteger("X", x);
				compound.setInteger("Y", y);
				compound.setInteger("Z", z);
				stack.setTagCompound(compound);
				if (world.isRemote) player.addChatComponentMessage(new ChatComponentTranslation("msg.tolkienaddon.keySet.txt"));
				return true;
			}

			int keyX = stack.getTagCompound().getInteger("X");
			int keyY = stack.getTagCompound().getInteger("Y");
			int keyZ = stack.getTagCompound().getInteger("Z");

			if (keyX == x && keyY == y && keyZ == z)
			{
				int meta = world.getBlockMetadata(x, y, z);
				world.setBlock(x, y, z, Blocks.standing_sign);
				TileEntitySign tile = world.getTileEntity(x, y, z) instanceof TileEntitySign ? (TileEntitySign) world.getTileEntity(x, y, z) : null;
				if (tile != null)
				{
					tile.signText[0] = EnumChatFormatting.DARK_BLUE + "###############";
					tile.signText[1] = EnumChatFormatting.DARK_RED + "Purchased By";
					tile.signText[2] = EnumChatFormatting.DARK_GREEN + player.getCommandSenderName();
					tile.signText[3] = EnumChatFormatting.DARK_BLUE + "###############";
				}
				switch (meta)
				{
					case 0://M N
						MediumHouse.spawnNorth(world, x-4, y-1, z-9);
						break;
					case 1://M S
						MediumHouse.spawnSouth(world, x-4, y-1, z+1);
						world.setBlockMetadataWithNotify(x, y, z, 8, 2);
						break;
					case 2://M E
						MediumHouse.spawnEast(world, x+1, y-1, z-4);
						world.setBlockMetadataWithNotify(x, y, z, 4, 2);
						break;
					case 3://M W
						MediumHouse.spawnWest(world, x-9, y-1, z-4);
						world.setBlockMetadataWithNotify(x, y, z, 12, 2);
						break;

					case 4://L N
						LargeHouse.spawnNorth(world, x - 6, y - 1, z - 13);
						break;
					case 5://L S
						LargeHouse.spawnSouth(world, x-6, y-1, z+1);
						world.setBlockMetadataWithNotify(x, y, z, 8, 2);
						break;
					case 6://L E
						LargeHouse.spawnEast(world, x+1, y-1, z-6);
						world.setBlockMetadataWithNotify(x, y, z, 4, 2);
						break;
					case 7://L W
						LargeHouse.spawnWest(world, x-13, y-1, z-6);
						world.setBlockMetadataWithNotify(x, y, z, 12, 2);
						break;

					case 8://L N
						SmallHouse.spawnNorth(world, x-4, y-1, z-9);
						break;
					case 9://L S
						SmallHouse.spawnSouth(world, x-4, y - 1, z+1);
						world.setBlockMetadataWithNotify(x, y, z, 8, 2);
						break;
					case 10://L E
						SmallHouse.spawnEast(world, x+1, y-1, z-4);
						world.setBlockMetadataWithNotify(x, y, z, 4, 2);
						break;
					case 11://L W
						SmallHouse.spawnWest(world, x-9, y-1, z-4);
						world.setBlockMetadataWithNotify(x, y, z, 12, 2);
						break;

					case 12://L N
						HobbitHole.spawnNorth(world, x - 16, y - 1, z - 33);
						break;
					case 13://L S
						HobbitHole.spawnSouth(world, x - 16, y - 1, z + 1);
						world.setBlockMetadataWithNotify(x, y, z, 8, 2);
						break;
					case 14://L E
						HobbitHole.spawnEast(world, x + 1, y - 1, z - 16);
						world.setBlockMetadataWithNotify(x, y, z, 4, 2);
						break;
					case 15://L W
						HobbitHole.spawnWest(world, x - 33, y - 1, z - 16);
						world.setBlockMetadataWithNotify(x, y, z, 12, 2);
						break;
				}
				//world.setBlockToAir(x, y, z);
				//player.destroyCurrentEquippedItem();
				return true;
			}
			else if (world.isRemote)
			{
				player.addChatComponentMessage(new ChatComponentTranslation("msg.tolkienaddon.keyWrong.txt"));
			}

		}

		return true;
	}
}
