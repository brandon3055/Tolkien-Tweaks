package com.brandon3055.tolkientweaks;

import com.brandon3055.tolkientweaks.utils.LogHelper;
import net.minecraftforge.common.config.Configuration;

/**
 * Created by Brandon on 13/01/2015.
 */
public class ConfigHandler {
	public static Configuration config;
    public static int milestoneCoolDown;

	public static int[] ringDisposalLocation;
	public static int ringDisposalRadius;
	public static String wrongLocationMessage;
	public static String wrongLocationMessagePalantir;

	public static void init(Configuration confFile) {
		if (config == null) {
			config = confFile;
			syncConfig();
		}
	}

	public static void syncConfig() {
		try {
			ringDisposalLocation = config.get(Configuration.CATEGORY_GENERAL, "Ring Disposal Location", new int[] {0, 0, 0}, "The centre point of the location where the ring must be destroyed Format:{x, y, z} if left as 0,0,0 the ring can be destroyed any wear").getIntList();
			ringDisposalRadius = config.get(Configuration.CATEGORY_GENERAL, "Ring Disposal Radius", 0, "Sets the size of the area the ring can be destroyed in").getInt();
			wrongLocationMessage = config.get(Configuration.CATEGORY_GENERAL, "Wrong Location Message", "This ring can not be destroyed by ordinary means", "This is the message that will be displayed if the player tries to destroy the ring in the wrong location").getString();
			wrongLocationMessagePalantir = config.get(Configuration.CATEGORY_GENERAL, "Wrong Location Message Palantir", "This item can not be destroyed by ordinary means", "This is the message that will be displayed if the player tries to destroy the Palantir in the wrong location").getString();
		    milestoneCoolDown = config.get(Configuration.CATEGORY_GENERAL, "Switch milestone coolDown (in seconds)", 1800).getInt(1800);
        }
		catch (Exception e) {
			LogHelper.error("Unable to load Config");
			e.printStackTrace();
		}
		finally {
			if (config.hasChanged()) config.save();
		}
	}
}

