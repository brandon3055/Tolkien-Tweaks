package com.brandon3055.tolkientweaks;

import com.brandon3055.brandonscore.handlers.FileHandler;
import com.brandon3055.tolkientweaks.utils.LogHelper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.config.Configuration;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.List;

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
	public static File chatCommandConfig;

	public static List<ChatCommand> chatCommands = new LinkedList<>();

	public static void init(Configuration confFile) {
		chatCommandConfig = new File(FileHandler.brandon3055Folder, "TT_Chat_Commands.json");

		if (config == null) {
			config = confFile;
			syncConfig();
		}

		if (!chatCommandConfig.exists()) {
			chatCommandConfig.getParentFile().mkdirs();
			chatCommands.add(new ChatCommand("[some random conditional message]", "/testfor @a[name=<player>,r=10,x=0,z=0]", "/tell <player> Hay! you found my secret conditional command!"));
			chatCommands.add(new ChatCommand("[some random non conditional message]", null, "/tell <player> Hay! you found my secret non-conditional command!"));
			saveChatCommands();
		}
		loadChatCommands();
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


	//Chat Command Config

	public static void saveChatCommands() {
		JsonArray array = new JsonArray();

		chatCommands.forEach(chatCommand -> array.add(chatCommand.save()));

		try {
			JsonWriter writer = new JsonWriter(new FileWriter(chatCommandConfig));
			writer.setIndent("  ");
			Streams.write(array, writer);
			writer.flush();
			IOUtils.closeQuietly(writer);
		}
		catch (Exception e) {
			LogHelper.error("Error saving Chat Command config");
			e.printStackTrace();
		}
	}

	public static void loadChatCommands() {
		JsonArray array;
		try {
			JsonParser parser = new JsonParser();
			FileReader reader = new FileReader(chatCommandConfig);
			JsonElement element = parser.parse(reader);
			IOUtils.closeQuietly(reader);
			if (!element.isJsonObject()) {
				LogHelper.error("Failed to load Chat Command config. Detected invalid config file.");
				return;
			}
			array = element.getAsJsonArray();
			chatCommands.clear();
			for (JsonElement e : array) {
				chatCommands.add(new ChatCommand().load(e.getAsJsonObject()));
			}
		}
		catch (Exception e) {
			LogHelper.error("Error loading Chat Command config");
			e.printStackTrace();
		}
	}

	public static class ChatCommand {

		public String trigger = "";
		public String condition = null;
		public String command = "";

		public ChatCommand() {}

		public ChatCommand(String trigger, String condition, String command) {
			this.trigger = trigger;
			this.condition = condition;
			this.command = command;
		}

		public JsonObject save() {
			JsonObject obj = new JsonObject();
			obj.addProperty("trigger", trigger);
			if (condition != null) {
				obj.addProperty("condition", condition);
			}
			obj.addProperty("command", command);
			return obj;
		}

		public ChatCommand load(JsonObject object) {
			trigger = JsonUtils.getString(object, "trigger", "[ERROR-73486238746328764781648716]").toLowerCase();
			if (trigger.equals("[ERROR-73486238746328764781648716]")) {
				LogHelper.error("Failed to load command Trigger!");
			}
			if (object.has("condition")) {
				condition = JsonUtils.getString(object, "condition", "[ERROR-73486238746328764781648716]");
				if (condition.equals("[ERROR-73486238746328764781648716]")) {
					LogHelper.error("Failed to load command Condition!");
				}
			}
			command = JsonUtils.getString(object, "command", "[ERROR-73486238746328764781648716]");
			if (command.equals("[ERROR-73486238746328764781648716]")) {
				LogHelper.error("Failed to load command Command!");
			}
			return this;
		}

		public void checkRunCommand(EntityPlayerMP player, MinecraftServer server, ICommandSender sender) {
			String test = condition == null ? null : condition.replaceAll("<player>", player.getName());

			if (test == null || server.commandManager.executeCommand(sender, test) > 0) {
				server.commandManager.executeCommand(sender, command.replaceAll("<player>", player.getName()));
			}
		}
	}
}

