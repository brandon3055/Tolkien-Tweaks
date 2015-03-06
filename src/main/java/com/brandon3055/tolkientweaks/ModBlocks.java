package com.brandon3055.tolkientweaks;

import com.brandon3055.tolkientweaks.blocks.HouseBuilder;
import com.brandon3055.tolkientweaks.blocks.Smoker;
import net.minecraft.block.Block;

/**
 * Created by Brandon on 24/01/2015.
 */
public class ModBlocks {

	public static Block houseBuilder;
	public static Block smoker;

	public static void init()
	{
		houseBuilder = new HouseBuilder();
		smoker = new Smoker();
	}
}
