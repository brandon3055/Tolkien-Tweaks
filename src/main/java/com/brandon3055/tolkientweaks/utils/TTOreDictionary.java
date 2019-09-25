package com.brandon3055.tolkientweaks.utils;

import com.brandon3055.tolkientweaks.TTFeatures;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class TTOreDictionary {
    public static void init()
    {
        addOreRegistration();
    }

    private static void addOreRegistration(){
        OreDictionary.registerOre("itemTokenEast", new ItemStack(TTFeatures.eastToken));
        OreDictionary.registerOre("itemTokenWest", new ItemStack(TTFeatures.westToken));
        OreDictionary.registerOre("itemCoinBronze", new ItemStack(TTFeatures.brons_coin));
        OreDictionary.registerOre("itemCoinSilver", new ItemStack(TTFeatures.silver_coin));
        OreDictionary.registerOre("itemCoinGold", new ItemStack(TTFeatures.gold_coin));
    }
}
