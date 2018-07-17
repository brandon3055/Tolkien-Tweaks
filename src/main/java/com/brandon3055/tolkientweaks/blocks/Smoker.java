package com.brandon3055.tolkientweaks.blocks;

import codechicken.lib.colour.EnumColour;
import com.brandon3055.tolkientweaks.tileentity.TileSmoker;
import com.brandon3055.tolkientweaks.tileentity.TileSmokerClient;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Created by Brandon on 8/02/2015.
 */
public class Smoker extends ChameleonBlock<TileSmoker> {

    public Smoker() {
        super();
        this.setHardness(10F);
        this.setResistance(100F);
    }

    @Override
    public TileSmoker createChameleonTile(World worldIn, int meta) {
        return worldIn.isRemote ? new TileSmokerClient() : new TileSmoker();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            return true;
        }

        TileEntity tile = world.getTileEntity(pos);

        ItemStack heldItem = player.getHeldItem(hand);
        if (tile instanceof TileSmoker && !heldItem.isEmpty()) {
            for (int id : OreDictionary.getOreIDs(heldItem)) {
                String oreName = OreDictionary.getOreName(id);
                if (oreName.startsWith("dye") && oreName.length() > 3) {
                    for (EnumColour colour : EnumColour.values()) {
                        if (oreName.equals(colour.getDyeOreName())) {
                            ((TileSmoker) tile).colour.value = (byte) colour.ordinal();
                            ((TileSmoker) tile).updateBlock();
                            ((TileSmoker) tile).getDataManager().detectAndSendChanges();
                            return true;
                        }
                    }
                }
            }

            if (player.capabilities.isCreativeMode) {
                return ((TileSmoker) tile).attemptSetFromStack(heldItem);
            }
        }

        return false;
    }

    @Nullable
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }
}
