package com.brandon3055.tolkientweaks.blocks;


import codechicken.lib.model.bakery.PlanarFaceBakery;
import com.brandon3055.tolkientweaks.client.rendering.TTTextureCache;
import com.brandon3055.tolkientweaks.tileentity.TileKeyStone;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
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

    @SideOnly(Side.CLIENT)
    @Override
    public List<BakedQuad> getQuadOverrides(@Nullable IBlockState state, @Nullable EnumFacing side) {
        if (state instanceof IExtendedBlockState && ((IExtendedBlockState)state).getValue(ChameleonBlock.DISABLE_CAMO)) {
            if (side == null) {
                return ImmutableList.of();
            }
            else if (side == EnumFacing.DOWN || side == EnumFacing.UP) {
                return Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(Blocks.DISPENSER.getDefaultState()).getQuads(Blocks.DISPENSER.getDefaultState(), side, 0);
            }
            else {
                return Collections.singletonList(PlanarFaceBakery.bakeFace(side, ((IExtendedBlockState)state).getValue(ChameleonBlock.RANDOM_BOOL) ? TTTextureCache.keystoneFaceActive : TTTextureCache.keystoneFace));
            }
        }

        return null;
    }
}
