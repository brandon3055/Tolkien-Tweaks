package com.brandon3055.tolkientweaks.tileentity;

import com.brandon3055.brandonscore.blocks.TileInventoryBase;
import com.brandon3055.brandonscore.client.ResourceHelperBC;
import com.brandon3055.brandonscore.network.PacketTileMessage;
import com.brandon3055.brandonscore.network.wrappers.SyncableByte;
import com.brandon3055.brandonscore.network.wrappers.SyncableString;
import com.brandon3055.tolkientweaks.blocks.ChameleonBlock;
import com.brandon3055.tolkientweaks.utils.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Brandon on 8/03/2015.
 */
public class TileCamoChest extends TileInventoryBase {

	private final SyncableString blockName = new SyncableString("minecraft:stone", true, false, false);
	private final SyncableByte blockMeta = new SyncableByte((byte) 0, true, false, false);

	public TileCamoChest() {
		setInventorySize(27);
		registerSyncableObject(blockName, true);
		registerSyncableObject(blockMeta, true);
		setShouldRefreshOnBlockChange();
	}

	//region Camo

	public final IBlockState getChameleonBlockState() {
		Block block = Block.REGISTRY.getObject(ResourceHelperBC.getResourceRAW(blockName.value));
		if (block == Blocks.AIR) {
			LogHelper.warn("TileChameleon: Could not load state from block - " + blockName.value + " With Meta " + blockMeta.value + " Using fallback state.");
			return Blocks.STONE.getDefaultState();
		}
		else {
			return block.getStateFromMeta(blockMeta.value);
		}
	}

	public void setChameleonState(IBlockState chameleonState) {
		ResourceLocation name = chameleonState.getBlock().getRegistryName();
		if (name == null) {
			blockName.value = "minecraft:stone";
			LogHelper.warn("TileChameleon: The given block dose not seem to be registered " + chameleonState + " Umm..... So.... What?!?! That should not be possible........");
		}
		else {
			blockName.value = name.toString();
		}

		blockMeta.value = (byte) chameleonState.getBlock().getMetaFromState(chameleonState);
//		thisBlock.setNewChameleonState(worldObj, pos, worldObj.getBlockState(pos), chameleonState);
		detectAndSendChanges();
		NBTTagCompound compound = new NBTTagCompound();
		compound.setByte("Meta", blockMeta.value);
		compound.setString("Name", blockName.value);
		if (!worldObj.isRemote) {
			sendPacketToClients(new PacketTileMessage(this, (byte) 0, compound, false), syncRange());
		}
	}

	@Override
	public void receivePacketFromServer(PacketTileMessage packet) {
		if (packet.isNBT()) {
			blockMeta.value = packet.compound.getByte("Meta");
			blockName.value = packet.compound.getString("Name");
			updateBlock();
		}
	}

	public boolean attemptSetFromStack(ItemStack stack) {
		if (stack == null || !(stack.getItem() instanceof ItemBlock)) {
			return false;
		}

		Block block = ((ItemBlock) stack.getItem()).getBlock();

		if (block instanceof ChameleonBlock) {
			return false;
		}

		setChameleonState(block.getStateFromMeta(stack.getItemDamage()));
		return true;
	}

	//endregion

	@Override
	public boolean shouldRenderInPass(int pass) {
		return true;
	}


//	@Override
//	public String getName()
//	{
//		return "container.tt.camoChest";
//	}

//
//	@Override
//	public int getSizeInventory()
//	{
//		return items.length;
//	}
//
//	@Override
//	public ItemStack getStackInSlot(int i)
//	{
//		return items[i];
//	}
//
//	@Override
//	public ItemStack decrStackSize(int i, int count)
//	{
//		ItemStack itemstack = getStackInSlot(i);
//
//		if (itemstack != null)
//		{
//			if (itemstack.stackSize <= count)
//			{
//				setInventorySlotContents(i, null);
//			} else
//			{
//				itemstack = itemstack.splitStack(count);
//				if (itemstack.stackSize == 0)
//				{
//					setInventorySlotContents(i, null);
//				}
//			}
//		}
//		return itemstack;
//	}
//
//	@Override
//	public ItemStack removeStackFromSlot(int i)
//	{
//		ItemStack item = getStackInSlot(i);
//		if (item != null)
//			setInventorySlotContents(i, null);
//		return item;
//	}
//
//	@Override
//	public void setInventorySlotContents(int i, ItemStack itemstack)
//	{
//		items[i] = itemstack;
//		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit())
//		{
//			itemstack.stackSize = getInventoryStackLimit();
//		}
//	}
//

//
//	@Override
//	public boolean hasCustomName()
//	{
//		return false;
//	}
//
//	@Override
//	public int getInventoryStackLimit()
//	{
//		return 64;
//	}
//
//	@Override
//	public boolean isUseableByPlayer(EntityPlayer player)
//	{
//		return player.getDistanceSqToCenter(pos) < 64;
//	}
//
//	@Override
//	public void openInventory(EntityPlayer player) {
//
//	}
//
//	@Override
//	public void closeInventory(EntityPlayer player) {
//
//	}
//
//	@Override
//	public boolean isItemValidForSlot(int i, ItemStack itemstack)
//	{
//		return true;
//	}
//
//	public Packet getDescriptionPacket() {
//		NBTTagCompound compound = new NBTTagCompound();
//		compound.setInteger("Block", block);
//		compound.setInteger("Meta", meta);
//		super.writeToNBT(compound);
//		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, compound);
//	}
//
//	@Override
//	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
//		readFromNBT(pkt.func_148857_g());
//	}
//
//	@Override
//	public void readFromNBT(NBTTagCompound compound) {
//		super.readFromNBT(compound);
//		block = compound.getInteger("Block");
//		if (Block.getBlockById(block) == ModBlocks.smoker || Block.getBlockById(block) == ModBlocks.camoChest) block = 0;
//		meta = compound.getInteger("Meta");
//
//		NBTTagCompound[] tag = new NBTTagCompound[items.length];
//
//		for (int i = 0; i < items.length; i++)
//		{
//			tag[i] = compound.getCompoundTag("Item" + i);
//			items[i] = ItemStack.loadItemStackFromNBT(tag[i]);
//		}
//	}
//
//	@Override
//	public void writeToNBT(NBTTagCompound compound) {
//		super.writeToNBT(compound);
//		compound.setInteger("Block", block);
//		compound.setInteger("Meta", meta);
//
//		NBTTagCompound[] tag = new NBTTagCompound[items.length];
//
//		for (int i = 0; i < items.length; i++)
//		{
//			tag[i] = new NBTTagCompound();
//
//			if (items[i] != null)
//			{
//				tag[i] = items[i].writeToNBT(tag[i]);
//			}
//
//			compound.setTag("Item" + i, tag[i]);
//		}
//	}
}
