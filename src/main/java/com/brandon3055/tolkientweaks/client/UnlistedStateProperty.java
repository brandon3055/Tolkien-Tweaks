package com.brandon3055.tolkientweaks.client;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

/**
 * Created by brandon3055 on 28/10/2016.
 */
public class UnlistedStateProperty implements IUnlistedProperty<IBlockState> {

    private String name;

    public UnlistedStateProperty(String name){
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isValid(IBlockState value) {
        return true;
    }

    @Override
    public Class<IBlockState> getType() {
        return IBlockState.class;
    }

    @Override
    public String valueToString(IBlockState value) {
        return value.toString();
    }
}
