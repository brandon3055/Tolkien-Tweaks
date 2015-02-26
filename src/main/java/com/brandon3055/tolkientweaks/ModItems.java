package com.brandon3055.tolkientweaks;

import com.brandon3055.tolkientweaks.items.HouseKey;
import com.brandon3055.tolkientweaks.items.Ring;
import com.brandon3055.tolkientweaks.items.SchematicTool;
import com.brandon3055.tolkientweaks.items.Soul;
import net.minecraft.item.Item;

/**
 * Created by Brandon on 13/01/2015.
 */
public class ModItems {

	public static Item ring;
	public static Item soul;
	public static Item houseKey;
	public static Item schematicTool;

	public static void init(){
		ring = new Ring();
		soul = new Soul();
		houseKey = new HouseKey();
		schematicTool = new SchematicTool();
	}
}
