package com.brandon3055.tolkientweaks.tileentity;

import com.brandon3055.tolkientweaks.TTFeatures;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Brandon on 8/02/2015.
 */
public class TileCamoGlowstone extends TileChameleon {

    public TileCamoGlowstone() {
        super(TTFeatures.camoGlowstone);
    }


    @Override
    public void writeExtraNBT(NBTTagCompound compound) {
        super.writeExtraNBT(compound);
    }

    @Override
    public void readExtraNBT(NBTTagCompound compound) {
        super.readExtraNBT(compound);
    }

    @Override
    public boolean disableCamo() {
        return false;
    }

    @Override
    public boolean randomBool() {
        return false;
    }
}
