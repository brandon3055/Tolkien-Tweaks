package com.brandon3055.tolkientweaks.utils;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by brandon3055 on 3/06/2016.
 */
public class TTWorldData extends WorldSavedData {
    public static final String DATA_TAG = "TTweaksWorldData";
    protected Map<String, MilestoneMarker> markers = new HashMap<String, MilestoneMarker>();

    public TTWorldData(String name) {
        super(name);
    }

    public static Map<String, MilestoneMarker> getMap(World world){

        if (!(world.getMapStorage().getOrLoadData(TTWorldData.class, DATA_TAG) instanceof TTWorldData)){
            world.getMapStorage().setData(DATA_TAG, new TTWorldData(DATA_TAG));
        }

        TTWorldData ttWorldData = (TTWorldData) world.getMapStorage().getOrLoadData(TTWorldData.class, DATA_TAG);

        return ttWorldData.markers;
    }

    /**
     * to remove a player from the map set y to -1
     * */
    public static void addMarker(World world, String user, int x, int y, int z, int dimension){
        if (!(world.getMapStorage().getOrLoadData(TTWorldData.class, DATA_TAG) instanceof TTWorldData)){
            world.getMapStorage().setData(DATA_TAG, new TTWorldData(DATA_TAG));
        }

        TTWorldData ttWorldData = (TTWorldData) world.getMapStorage().getOrLoadData(TTWorldData.class, DATA_TAG);

        if (ttWorldData.markers.containsKey(user)){
            ttWorldData.markers.remove(user);
        }

        if(y == -1) {
            return;
        }

        ttWorldData.markers.put(user, new MilestoneMarker(user, x, y, z, dimension));
        ttWorldData.markDirty();
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        NBTTagList list = compound.getTagList("Milestones", 10);

        for (int i = 0; i < list.tagCount(); i++){
            NBTTagCompound tag = list.getCompoundTagAt(i);
            markers.put(tag.getString("User"), new MilestoneMarker().readNBT(tag));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagList list = new NBTTagList();

        for (String name : markers.keySet()){
            list.appendTag(markers.get(name).writeNBT());
        }

        compound.setTag("Milestones", list);
        return compound;
    }

    public static class MilestoneMarker {
        public String username;
        public int x;
        public int y;
        public int z;
        public int dimension;

        public MilestoneMarker(){}

        public MilestoneMarker(String username, int x, int y, int z, int dimension){
            this.username = username;
            this.x = x;
            this.y = y;
            this.z = z;
            this.dimension = dimension;
        }

        public NBTTagCompound writeNBT(){
            NBTTagCompound compound = new NBTTagCompound();
            compound.setInteger("X", x);
            compound.setInteger("Y", y);
            compound.setInteger("Z", z);
            compound.setInteger("Dim", dimension);
            compound.setString("User", username);
            return compound;
        }

        public MilestoneMarker readNBT(NBTTagCompound compound){
            x = compound.getInteger("X");
            y = compound.getInteger("Y");
            z = compound.getInteger("Z");
            dimension = compound.getInteger("Dim");
            username = compound.getString("User");
            return this;
        }
    }
}
