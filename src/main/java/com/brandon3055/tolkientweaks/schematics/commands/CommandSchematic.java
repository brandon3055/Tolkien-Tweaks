package com.brandon3055.tolkientweaks.schematics.commands;

import com.brandon3055.tolkientweaks.utills.LogHelper;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brandon on 21/02/2015.
 */
public class CommandSchematic implements ICommand {

	private List aliases;
	public CommandSchematic()
	{
		this.aliases = new ArrayList();
		this.aliases.add("ttsch");
	}

	@Override
	public String getCommandName()
	{
		return "tt-schematic";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender)
	{
		return "/tt-schematic <function>";
	}

	@Override
	public List getCommandAliases()
	{
		return this.aliases;
	}

	@Override
	public void processCommand(ICommandSender player, String[] args)
	{

		if(args.length == 0)
		{
			player.addChatMessage(new ChatComponentText("Usage: /tt-schematic <function>"));
			player.addChatMessage(new ChatComponentText("/tt-schematic tutorial [Video Tutorial]"));
			player.addChatMessage(new ChatComponentText("/tt-schematic create <name> {-o} [Creates a schematic with given name. -o will make it overwrite an existing schematic with that name if one exists]"));
			player.addChatMessage(new ChatComponentText("/tt-schematic delete <name> [Deletes the specified schematic]"));
			player.addChatMessage(new ChatComponentText("/tt-schematic list [Lists all schematics]"));
			player.addChatMessage(new ChatComponentText("/tt-schematic paste <name> {-i} [Past the specified schematic at your coords. -i will make it ignore air]"));
			player.addChatMessage(new ChatComponentText("/tt-schematic block <function> [Used to interact with the structure builder block you are looking at]"));
			return;
		}

		if (args[0].equals("tutorial"))
		{
			if (args.length == 1)
			{
				player.addChatMessage(new ChatComponentText(EnumChatFormatting.BOLD + "Note this will open the following web page"));
				player.addChatMessage(new ChatComponentText(EnumChatFormatting.BLUE + "http://youtu.be/3vpUF5-qJK8"));
				player.addChatMessage(new ChatComponentText("To open the page run: /tt-schematic tutorial confirm"));
			}
			else if (args.length == 2 && args[1].equals("confirm"))
			{
				try
				{
					Class oclass = Class.forName("java.awt.Desktop");
					Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object) null);
					oclass.getMethod("browse", new Class[]{URI.class}).invoke(object, new URI("http://youtu.be/3vpUF5-qJK8"));
				}
				catch (Throwable throwable)
				{
					LogHelper.error("Couldn\'t open link " + throwable);
				}
			}
		}
		else if (args[0].equals("create"))
		{


		}
		else if (args[0].equals("delete"))
		{


		}
		else if (args[0].equals("list"))
		{

		}
		else if (args[0].equals("paste"))
		{

		}
		else if (args[0].equals("block"))
		{

		}



	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender icommandsender)
	{
		return icommandsender instanceof EntityPlayer && ((EntityPlayer) icommandsender).capabilities.isCreativeMode;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List addTabCompletionOptions(ICommandSender icommandsender, String[] astring)
	{
		List list = new ArrayList();
		if (astring.length == 1)
		{
			list.add("tutorial");
			list.add("create");
			list.add("delete");
			list.add("list");
			list.add("paste");
			list.add("block");
		}

		return list;
	}

	@Override
	public boolean isUsernameIndex(String[] astring, int i)
	{
		return false;
	}

	@Override
	public int compareTo(Object o)
	{
		return 0;
	}


}
