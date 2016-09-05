package com.brandon3055.tolkientweaks.blocks;

import com.brandon3055.draconicevolution.common.ModItems;
import com.brandon3055.draconicevolution.common.lib.References;
import com.brandon3055.brandonscore.common.utills.ItemNBTHelper;
import com.brandon3055.tolkientweaks.ModBlocks;
import com.brandon3055.tolkientweaks.TolkienTweaks;
import com.brandon3055.tolkientweaks.tileentity.TileKeyStone;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

/**
 * Created by Brandon on 17/6/2015.
 */
public class CamoKeystone extends Block {
	private IIcon blockIcon1;

	public CamoKeystone(){
		super(Material.rock);
		this.setBlockTextureName(References.RESOURCESPREFIX + "key_stone_inactive");
		this.setBlockName(TolkienTweaks.RPREFIX + "camoKeystone");
		this.setHardness(10F);
		this.setResistance(100F);
		this.setBlockUnbreakable();

		GameRegistry.registerBlock(this, CKeyStoneItemBlock.class, "camoKeystone");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {
		blockIcon = iconRegister.registerIcon(References.RESOURCESPREFIX + "key_stone_inactive");
		blockIcon1 = iconRegister.registerIcon(References.RESOURCESPREFIX + "key_stone_active");
	}

	@Override
	public boolean hasTileEntity(int metadata) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return new TileKeyStone();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int meta) {
		return side != 0 && side != 1? blockIcon1 : Blocks.furnace.getIcon(side, meta);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int side) {
		TileKeyStone tile = blockAccess.getTileEntity(x, y, z) instanceof TileKeyStone ? (TileKeyStone) blockAccess.getTileEntity(x, y, z) : null;
		if (tile != null)
		{
			if (tile.show) return side != 0 && side != 1 ? tile.isActivated ? blockIcon1 : blockIcon : Blocks.furnace.getIcon(side, 0);
			else return Block.getBlockById(tile.block).getIcon(blockAccess, x, y, z, side);
		}
		return super.getIcon(blockAccess, x, y, z, side);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
		TileKeyStone tile = world.getTileEntity(x, y, z) instanceof TileKeyStone ? (TileKeyStone) world.getTileEntity(x, y, z) : null;
		if (player.capabilities.isCreativeMode && tile != null && player.getHeldItem() != null && player.getHeldItem().getItem() instanceof ItemBlock && player.getHeldItem().getItem() != Item.getItemFromBlock(ModBlocks.smoker) && player.getHeldItem().getItem() != Item.getItemFromBlock(ModBlocks.camoChest) && player.getHeldItem().getItem() != Item.getItemFromBlock(ModBlocks.camoKeystone))
		{
			Block block = ((ItemBlock) player.getHeldItem().getItem()).field_150939_a;
			if (block.isOpaqueCube() && block.renderAsNormalBlock())
			{
				tile.block = Block.getIdFromBlock(block);
				tile.meta = player.getHeldItem().stackSize;
				world.markBlockForUpdate(x, y, z);
			}
		}else if (tile != null) return tile.onActivated(player.getHeldItem(), player);
		return true;
	}

	@Override
	public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side) {
		return true;
	}

	@Override
	public boolean canProvidePower(){
		return true;
	}

	@Override
	public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int meta)
	{
		TileKeyStone tile = world.getTileEntity(x, y, z) != null && world.getTileEntity(x, y, z) instanceof TileKeyStone ? (TileKeyStone) world.getTileEntity(x, y, z) : null;
		if (tile != null) return tile.isActivated ? 15 : 0;
		return 0;
	}

	@Override
	public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int meta)
	{
		return 0;
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
		TileKeyStone tile = world.getTileEntity(x, y, z) != null && world.getTileEntity(x, y, z) instanceof TileKeyStone ? (TileKeyStone) world.getTileEntity(x, y, z) : null;
		if (tile != null) {
			ItemStack key = new ItemStack(ModItems.key);
			ItemNBTHelper.setInteger(key, "KeyCode", tile.getKeyCode());
			ItemNBTHelper.setInteger(key, "X", x);
			ItemNBTHelper.setInteger(key, "Y", y);
			ItemNBTHelper.setInteger(key, "Z", z);
			return key;
		}
		return null;
	}

	@Override
	public boolean isBlockSolid(IBlockAccess p_149747_1_, int p_149747_2_, int p_149747_3_, int p_149747_4_, int p_149747_5_) {
		return true;
	}

	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
		return true;
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
		return null;
	}

}
