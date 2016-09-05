package com.brandon3055.tolkientweaks.command;

import com.brandon3055.tolkientweaks.tileentity.TileMilestone;
import com.brandon3055.tolkientweaks.utills.TTWorldData;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by brandon3055 on 3/06/2016.
 */
public class CommandMilestone extends CommandBase {
    @Override
    public String getCommandName() {
        return "milestone";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return "/milestone [player]";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!TTWorldData.getMap(sender.getEntityWorld()).containsKey(sender.getCommandSenderName())){
            throw new CommandException("No milestone set!");
        }

        EntityPlayer target = args.length == 1 ? getPlayer(sender, args[0]) : getCommandSenderAsPlayer(sender);

        TTWorldData.MilestoneMarker marker = TTWorldData.getMap(sender.getEntityWorld()).get(sender.getCommandSenderName());

        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        World world = server.worldServerForDimension(marker.dimension);

        if (world == null){
            throw new CommandException("Dimension %s not found!", marker.dimension);
        }

        TileEntity tile = world.getTileEntity(marker.x, marker.y, marker.z);

        if (!(tile instanceof TileMilestone)){
            throw new CommandException("Did not find your bound milestone...");
        }

        ((TileMilestone)tile).handleTeleport(target, true);
    }
}
