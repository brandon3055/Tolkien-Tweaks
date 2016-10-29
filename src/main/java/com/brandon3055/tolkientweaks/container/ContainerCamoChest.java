package com.brandon3055.tolkientweaks.container;

import com.brandon3055.brandonscore.inventory.ContainerBCBase;
import com.brandon3055.tolkientweaks.tileentity.TileCamoChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Created by Brandon on 8/03/2015.
 */
public class ContainerCamoChest extends ContainerBCBase<TileCamoChest> {

	public ContainerCamoChest(TileCamoChest tile, InventoryPlayer invPlayer) {
		super(invPlayer.player, tile);
		this.tile = tile;

		for (int x = 0; x < 9; x++) {
			addSlotToContainer(new Slot(invPlayer, x, 8 + 18 * x, 143));
		}

		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				addSlotToContainer(new Slot(invPlayer, x + y * 9 + 9, 8 + 18 * x, 85 + y * 18));
			}
		}

		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				addSlotToContainer(new Slot(tile, x + y * 9, 8 + 18 * x, 18 + y * 18));
			}
		}
		player.playSound(SoundEvents.BLOCK_CHEST_OPEN, 0.5F, player.worldObj.rand.nextFloat() * 0.1F + 0.9F);
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		player.playSound(SoundEvents.BLOCK_CHEST_CLOSE, 0.5F, player.worldObj.rand.nextFloat() * 0.1F + 0.9F);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int i)
	{
		Slot slot = getSlot(i);

		if (slot != null && slot.getHasStack())
		{
			ItemStack stack = slot.getStack();
			ItemStack result = stack.copy();

			if (i >= 36){
				if (!mergeItemStack(stack, 0, 36, false)){
					return null;
				}
			}else if (!mergeItemStack(stack, 36, 36 + tile.getSizeInventory(), false)){
				return null;
			}

			if (stack.stackSize == 0) {
				slot.putStack(null);
			}else{
				slot.onSlotChanged();
			}

			slot.onPickupFromSlot(player, stack);

			return result;
		}

		return null;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return tile.isUseableByPlayer(player);
	}
}
