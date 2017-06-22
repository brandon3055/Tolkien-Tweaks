package com.brandon3055.tolkientweaks.blocks;


import codechicken.lib.model.bakery.PlanarFaceBakery;
import com.brandon3055.tolkientweaks.client.rendering.TTTextureCache;
import com.brandon3055.tolkientweaks.tileentity.TileCamoGlowstone;
import com.brandon3055.tolkientweaks.tileentity.TileChameleon;
import com.brandon3055.tolkientweaks.tileentity.TileFluidSource2;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
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
public class FluidSource2 extends ChameleonBlock<TileFluidSource2> {

    public FluidSource2() {
        super(Material.ROCK);
    }

    @Override
    public TileFluidSource2 createChameleonTile(World worldIn, int meta) {
        return new TileFluidSource2();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        return super.onBlockActivated(world, pos, state, player, hand, heldItem, side, hitX, hitY, hitZ);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileCamoGlowstone) {
            ((TileCamoGlowstone) tile).setChameleonState(Blocks.STONE.getDefaultState());
        }
        super.onBlockPlacedBy(world, pos, state, placer, stack);
    }

    @Nullable
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }

    @Override
    public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState blockState, TileEntity te, ItemStack heldStack) {
        if (te instanceof TileChameleon) {
            IBlockState state = ((TileChameleon) te).getChameleonBlockState();
            spawnAsEntity(world, pos, new ItemStack(state.getBlock()));
        }
    }

    @Override
    public float getBlockHardness(IBlockState blockState, World world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileChameleon) {
            IBlockState state = ((TileChameleon) tile).getChameleonBlockState();
            try {
                return state.getBlockHardness(world, pos);
            }
            catch (Throwable e) {
                return state.getBlock().blockHardness;
            }
        }
        return super.getBlockHardness(blockState, world, pos);
    }

    @Override
    public int getLightValue(IBlockState blockState, IBlockAccess world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileChameleon) {
            IBlockState state = ((TileChameleon) tile).getChameleonBlockState();
            return state.getBlock().getLightValue(state);
        }
        return super.getLightValue(blockState, world, pos);
    }

    @Override
    public boolean canDropFromExplosion(Explosion explosionIn) {
        return false;
    }

    @Override
    public void onBlockExploded(World world, BlockPos pos, Explosion explosion) {
    }

    @Override
    public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
    }

    @SideOnly(Side.CLIENT)
    @Override
    public List<BakedQuad> getQuadOverrides(@Nullable IBlockState state, @Nullable EnumFacing side) {
        if (state instanceof IExtendedBlockState && ((IExtendedBlockState) state).getValue(ChameleonBlock.DISABLE_CAMO)) {
            if (side == null) {
                return ImmutableList.of();
            }
            else if (side == EnumFacing.DOWN || side == EnumFacing.UP) {
                return Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(Blocks.DISPENSER.getDefaultState()).getQuads(Blocks.DISPENSER.getDefaultState(), side, 0);
            }
            else {
                return Collections.singletonList(PlanarFaceBakery.bakeFace(side, ((IExtendedBlockState) state).getValue(ChameleonBlock.RANDOM_BOOL) ? TTTextureCache.keystoneFaceActive : TTTextureCache.keystoneFace));
            }
        }

        return null;
    }
}
