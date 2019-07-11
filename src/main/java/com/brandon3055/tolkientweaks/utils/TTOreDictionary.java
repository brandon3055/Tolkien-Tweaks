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
        OreDictionary.registerOre("itemToken", new ItemStack(TTFeatures.eastToken));
        OreDictionary.registerOre("itemToken", new ItemStack(TTFeatures.westToken));
        OreDictionary.registerOre("itemCoin", new ItemStack(TTFeatures.brons_coin));
        OreDictionary.registerOre("itemCoin", new ItemStack(TTFeatures.silver_coin));
        OreDictionary.registerOre("itemCoin", new ItemStack(TTFeatures.gold_coin));
    }
}
