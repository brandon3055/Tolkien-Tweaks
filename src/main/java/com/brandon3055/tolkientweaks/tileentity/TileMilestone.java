package com.brandon3055.tolkientweaks.tileentity;

import com.brandon3055.brandonscore.blocks.TileBCBase;
import com.brandon3055.brandonscore.lib.TeleportUtils;
import com.brandon3055.brandonscore.lib.Vec3D;
import com.brandon3055.brandonscore.network.wrappers.SyncableString;
import com.brandon3055.brandonscore.network.wrappers.SyncableVec3D;
import com.brandon3055.tolkientweaks.ConfigHandler;
import com.brandon3055.tolkientweaks.ForgeEventHandler;
import com.brandon3055.tolkientweaks.utils.LogHelper;
import com.brandon3055.tolkientweaks.utils.TTWorldData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by brandon3055 on 3/06/2016.
 */
public class TileMilestone extends TileBCBase {

    public static Map<String, Integer> switchingCooldowns = new HashMap<String, Integer>();

    public int coolDown = 0;
    public Map<String, Integer> cooldowns = new HashMap<String, Integer>();
    public SyncableVec3D milestonePos = new SyncableVec3D(new Vec3D(0, -1, 0), true, false);
    public SyncableString markerName = new SyncableString("Unnamed...", true, false);
    public List<String> users = new ArrayList<>();

    public TileMilestone() {
        registerSyncableObject(milestonePos, true);
        registerSyncableObject(markerName, true);
    }

    public void onActivated(EntityPlayer player){
        TTWorldData.MilestoneMarker current = TTWorldData.getMap(worldObj).get(player.getName());

        if (current != null && current.x == pos.getX() && current.y == pos.getY() && current.z == pos.getZ() && current.dimension == worldObj.provider.getDimension()){
            return;
        }

        int time = switchingCooldowns.containsKey(player.getName()) ? (switchingCooldowns.get(player.getName()) + ConfigHandler.milestoneCoolDown * 20) - ForgeEventHandler.tick : 0;

        if (time > 0 && !player.capabilities.isCreativeMode){
            time /= 20;
            String m;
            if (time > 60){
                m = time / 60 + " minute" + (time / 60 > 1 ? "s" : "");
            }
            else {
                m = time + " second" + (time > 1 ? "s" : "");
            }

            player.addChatComponentMessage(new TextComponentString("You must wait "+m+" before you can switch milestones").setStyle(new Style().setColor(TextFormatting.RED)));
            return;
        }

        switchingCooldowns.put(player.getName(), ForgeEventHandler.tick);

        TTWorldData.MilestoneMarker marker = TTWorldData.getMap(worldObj).get(player.getName());
        TTWorldData.addMarker(worldObj, player.getName(), pos.getX(), pos.getY(), pos.getZ(), worldObj.provider.getDimension());
        dirtyBlock();
        updateBlock();

        if (marker == null){
            return;
        }

        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        World world = server.worldServerForDimension(marker.dimension);

        TileEntity tile = world.getTileEntity(new BlockPos(marker.x, marker.y, marker.z));

        if (tile instanceof TileMilestone && !world.isRemote){
            ((TileMilestone) tile).users.clear();
            ((TileMilestone) tile).dirtyBlock();
            ((TileMilestone) tile).updateBlock();
        }
    }

    @Override
    public void writeExtraNBT(NBTTagCompound compound) {
        super.writeExtraNBT(compound);

        users.clear();
        if (worldObj != null){
            Map<String, TTWorldData.MilestoneMarker> markers = TTWorldData.getMap(worldObj);
            for (String name : markers.keySet()){
                TTWorldData.MilestoneMarker marker = markers.get(name);
                if (marker.x == pos.getX() && marker.y == pos.getY() && marker.z == pos.getZ()){
                    users.add(name);
                }
            }
        }

        NBTTagList list = new NBTTagList();
        for (String user : users){
            list.appendTag(new NBTTagString(user));
        }
        compound.setTag("Users", list);
    }

    @Override
    public void readExtraNBT(NBTTagCompound compound) {
        super.readExtraNBT(compound);
        users.clear();
        if (compound.hasKey("Users")){
            NBTTagList list = compound.getTagList("Users", (byte)8);
            for (int i = 0; i < list.tagCount(); i++){
                users.add(list.getStringTagAt(i));
            }
        }

    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return super.getRenderBoundingBox().expand(0, 1, 0);
    }

    public void handleTeleport(EntityPlayer player, boolean force){
        int time = cooldowns.containsKey(player.getName()) ? (cooldowns.get(player.getName()) + coolDown) - ForgeEventHandler.tick : 0;

        if (time > 0 && !force && !player.capabilities.isCreativeMode){
            player.addChatComponentMessage(new TextComponentString(String.format("You must wait %s more seconds before using this milestone again", time / 20)));
            return;
        }

        LogHelper.info(milestonePos.vec.y);
        if (milestonePos.vec.y == -1){
            milestonePos.vec = new Vec3D(pos).add(0.5, 2, 0.5);
        }

        cooldowns.put(player.getName(), ForgeEventHandler.tick);

        TeleportUtils.teleportEntity(player, worldObj.provider.getDimension(), milestonePos.vec.x, milestonePos.vec.y, milestonePos.vec.z);
    }

}
