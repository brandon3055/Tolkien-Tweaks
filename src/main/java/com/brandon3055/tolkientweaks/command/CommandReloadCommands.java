package com.brandon3055.tolkientweaks.command;

import com.brandon3055.tolkientweaks.ConfigHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

/**
 * Created by brandon3055 on 3/06/2016.
 */
public class CommandReloadCommands extends CommandBase {
    @Override
    public String getName() {
        return "tt_reload_chat_commands";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/tt_reload_chat_commands";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        ConfigHandler.loadChatCommands();
    }
}
