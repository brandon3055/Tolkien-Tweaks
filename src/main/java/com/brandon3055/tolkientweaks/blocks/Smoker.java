package com.brandon3055.tolkientweaks.blocks;

import com.brandon3055.tolkientweaks.TolkienTweaks;
import com.brandon3055.tolkientweaks.tileentity.TileSmoker;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by Brandon on 8/02/2015.
 */
public class Smoker extends Block {
	public Smoker() {
		super(Material.rock);
		this.setBlockTextureName(TolkienTweaks.RPREFIX + "smoker");
		this.setBlockName(TolkienTweaks.RPREFIX + "smoker");
		this.setHardness(10F);
		this.setResistance(100F);

		GameRegistry.registerBlock(this, "smoker");
	}

	@Override
	public boolean hasTileEntity(int metadata) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return new TileSmoker();
	}

	@Override
	public void registerBlockIcons(IIconRegister p_149651_1_) {

	}

	@Override
	public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int p_149673_5_) {
		TileSmoker tile = blockAccess.getTileEntity(x, y, z) instanceof TileSmoker ? (TileSmoker) blockAccess.getTileEntity(x, y, z) : null;
		if (tile != null)
		{
			return Block.getBlockById(tile.block).getIcon(blockAccess, x, y, z, p_149673_5_);
		}
		return super.getIcon(blockAccess, x, y, z, p_149673_5_);
	}


	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
		TileSmoker tile = world.getTileEntity(x, y, z) instanceof TileSmoker ? (TileSmoker) world.getTileEntity(x, y, z) : null;
		if (tile != null && player.getHeldItem() != null && player.getHeldItem().getItem() instanceof ItemBlock)
		{
			Block block = ((ItemBlock) player.getHeldItem().getItem()).field_150939_a;
			if (block.isOpaqueCube() && block.renderAsNormalBlock())
			{
				tile.block = Block.getIdFromBlock(block);
				tile.meta = player.getHeldItem().stackSize;
				world.markBlockForUpdate(x, y, z);
			}
		}
		return true;
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
		return null;
	}
}
