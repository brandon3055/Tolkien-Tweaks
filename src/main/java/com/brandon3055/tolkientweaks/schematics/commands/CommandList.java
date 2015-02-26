package com.brandon3055.tolkientweaks.schematics.commands;

import com.brandon3055.tolkientweaks.TolkienTweaks;
import com.brandon3055.tolkientweaks.network.PacketClientList;
import com.brandon3055.tolkientweaks.schematics.SchematicHandler;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brandon on 25/02/2015.
 */
public class CommandList implements ISubCommand
{
	public static CommandList instance = new CommandList();

	@Override
	public String getCommandName() {
		return "list";
	}

	@Override
	public void handleCommand(EntityPlayer player, String[] args)
	{
		if (args != null && args.length == 2 && (args[1].equals("client") || args[1].equals("c")) && TolkienTweaks.proxy.isDedicatedServer() && player instanceof EntityPlayerMP)
		{
			TolkienTweaks.network.sendTo(new PacketClientList(), (EntityPlayerMP)player);
			return;
		}
		String names = "";
		for (String s : SchematicHandler.getSchematics())
		{
			names = names + EnumChatFormatting.DARK_PURPLE + s + ", ";
		}
		if (StringUtils.isNullOrEmpty(names)) names = "[404] - No schematics found";

		player.addChatMessage(new ChatComponentText(names));
	}

	@Override
	public List<String> addTabCompletionOptions(ICommandSender paramICommandSender, String[] paramArrayOfString) {
		List<String> l = new ArrayList<String>();
		l.add("client");
		return l;
	}

	@Override
	public boolean canSenderUseCommand(ICommandSender sender) {
		return CommandHandler.checkOpAndNotify(sender);
	}

	@Override
	public String[] helpInfo(EntityPlayer sender) {
		return new String[]
		{
			"Usage: /tt-schematic list [client]",
			"",
			"Lists all schematics",
			"Add \"client\" to display schematics",
			"on your local machine if you are on a server"
		};
	}
}
