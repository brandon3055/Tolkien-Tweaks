package com.brandon3055.tolkientweaks.tileentity;

import net.minecraft.block.state.IBlockState;

/**
 * Created by brandon3055 on 28/10/2016.
 */
public interface IChameleonStateProvider {

    /**
     * @return the block state of the block who's model this tile is emulating.
     */
    IBlockState getChameleonBlockState();

}
