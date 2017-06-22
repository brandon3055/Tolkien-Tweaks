package com.brandon3055.tolkientweaks.blocks;


import com.brandon3055.brandonscore.blocks.BlockBCore;
import com.brandon3055.tolkientweaks.tileentity.TileFluidSource;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Created by Brandon on 17/6/2015.
 */
public class FluidSource extends BlockBCore implements ITileEntityProvider {

    public FluidSource() {
        super(Material.ROCK);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileFluidSource();
    }

    @Nullable
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(Blocks.STONE);
    }

}
