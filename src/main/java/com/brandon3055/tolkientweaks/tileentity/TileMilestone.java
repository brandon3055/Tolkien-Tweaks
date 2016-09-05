package com.brandon3055.tolkientweaks.tileentity;

import com.brandon3055.brandonscore.common.utills.Teleporter;
import com.brandon3055.tolkientweaks.ConfigHandler;
import com.brandon3055.tolkientweaks.ForgeEventHandler;
import com.brandon3055.tolkientweaks.utills.TTWorldData;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by brandon3055 on 3/06/2016.
 */
public class TileMilestone extends TileEntity {

    public static Map<String, Integer> switchingCooldowns = new HashMap<String, Integer>();

    public String markerName = "Unnamed...";
    public int coolDown = 0;
    public Map<String, Integer> cooldowns = new HashMap<String, Integer>();
    public double x;
    public double y = -1;
    public double z;
    public List<String> users = new ArrayList<String>();



    public void onActivated(EntityPlayer player){

        TTWorldData.MilestoneMarker current = TTWorldData.getMap(worldObj).get(player.getCommandSenderName());

        if (current != null && current.x == xCoord && current.y == yCoord && current.z == zCoord && current.dimension == worldObj.provider.dimensionId){
            return;
        }

        int time = switchingCooldowns.containsKey(player.getCommandSenderName()) ? (switchingCooldowns.get(player.getCommandSenderName()) + ConfigHandler.milestoneCoolDown * 20) - ForgeEventHandler.tick : 0;

        if (time > 0 && !player.capabilities.isCreativeMode){
            time /= 20;
            String m;
            if (time > 60){
                m = time / 60 + " minute" + (time / 60 > 1 ? "s" : "");
            }
            else {
                m = time + " second" + (time > 1 ? "s" : "");
            }

            player.addChatComponentMessage(new ChatComponentText("You must wait "+m+" before you can switch milestones").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
            return;
        }

        switchingCooldowns.put(player.getCommandSenderName(), ForgeEventHandler.tick);

        TTWorldData.MilestoneMarker marker = TTWorldData.getMap(worldObj).get(player.getCommandSenderName());
        TTWorldData.addMarker(worldObj, player.getCommandSenderName(), xCoord, yCoord, zCoord, worldObj.provider.dimensionId);
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

        if (marker == null){
            return;
        }

        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        World world = server.worldServerForDimension(marker.dimension);

        TileEntity tile = world.getTileEntity(marker.x, marker.y, marker.z);

        if (tile instanceof TileMilestone){
            world.markBlockForUpdate(tile.xCoord, tile.yCoord, tile.zCoord);
        }
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return super.getRenderBoundingBox().expand(0, 1, 0);
    }

    public void handleTeleport(EntityPlayer player, boolean force){
        int time = cooldowns.containsKey(player.getCommandSenderName()) ? (cooldowns.get(player.getCommandSenderName()) + coolDown) - ForgeEventHandler.tick : 0;

        if (time > 0 && !force && !player.capabilities.isCreativeMode){
            player.addChatComponentMessage(new ChatComponentText(String.format("You must wait %s more seconds before using this milestone again", time / 20)));
            return;
        }

        if (y == -1){
            x = xCoord + 0.5;
            y = yCoord + 2;
            z = zCoord + 0.5;
        }

        cooldowns.put(player.getCommandSenderName(), ForgeEventHandler.tick);

        new Teleporter.TeleportLocation(x, y, z, worldObj.provider.dimensionId).sendEntityToCoords(player);
    }

    @Override
    public Packet getDescriptionPacket() {
        if (worldObj != null){
            users.clear();
            Map<String, TTWorldData.MilestoneMarker> markers = TTWorldData.getMap(worldObj);
            for (String name : markers.keySet()){
                TTWorldData.MilestoneMarker marker = markers.get(name);
                if (marker.x == xCoord && marker.y == yCoord && marker.z == zCoord){
                    users.add(name);
                }
            }
        }


        NBTTagCompound compound = new NBTTagCompound();
        NBTTagList list = new NBTTagList();
        for (String user : users){
            list.appendTag(new NBTTagString(user));
        }
        compound.setTag("Users", list);
        compound.setString("Name", markerName);
        compound.setInteger("CoolDown", coolDown);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, compound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        users.clear();
        NBTTagCompound compound = pkt.func_148857_g();
        markerName = compound.getString("Name");
        coolDown = compound.getInteger("CoolDown");
        if (compound.hasKey("Users")){
            NBTTagList list = compound.getTagList("Users", (byte)8);
            for (int i = 0; i < list.tagCount(); i++){
                users.add(list.getStringTagAt(i));
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setString("Name", markerName);
        compound.setInteger("CoolDown", coolDown);
        compound.setDouble("X", x);
        compound.setDouble("Y", y);
        compound.setDouble("Z", z);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        markerName = compound.getString("Name");
        coolDown = compound.getInteger("CoolDown");
        x = compound.getDouble("X");
        y = compound.getDouble("Y");
        z = compound.getDouble("Z");
    }
}
