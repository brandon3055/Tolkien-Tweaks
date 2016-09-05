package com.brandon3055.tolkientweaks.command;

import com.brandon3055.tolkientweaks.tileentity.TileMilestone;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

/**
 * Created by brandon3055 on 3/06/2016.
 */
public class CommandMilestoneConfig extends CommandBase {
    @Override
    public String getCommandName() {
        return "milestone-config";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return "/milestone-config";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 4;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length != 2){
            help(sender);
            return;
        }

        TileMilestone tile = null;
        EntityPlayer player = getCommandSenderAsPlayer(sender);

        for (int x = (int)player.posX - 5; x < player.posX + 10; x++){
            for (int y = (int)player.posY - 5; y < player.posY + 10; y++){
                for (int z = (int)player.posZ - 5; z < player.posZ + 10; z++){
                    TileEntity tileEntity = sender.getEntityWorld().getTileEntity(x, y, z);
                    if (tileEntity instanceof TileMilestone){
                        tile = (TileMilestone)tileEntity;
                        break;
                    }
                }
                if (tile != null) break;
            }
            if (tile != null) break;
        }

        if (tile == null){
            throw new CommandException("Did not find milestone withing 5 blocks");
        }

        if (args[0].equals("name")){
            tile.markerName = args[1];
            sender.addChatMessage(new ChatComponentText("Name set!").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)));
            tile.getWorldObj().markBlockForUpdate(tile.xCoord, tile.yCoord, tile.zCoord);
        }
        else if (args[0].equals("cooldown")){
            tile.coolDown = Integer.parseInt(args[1]) * 20;
            sender.addChatMessage(new ChatComponentText("Cool Down set!").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)));
            tile.getWorldObj().markBlockForUpdate(tile.xCoord, tile.yCoord, tile.zCoord);
        }
        else {
            help(sender);
        }
    }

    private void help(ICommandSender sender){
        sender.addChatMessage(new ChatComponentText("/milestone"));
        sender.addChatMessage(new ChatComponentText("/milestone name [Name]"));
        sender.addChatMessage(new ChatComponentText("/milestone cooldown [seconds]"));
    }
}
