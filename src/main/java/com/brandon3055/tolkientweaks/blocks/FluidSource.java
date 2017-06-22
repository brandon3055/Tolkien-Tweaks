package com.brandon3055.tolkientweaks.blocks;


import codechicken.lib.model.bakery.PlanarFaceBakery;
import com.brandon3055.tolkientweaks.client.rendering.TTTextureCache;
import com.brandon3055.tolkientweaks.tileentity.TileCamoGlowstone;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
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
public class CamoGlowstone extends ChameleonBlock<TileCamoGlowstone> {

    public CamoGlowstone() {
        super(Material.GLASS);
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return 15;
    }

    @Override
    public TileCamoGlowstone createChameleonTile(World worldIn, int meta) {
        return new TileCamoGlowstone();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileCamoGlowstone) {
            if (player.capabilities.isCreativeMode && ((TileCamoGlowstone) tile).attemptSetFromStack(heldItem)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileCamoGlowstone) {
            ((TileCamoGlowstone) tile).setChameleonState(Blocks.GLOWSTONE.getDefaultState());
        }
        super.onBlockPlacedBy(world, pos, state, placer, stack);
    }

    public int quantityDroppedWithBonus(int fortune, Random random)
    {
        return MathHelper.clamp_int(this.quantityDropped(random) + random.nextInt(fortune + 1), 1, 4);
    }

    public int quantityDropped(Random random)
    {
        return 2 + random.nextInt(3);
    }

    @Nullable
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Items.GLOWSTONE_DUST;
    }

    public MapColor getMapColor(IBlockState state)
    {
        return MapColor.SAND;
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
