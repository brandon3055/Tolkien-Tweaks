package com.brandon3055.tolkientweaks.blocks;

import com.brandon3055.brandonscore.blocks.BlockBCore;
import com.brandon3055.brandonscore.config.Feature;
import com.brandon3055.brandonscore.config.ICustomRender;
import com.brandon3055.tolkientweaks.TolkienTweaks;
import com.brandon3055.tolkientweaks.client.gui.GuiHandler;
import com.brandon3055.tolkientweaks.client.rendering.RenderTileCamoChest;
import com.brandon3055.tolkientweaks.tileentity.TileCamoChest;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

/**
 * Created by Brandon on 8/03/2015.
 */
public class CamoChest extends BlockBCore implements ITileEntityProvider, ICustomRender {
//	public static IIcon icon_side;

    private AxisAlignedBB AABB = new AxisAlignedBB(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);

    public CamoChest() {
        this.setHardness(2.5F);
        this.setResistance(5F);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB;
    }

    @Override
    public boolean uberIsBlockFullCube() {
        return false;
    }

//	@Override
//	public void registerBlockIcons(IIconRegister iconRegister) {
//		blockIcon = iconRegister.registerIcon(TolkienTweaks.RPREFIX + "camoChestTop");
//		icon_side = iconRegister.registerIcon(TolkienTweaks.RPREFIX + "camoChestSide");
//	}

//	@Override
//	public int getRenderType() {
//		return TolkienTweaks.proxy.getCammoChestRenderpass();
//	}

//	@Override
//	public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int side) {
////		TileCamoChest tile = blockAccess.getTileEntity(x, y, z) instanceof TileCamoChest ? (TileCamoChest) blockAccess.getTileEntity(x, y, z) : null;
////		if (tile != null)
////		{
////			return Block.getBlockById(tile.block).getIcon(blockAccess, x, y, z, p_149673_5_);
////		}
//		if (side == 0 || side == 1) return blockIcon;
//		return icon_side;
//		//return super.getIcon(blockAccess, x, y, z, p_149673_5_);
//	}

//	@Override
//	public boolean canRenderInPass(int pass) {
//		ClientProxy.renderPass = pass;
//		return true;
//	}
//
//	@Override
//	public int getRenderBlockPass() {
//		return 1;
//	}


    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileCamoChest) {
            if (player.isCreative() && ((TileCamoChest) tile).attemptSetFromStack(heldItem)) {
                return true;
            }
            else if (!world.isRemote) {
                player.openGui(TolkienTweaks.instance, GuiHandler.ID_CAMO_CHEST, world, pos.getX(), pos.getY(), pos.getZ());
            }
        }

        return true;
    }


    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileCamoChest();
    }

//	@Override
//	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
//		if (!world.isRemote) player.openGui(TolkienTweaks.instance, GuiHandler.ID_CAMO_CHEST, world, x, y, z);
//		return true;
//	}

//
//    @Override
//    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
//        super.breakBlock(worldIn, pos, state);
//    }
//
//    @Override
//    public void breakBlock(World world, int x, int y, int z, Block p_149749_5_, int p_149749_6_) {
//        IInventory tile = world.getTileEntity(x, y, z) instanceof IInventory ? (IInventory) world.getTileEntity(x, y, z) : null;
//
//        if (tile != null) {
//            for (int i = 0; i < tile.getSizeInventory(); i++) {
//                if (tile.getStackInSlot(i) != null) {
//                    EntityItem item = new EntityItem(world, x + 0.5, y + 0.5, z + 0.5, tile.getStackInSlot(i));
//                    item.motionX = (world.rand.nextFloat() - 0.5F) * 0.1f;
//                    item.motionY = (world.rand.nextFloat() - 0.5F) * 0.1f;
//                    item.motionZ = (world.rand.nextFloat() - 0.5F) * 0.1f;
//                    world.spawnEntityInWorld(item);
//                    tile.setInventorySlotContents(i, null);
//                }
//            }
//        }
//
//        super.breakBlock(world, x, y, z, p_149749_5_, p_149749_6_);
//    }


    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerRenderer(Feature feature) {
//        ModelResourceLocation modelLocation = new ModelResourceLocation("tolkientweaks:" + feature.registryName(), "inventory");
//        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, modelLocation);
//        ModelRegistryHelper.register(modelLocation, new CCOverrideBakedBlockModel("tolkientweaks:" + feature.registryName()));
        ClientRegistry.bindTileEntitySpecialRenderer(TileCamoChest.class, new RenderTileCamoChest());
    }

    @Override
    public boolean registerNormal(Feature feature) {
        return true;
    }
}
