package com.brandon3055.tolkientweaks.schematics.commands;

import com.brandon3055.tolkientweaks.ConfigHandler;
import com.brandon3055.tolkientweaks.TolkienTweaks;
import com.brandon3055.tolkientweaks.network.PacketFileTransfer;
import com.brandon3055.tolkientweaks.schematics.SchematicHandler;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

/**
 * Created by Brandon on 25/02/2015.
 */
public class CommandSend implements ISubCommand {

	public static CommandSend instance = new CommandSend();

	@Override
	public String getCommandName() {
		return "uploadtoserver";
	}

	@Override
	public void handleCommand(EntityPlayer player, String[] args) {
		if (args.length == 2 && TolkienTweaks.proxy.isDedicatedServer() && player instanceof EntityPlayerMP)
		{
			if (SchematicHandler.loadCompoundFromFile(args[1]) != null)
			{
				player.addChatComponentMessage(new ChatComponentText(args[1] + " Already exists on the server"));
				return;
			}
			if (!TolkienTweaks.proxy.isTransferInProgress()) TolkienTweaks.network.sendTo(new PacketFileTransfer(args[1], true, ConfigHandler.filePort), (EntityPlayerMP) player);
			else player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.RED + "The server is already receiving a file"));
		}
	}

	@Override
	public List<String> addTabCompletionOptions(ICommandSender paramICommandSender, String[] paramArrayOfString) {
		return null;
	}

	@Override
	public boolean canSenderUseCommand(ICommandSender sender) {
		return CommandHandler.checkOpAndNotify(sender);
	}

	@Override
	public String[] helpInfo(EntityPlayer sender) {
		return new String[]
		{
			"Usage: /tt-schematic uploadtoserver <name>",
			"",
			"Sends the specified schematic from your client",
			"to the server"
		};
	}
}
