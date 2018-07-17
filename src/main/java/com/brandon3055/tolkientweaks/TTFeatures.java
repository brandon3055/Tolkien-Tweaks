package com.brandon3055.tolkientweaks;

import com.brandon3055.brandonscore.registry.Feature;
import com.brandon3055.brandonscore.registry.IModFeatures;
import com.brandon3055.brandonscore.registry.ModFeature;
import com.brandon3055.brandonscore.registry.ModFeatures;
import com.brandon3055.tolkientweaks.blocks.*;
import com.brandon3055.tolkientweaks.items.*;
import com.brandon3055.tolkientweaks.tileentity.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;

/**
 * Created by Brandon on 13/01/2015.
 */
@GameRegistry.ObjectHolder(TolkienTweaks.MODID)
@ModFeatures(modid = TolkienTweaks.MODID)
public class TTFeatures implements IModFeatures {

    @Nullable
    @Override
    public CreativeTabs getCreativeTab(Feature feature) {
        return CreativeTabs.MISC;
    }

    //region Items

    @ModFeature(name = "key", stateOverride = "tt_items", variantMap = {"0:type=key", "1:type=key", "2:type=key2", "3:type=copper_key", "4:type=silver_key", "5:type=gold_key"})
    public static Key key = new Key();

    @ModFeature(name = "palantir_shard", stateOverride = "tt_items#type=palantir_shard")
    //.setUnlocalizedName(TolkienTweaks.MODID.toLowerCase()+":palantir").setCreativeTab(CreativeTabs.tabMisc).setTextureName(TolkienTweaks.MODID.toLowerCase()+":palantir").setMaxStackSize(1);
    public static IndestructableItem palantirShard = new IndestructableItem();

    @ModFeature(name = "soul", stateOverride = "tt_items#type=soul")
    public static Soul soul = new Soul();

    @ModFeature(name = "east_token", stateOverride = "tt_items#type=east_token")
    //.setUnlocalizedName(TolkienTweaks.MODID.toLowerCase() + ":palantirShard").setCreativeTab(CreativeTabs.tabMisc).setTextureName(TolkienTweaks.MODID.toLowerCase() + ":palantirShard");
    public static IndestructableItem eastToken = new IndestructableItem();

    @ModFeature(name = "west_token", stateOverride = "tt_items#type=west_token")
    //.setUnlocalizedName(TolkienTweaks.MODID.toLowerCase()+":easternAllianceToken").setCreativeTab(CreativeTabs.tabMisc).setTextureName(TolkienTweaks.MODID.toLowerCase()+":easternAllianceToken");
    public static IndestructableItem westToken = new IndestructableItem();

    @ModFeature(name = "hype_horn", stateOverride = "tt_items#type=hype_horn")
    //.setUnlocalizedName(TolkienTweaks.MODID.toLowerCase()+":westernAllianceToken").setCreativeTab(CreativeTabs.tabMisc).setTextureName(TolkienTweaks.MODID.toLowerCase()+":westernAllianceToken");
    public static HypeHorn hypeHorn = new HypeHorn();

    @ModFeature(name = "ring")
    public static Ring ring = new Ring();

    @ModFeature(name = "palantir")
    public static Palantir palantir = new Palantir();                                        //.setUnlocalizedName(TolkienTweaks.MODID.toLowerCase()+":hypeHorn").setCreativeTab(CreativeTabs.tabMisc).setTextureName(TolkienTweaks.MODID.toLowerCase()+":hypeHorn");

    @ModFeature(name = "brons_coin", stateOverride = "tt_items#type=brons_coin")
    public static Coin brons_coin = new Coin();

    @ModFeature(name = "silver_coin", stateOverride = "tt_items#type=silver_coin")
    public static Coin silver_coin = new Coin();

    @ModFeature(name = "gold_coin", stateOverride = "tt_items#type=gold_coin")
    public static Coin gold_coin = new Coin();


    @ModFeature(name = "key_chain", stateOverride = "tt_items#type=key_chain")
    public static KeyChain keyChain = new KeyChain();

    @ModFeature(name = "coin_pouch", stateOverride = "tt_items#type=coin_pouch")
    public static CoinPouch coinPouch = new CoinPouch();

    //endregion

    //region Blocks

    @ModFeature(name = "smoker", tileEntity = TileSmoker.class)
    //.setUnlocalizedName(TolkienTweaks.MODID.toLowerCase()+":westernAllianceToken").setCreativeTab(CreativeTabs.tabMisc).setTextureName(TolkienTweaks.MODID.toLowerCase()+":westernAllianceToken");
    public static Smoker smoker = new Smoker();

    @ModFeature(name = "camo_chest", tileEntity = TileCamoChest.class)
    public static CamoChest camoChest = new CamoChest();

    @ModFeature(name = "camo_keystone", tileEntity = TileKeyStone.class)
    public static CamoKeystone camoKeystone = new CamoKeystone();

    @ModFeature(name = "milestone", tileEntity = TileMilestone.class)
    public static MileStone milestone = new MileStone();

    @ModFeature(name = "lockable_chest", tileEntity = TileLockableChest.class)
    public static LockableChest lockableChest = new LockableChest();

    @ModFeature(name = "camo_glowstone", tileEntity = TileCamoGlowstone.class)
    public static CamoGlowstone camoGlowstone = new CamoGlowstone();

    @ModFeature(name = "fluid_source", tileEntity = TileFluidSource2.class)
    public static FluidSource2 fluidSource = new FluidSource2();

    //endregion
}
