package com.brandon3055.tolkientweaks.tileentity;

import com.brandon3055.brandonscore.lib.datamanager.ManagedByte;
import com.brandon3055.tolkientweaks.TTFeatures;

/**
 * Created by Brandon on 8/02/2015.
 */
public class TileSmoker extends TileChameleon {

	public final ManagedByte colour = register("colour", new ManagedByte((byte) 0)).syncViaTile().trigerUpdate().saveToTile().finish();

	public TileSmoker() {
		super(TTFeatures.smoker);
	}
}
