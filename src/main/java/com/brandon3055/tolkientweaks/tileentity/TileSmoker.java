package com.brandon3055.tolkientweaks.tileentity;

import com.brandon3055.brandonscore.network.wrappers.SyncableByte;
import com.brandon3055.tolkientweaks.TTFeatures;

/**
 * Created by Brandon on 8/02/2015.
 */
public class TileSmoker extends TileChameleon {

	public final SyncableByte colour = new SyncableByte((byte) 0, true, false, true);

	public TileSmoker() {
		super(TTFeatures.smoker);
		registerSyncableObject(colour, true);
	}
}
