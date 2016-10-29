package com.brandon3055.tolkientweaks.blocks;


import com.brandon3055.tolkientweaks.tileentity.TileKeyStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Created by Brandon on 17/6/2015.
 */
public class CamoKeystone extends ChameleonBlock<TileKeyStone> {

    public CamoKeystone() {
        canProvidePower = true;
        this.setHardness(10F);
        this.setResistance(100F);
        this.setBlockUnbreakable();
    }

    @Override
    public TileKeyStone createChameleonTile(World worldIn, int meta) {
        return new TileKeyStone();
    }


    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileKeyStone) {
            if (player.capabilities.isCreativeMode && ((TileKeyStone) tile).attemptSetFromStack(heldItem)) {
                return true;
            }
            else {
                return super.onBlockActivated(world, pos, state, player, hand, heldItem, side, hitX, hitY, hitZ);
            }
        }
        return false;
    }


//    @Override
//    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
//        TileKeyStone tile = world.getTileEntity(x, y, z) != null && world.getTileEntity(x, y, z) instanceof TileKeyStone ? (TileKeyStone) world.getTileEntity(x, y, z) : null;
//        if (tile != null) {
//            ItemStack key = new ItemStack(ModItems.key);
//            ItemNBTHelper.setInteger(key, "KeyCode", tile.getKeyCode());
//            ItemNBTHelper.setInteger(key, "X", x);
//            ItemNBTHelper.setInteger(key, "Y", y);
//            ItemNBTHelper.setInteger(key, "Z", z);
//            return key;
//        }
//        return null;
//    }

    @Override
    public boolean isBlockSolid(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return true;
    }

    @Override
    public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return true;
    }

    @Nullable
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }
}
