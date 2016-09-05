package com.brandon3055.tolkientweaks;

import com.brandon3055.tolkientweaks.items.*;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * Created by Brandon on 13/01/2015.
 */
public class ModItems {

	public static Item ring;
	public static Item soul;
	public static Item houseKey;
	public static Item palantir = new Palantir().setUnlocalizedName(TolkienTweaks.MODID.toLowerCase()+":palantir").setCreativeTab(CreativeTabs.tabMisc).setTextureName(TolkienTweaks.MODID.toLowerCase()+":palantir").setMaxStackSize(1);
	public static Item palantirShard = new IndestructableItem().setUnlocalizedName(TolkienTweaks.MODID.toLowerCase() + ":palantirShard").setCreativeTab(CreativeTabs.tabMisc).setTextureName(TolkienTweaks.MODID.toLowerCase() + ":palantirShard");
	public static Item easternAllianceToken = new IndestructableItem().setUnlocalizedName(TolkienTweaks.MODID.toLowerCase()+":easternAllianceToken").setCreativeTab(CreativeTabs.tabMisc).setTextureName(TolkienTweaks.MODID.toLowerCase()+":easternAllianceToken");
	public static Item westernAllianceToken = new IndestructableItem().setUnlocalizedName(TolkienTweaks.MODID.toLowerCase()+":westernAllianceToken").setCreativeTab(CreativeTabs.tabMisc).setTextureName(TolkienTweaks.MODID.toLowerCase()+":westernAllianceToken");
	public static Item hypeHorn = new HypeHorn().setUnlocalizedName(TolkienTweaks.MODID.toLowerCase()+":hypeHorn").setCreativeTab(CreativeTabs.tabMisc).setTextureName(TolkienTweaks.MODID.toLowerCase()+":hypeHorn");


	public static void init(){
		ring = new Ring();
		soul = new Soul();
		houseKey = new HouseKey();
		GameRegistry.registerItem(palantir, "palantir");
		GameRegistry.registerItem(palantirShard, "palantirShard");
		GameRegistry.registerItem(easternAllianceToken, "easternAllianceToken");
		GameRegistry.registerItem(westernAllianceToken, "westernAllianceToken");
		GameRegistry.registerItem(hypeHorn, "hypeHorn");
	}
}
