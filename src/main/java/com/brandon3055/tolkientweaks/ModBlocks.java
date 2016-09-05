package com.brandon3055.tolkientweaks;

import com.brandon3055.tolkientweaks.blocks.*;
import net.minecraft.block.Block;

/**
 * Created by Brandon on 24/01/2015.
 */
public class ModBlocks {

	public static Block houseBuilder;
	public static Block smoker;
	public static Block camoChest;
	public static Block camoKeystone;
    public static Block milestone;

	public static void init()
	{
		houseBuilder = new HouseBuilder();
		smoker = new Smoker();
		camoChest = new CamoChest();
		camoKeystone = new CamoKeystone();
        milestone = new MileStone();
	}
}
