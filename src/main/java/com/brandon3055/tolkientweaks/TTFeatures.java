package com.brandon3055.tolkientweaks;

import com.brandon3055.brandonscore.config.Feature;
import com.brandon3055.tolkientweaks.blocks.*;
import com.brandon3055.tolkientweaks.items.*;
import com.brandon3055.tolkientweaks.tileentity.*;
import net.minecraft.item.Item;

/**
 * Created by Brandon on 13/01/2015.
 */
public class TTFeatures {

	//region Items

	@Feature(registryName = "key", stateOverride = "tt_items#type=key", variantMap = {"0:type=key", "1:type=key", "2:type=key2", "3:type=copper_key", "4:type=silver_key", "5:type=gold_key"})
	public static Key key = new Key();

	@Feature(registryName = "palantir_shard", stateOverride = "tt_items#type=palantir_shard")												//.setUnlocalizedName(TolkienTweaks.MODID.toLowerCase()+":palantir").setCreativeTab(CreativeTabs.tabMisc).setTextureName(TolkienTweaks.MODID.toLowerCase()+":palantir").setMaxStackSize(1);
	public static IndestructableItem palantirShard = new IndestructableItem();

	@Feature(registryName = "soul", stateOverride = "tt_items#type=soul")
	public static Soul soul = new Soul();

	@Feature(registryName = "east_token", stateOverride = "tt_items#type=east_token")													//.setUnlocalizedName(TolkienTweaks.MODID.toLowerCase() + ":palantirShard").setCreativeTab(CreativeTabs.tabMisc).setTextureName(TolkienTweaks.MODID.toLowerCase() + ":palantirShard");
	public static IndestructableItem eastToken = new IndestructableItem();

	@Feature(registryName = "west_token", stateOverride = "tt_items#type=west_token")															//.setUnlocalizedName(TolkienTweaks.MODID.toLowerCase()+":easternAllianceToken").setCreativeTab(CreativeTabs.tabMisc).setTextureName(TolkienTweaks.MODID.toLowerCase()+":easternAllianceToken");
	public static IndestructableItem westToken = new IndestructableItem();

	@Feature(registryName = "hype_horn", stateOverride = "tt_items#type=hype_horn")													//.setUnlocalizedName(TolkienTweaks.MODID.toLowerCase()+":westernAllianceToken").setCreativeTab(CreativeTabs.tabMisc).setTextureName(TolkienTweaks.MODID.toLowerCase()+":westernAllianceToken");
	public static HypeHorn hypeHorn = new HypeHorn();

	@Feature(registryName = "ring")
	public static Ring ring = new Ring();

	@Feature(registryName = "palantir")
	public static Palantir palantir = new Palantir();										//.setUnlocalizedName(TolkienTweaks.MODID.toLowerCase()+":hypeHorn").setCreativeTab(CreativeTabs.tabMisc).setTextureName(TolkienTweaks.MODID.toLowerCase()+":hypeHorn");

	@Feature(registryName = "brons_coin", stateOverride = "tt_items#type=brons_coin")
	public static Item brons_coin = new Item();

	@Feature(registryName = "silver_coin", stateOverride = "tt_items#type=silver_coin")
	public static Item silver_coin = new Item();

	@Feature(registryName = "gold_coin", stateOverride = "tt_items#type=gold_coin")
	public static Item gold_coin = new Item();


	@Feature(registryName = "key_chain", stateOverride = "tt_items#type=key_chain")
	public static KeyChain keyChain = new KeyChain();

	@Feature(registryName = "coin_pouch", stateOverride = "tt_items#type=coin_pouch")
	public static CoinPouch coinPouch = new CoinPouch();

	//endregion

	//region Blocks

	@Feature(registryName = "smoker", tileEntity = TileSmoker.class)													//.setUnlocalizedName(TolkienTweaks.MODID.toLowerCase()+":westernAllianceToken").setCreativeTab(CreativeTabs.tabMisc).setTextureName(TolkienTweaks.MODID.toLowerCase()+":westernAllianceToken");
	public static Smoker smoker = new Smoker();

	@Feature(registryName = "camo_chest", tileEntity = TileCamoChest.class)
	public static CamoChest camoChest = new CamoChest();

	@Feature(registryName = "camo_keystone", tileEntity = TileKeyStone.class)
	public static CamoKeystone camoKeystone = new CamoKeystone();

	@Feature(registryName = "milestone", tileEntity = TileMilestone.class)
	public static MileStone milestone = new MileStone();

	@Feature(registryName = "lockable_chest", tileEntity = TileLockableChest.class)
	public static LockableChest lockableChest = new LockableChest();

	@Feature(registryName = "camo_glowstone", tileEntity = TileCamoGlowstone.class)
	public static CamoGlowstone camoGlowstone = new CamoGlowstone();

	@Feature(registryName = "fluid_source", tileEntity = TileFluidSource2.class)
	public static FluidSource2 fluidSource = new FluidSource2();

	//endregion
}
