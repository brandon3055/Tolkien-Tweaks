package com.brandon3055.tolkientweaks.container;

import com.brandon3055.brandonscore.inventory.ContainerBCBase;
import com.brandon3055.tolkientweaks.TTFeatures;
import com.brandon3055.tolkientweaks.tileentity.TileLockableChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

/**
 * Created by brandon3055 on 16/04/2017.
 */
public class ContainerLockableChest extends ContainerBCBase<TileLockableChest> {

    public final InventoryLockableChest lockableInventory;
    private final int numRows;

    public ContainerLockableChest(TileLockableChest tile, IInventory playerInventory, InventoryLockableChest chestInventory, EntityPlayer player)
    {
        super(player, tile);
        this.lockableInventory = chestInventory;
        this.numRows = chestInventory.getSizeInventory() / 9;
        chestInventory.openInventory(player);
        int i = (this.numRows - 4) * 18;

        for (int j = 0; j < this.numRows; ++j)
        {
            for (int k = 0; k < 9; ++k)
            {
                this.addSlotToContainer(new Slot(chestInventory, k + j * 9, 8 + k * 18, 18 + j * 18));
            }
        }

        for (int l = 0; l < 3; ++l)
        {
            for (int j1 = 0; j1 < 9; ++j1)
            {
                this.addSlotToContainer(new SlotPlayerInv(playerInventory, j1 + l * 9 + 9, 8 + j1 * 18, 103 + l * 18 + i));
            }
        }

        for (int i1 = 0; i1 < 9; ++i1)
        {
            this.addSlotToContainer(new SlotPlayerInv(playerInventory, i1, 8 + i1 * 18, 161 + i));
        }
    }

    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return this.lockableInventory.isUsableByPlayer(playerIn);
    }

    @Nullable
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot)this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < this.numRows * 9)
            {
                if (!this.mergeItemStack(itemstack1, this.numRows * 9, this.inventorySlots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 0, this.numRows * 9, false))
            {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    public void onContainerClosed(EntityPlayer playerIn)
    {
        super.onContainerClosed(playerIn);
        this.lockableInventory.closeInventory(playerIn);
    }

    public class SlotPlayerInv extends Slot {

        public SlotPlayerInv(IInventory iInventory, int slot, int x, int y) {
            super(iInventory, slot, x, y);
        }

        @Override
        public boolean canTakeStack(EntityPlayer player) {
            if (getHasStack() && getStack().getItem() == TTFeatures.key) {
                return false;
            }
            return super.canTakeStack(player);
        }
    }
}
