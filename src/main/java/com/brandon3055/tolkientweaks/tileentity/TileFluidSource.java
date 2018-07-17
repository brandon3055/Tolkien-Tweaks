package com.brandon3055.tolkientweaks.tileentity;

import com.brandon3055.brandonscore.blocks.TileBCBase;
import com.brandon3055.brandonscore.lib.IActivatableTile;
import com.brandon3055.brandonscore.lib.IChangeListener;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

/**
 * Created by brandon3055 on 28/10/2016.
 */
public class TileFluidSource extends TileBCBase implements IActivatableTile, IChangeListener {

    private FluidStack fluidStack = null;

    public TileFluidSource() {
    }


    @Override
    public boolean onBlockActivated(IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if (!player.isCreative() || stack.isEmpty() || !(stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null))) {
            return false;
        }

        try {
            IFluidTankProperties props = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null).getTankProperties()[0];
            fluidStack = props.getContents();
            onNeighborChange(pos);
            return true;
        }

        catch (Throwable e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void writeExtraNBT(NBTTagCompound compound) {
        super.writeExtraNBT(compound);
        if (fluidStack != null) {
            NBTTagCompound tag = new NBTTagCompound();
            fluidStack.writeToNBT(tag);
            compound.setTag("Fluid", tag);
        }
    }

    @Override
    public void readExtraNBT(NBTTagCompound compound) {
        super.readExtraNBT(compound);
        if (compound.hasKey("Fluid")) {
            fluidStack = FluidStack.loadFluidStackFromNBT(compound.getCompoundTag("Fluid"));
        }
    }

    @Override
    public void onNeighborChange(BlockPos neighbor) {
        if (fluidStack == null || fluidStack.getFluid().getBlock() == null) {
            return;
        }
        Block block = fluidStack.getFluid().getBlock();
        for (EnumFacing facing : EnumFacing.values()) {
            BlockPos side = pos.offset(facing);
            if (world.isAirBlock(side)) {
                world.setBlockState(side, block.getDefaultState());
                world.neighborChanged(side, block, pos);
            }
        }
    }
}
