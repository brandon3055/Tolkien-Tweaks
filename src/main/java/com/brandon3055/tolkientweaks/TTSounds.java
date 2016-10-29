package com.brandon3055.tolkientweaks;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

/**
 * Created by brandon3055 on 28/10/2016.
 */
public class TTSounds {
    public static final SoundEvent hypeHorn;
    public static final SoundEvent ringDestroy;

    static {
        hypeHorn = getRegisteredSoundEvent("tolkientweaks:hype_horn");
        ringDestroy = getRegisteredSoundEvent("tolkientweaks:ringdestroy");
    }

    private static SoundEvent getRegisteredSoundEvent(String id) {
        return new SoundEvent(new ResourceLocation(id));
    }
}
