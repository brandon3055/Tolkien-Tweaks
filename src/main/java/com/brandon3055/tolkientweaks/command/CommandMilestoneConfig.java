package com.brandon3055.tolkientweaks.command;

import com.brandon3055.tolkientweaks.tileentity.TileMilestone;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

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
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length != 2){
            help(sender);
            return;
        }

        TileMilestone tile = null;
        EntityPlayer player = getCommandSenderAsPlayer(sender);

        for (int x = (int)player.posX - 5; x < player.posX + 10; x++){
            for (int y = (int)player.posY - 5; y < player.posY + 10; y++){
                for (int z = (int)player.posZ - 5; z < player.posZ + 10; z++){
                    TileEntity tileEntity = sender.getEntityWorld().getTileEntity(new BlockPos(x, y, z));
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
            tile.markerName.value = args[1];
            sender.addChatMessage(new TextComponentString("Name set!").setStyle(new Style().setColor(TextFormatting.GREEN)));
//            tile.getWorldObj().markBlockForUpdate(tile.xCoord, tile.yCoord, tile.zCoord);
            tile.updateBlock();
        }
        else if (args[0].equals("cooldown")){
            tile.coolDown = Integer.parseInt(args[1]) * 20;
            sender.addChatMessage(new TextComponentString("Cool Down set!").setStyle(new Style().setColor(TextFormatting.GREEN)));
//            tile.getWorldObj().markBlockForUpdate(tile.xCoord, tile.yCoord, tile.zCoord);
            tile.updateBlock();
        }
        else {
            help(sender);
        }
    }

    private void help(ICommandSender sender){
        sender.addChatMessage(new TextComponentString("/milestone"));
        sender.addChatMessage(new TextComponentString("/milestone name [Name]"));
        sender.addChatMessage(new TextComponentString("/milestone cooldown [seconds]"));
    }
}
