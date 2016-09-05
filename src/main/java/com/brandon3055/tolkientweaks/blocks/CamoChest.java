package com.brandon3055.tolkientweaks.blocks;

import com.brandon3055.tolkientweaks.ModBlocks;
import com.brandon3055.tolkientweaks.TolkienTweaks;
import com.brandon3055.tolkientweaks.client.ClientProxy;
import com.brandon3055.tolkientweaks.client.gui.GuiHandler;
import com.brandon3055.tolkientweaks.tileentity.TileCamoChest;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Created by Brandon on 8/03/2015.
 */
public class CamoChest extends BlockContainer {
	public static IIcon icon_side;

	public CamoChest() {
		super(Material.wood);
		this.setBlockTextureName(TolkienTweaks.RPREFIX + "camoChest");
		this.setBlockName(TolkienTweaks.RPREFIX + "camoChest");
		this.setHardness(2.5F);
		this.setResistance(5F);
		this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);

		GameRegistry.registerBlock(this, "camoChest");
	}

	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {
		blockIcon = iconRegister.registerIcon(TolkienTweaks.RPREFIX + "camoChestTop");
		icon_side = iconRegister.registerIcon(TolkienTweaks.RPREFIX + "camoChestSide");
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess p_149646_1_, int p_149646_2_, int p_149646_3_, int p_149646_4_, int p_149646_5_) {
		return true;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public int getRenderType() {
		return TolkienTweaks.proxy.getCammoChestRenderpass();
	}

	@Override
	public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int side) {
//		TileCamoChest tile = blockAccess.getTileEntity(x, y, z) instanceof TileCamoChest ? (TileCamoChest) blockAccess.getTileEntity(x, y, z) : null;
//		if (tile != null)
//		{
//			return Block.getBlockById(tile.block).getIcon(blockAccess, x, y, z, p_149673_5_);
//		}
		if (side == 0 || side == 1) return blockIcon;
		return icon_side;
		//return super.getIcon(blockAccess, x, y, z, p_149673_5_);
	}

	@Override
	public boolean canRenderInPass(int pass) {
		ClientProxy.renderPass = pass;
		return true;
	}

	@Override
	public int getRenderBlockPass() {
		return 1;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
		TileCamoChest tile = world.getTileEntity(x, y, z) instanceof TileCamoChest ? (TileCamoChest) world.getTileEntity(x, y, z) : null;
		if (player.capabilities.isCreativeMode && tile != null && player.getHeldItem() != null && player.getHeldItem().getItem() instanceof ItemBlock && player.getHeldItem().getItem() != Item.getItemFromBlock(ModBlocks.smoker) && player.getHeldItem().getItem() != Item.getItemFromBlock(ModBlocks.camoChest))
		{
			Block block = ((ItemBlock) player.getHeldItem().getItem()).field_150939_a;
			if (block.isOpaqueCube() && block.renderAsNormalBlock())
			{
				tile.block = Block.getIdFromBlock(block);
				tile.meta = player.getHeldItem().stackSize;
				world.setBlockMetadataWithNotify(x, y, z, player.getHeldItem().getItemDamage(), 2);
				world.markBlockForUpdate(x, y, z);
			}
			return true;
		}
		else if (!world.isRemote) player.openGui(TolkienTweaks.instance, GuiHandler.ID_CAMO_CHEST, world, x, y, z);
		return true;
	}


	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileCamoChest();
	}

//	@Override
//	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
//		if (!world.isRemote) player.openGui(TolkienTweaks.instance, GuiHandler.ID_CAMO_CHEST, world, x, y, z);
//		return true;
//	}


	@Override
	public void breakBlock(World world, int x, int y, int z, Block p_149749_5_, int p_149749_6_) {
		IInventory tile = world.getTileEntity(x, y, z) instanceof IInventory ? (IInventory) world.getTileEntity(x, y, z) : null;

		if (tile != null)
		{
			for (int i = 0; i < tile.getSizeInventory(); i++)
			{
				if (tile.getStackInSlot(i) != null)
				{
					EntityItem item = new EntityItem(world, x + 0.5, y + 0.5, z + 0.5, tile.getStackInSlot(i));
					item.motionX = (world.rand.nextFloat() - 0.5F) * 0.1f;
					item.motionY = (world.rand.nextFloat() - 0.5F) * 0.1f;
					item.motionZ = (world.rand.nextFloat() - 0.5F) * 0.1f;
					world.spawnEntityInWorld(item);
					tile.setInventorySlotContents(i, null);
				}
			}
		}

		super.breakBlock(world, x, y, z, p_149749_5_, p_149749_6_);
	}
}
