package com.brandon3055.tolkientweaks.tileentity;

import mods.railcraft.common.util.effects.EffectManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by Brandon on 8/02/2015.
 */
public class TileSmoker extends TileEntity {

	public int block;
	public int meta;

	@Override
	public void updateEntity() {
		if (worldObj.isRemote && !worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord))
		{
			if(this.worldObj.isAirBlock(xCoord, yCoord + 1, zCoord)) {
				double px = (double)((float)this.xCoord + worldObj.rand.nextFloat());
				double py = (double)((float)this.yCoord + worldObj.rand.nextFloat() * 0.5F + 1.0F);
				double pz = (double)((float)this.zCoord + worldObj.rand.nextFloat());
				EffectManager.instance.chimneyEffect(worldObj, px, py, pz);
			}
		}
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound tagCompound = new NBTTagCompound();
		writeToNBT(tagCompound);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, tagCompound);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		readFromNBT(pkt.func_148857_g());
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		block = compound.getInteger("Block");
		meta = compound.getInteger("Meta");
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger("Block", block);
		compound.setInteger("Meta", meta);
	}
}
