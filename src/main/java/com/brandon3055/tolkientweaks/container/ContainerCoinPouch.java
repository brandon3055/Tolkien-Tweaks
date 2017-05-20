package com.brandon3055.tolkientweaks.container;

import com.brandon3055.brandonscore.inventory.ContainerBCBase;
import com.brandon3055.brandonscore.utils.ItemNBTHelper;
import com.brandon3055.tolkientweaks.TTFeatures;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

import javax.annotation.Nullable;

/**
 * Created by brandon3055 on 16/04/2017.
 */
public class ContainerCoinPouch extends ContainerBCBase {

    private final IInventory playerInventory;
    public final InventoryItemStackDynamic itemInventory;
    private final EnumHand hand;
    private int numRows;
    private int numSlots;
    private int itemTrackingNumber;
    boolean shiftClicked = false;

    public ContainerCoinPouch(IInventory playerInventory, InventoryItemStackDynamic itemInventory, EntityPlayer player, EnumHand hand) {
        this.player = player;
        this.playerInventory = playerInventory;
        this.itemInventory = itemInventory;
        this.hand = hand;
        itemInventory.openInventory(player);
        updateSlots();

        itemTrackingNumber = player.worldObj.rand.nextInt(Short.MAX_VALUE);
        ItemStack stack = player.getHeldItem(hand);
        if (stack != null) {
            ItemNBTHelper.setInteger(stack, "itemTrackingNumber", itemTrackingNumber);
        }
    }

    public void updateSlots() {
        inventorySlots.clear();
        inventoryItemStacks.clear();

        numSlots = itemInventory.getSizeInventory();
        numRows = 1 + ((itemInventory.getSizeInventory() - 1) / 9);
        int i = (numRows - 4) * 18;

        for (int j = 0; j < numRows; ++j) {
            for (int k = 0; k < 9; ++k) {
                if (k + j * 9 < itemInventory.getSizeInventory()) {
                    this.addSlotToContainer(new Slot(itemInventory, k + j * 9, 8 + k * 18, 18 + j * 18) {
                        @Override
                        public boolean isItemValid(@Nullable ItemStack stack) {
                            return stack != null && (stack.getItem() == TTFeatures.brons_coin || stack.getItem() == TTFeatures.gold_coin || stack.getItem() == TTFeatures.silver_coin);
                        }
                    });
                }
            }
        }

        for (int l = 0; l < 3; ++l) {
            for (int j1 = 0; j1 < 9; ++j1) {
                this.addSlotToContainer(new SlotPlayerInv(playerInventory, j1 + l * 9 + 9, 8 + j1 * 18, 103 + l * 18 + i));
            }
        }

        for (int i1 = 0; i1 < 9; ++i1) {
            this.addSlotToContainer(new SlotPlayerInv(playerInventory, i1, 8 + i1 * 18, 161 + i));
        }
    }

    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.itemInventory.isUseableByPlayer(playerIn);
    }

    @Override
    public void detectAndSendChanges() {
        ItemStack stack = player.getHeldItem(hand);
        if (!player.worldObj.isRemote && (stack == null || ItemNBTHelper.getInteger(stack, "itemTrackingNumber", -1) != itemTrackingNumber)) {
            player.closeScreen();
        }

        super.detectAndSendChanges();
        shiftClicked = false;
    }

    @Nullable
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        if (shiftClicked) {
            return null;
        }
        shiftClicked = true;

        ItemStack itemstack = null;
        Slot slot = (Slot) this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < numSlots) {
                //if (!this.mergeItemStack(itemstack1, numRows * 9, this.inventorySlots.size(), true)) {
                return null;
                //}
            }
            else if (!this.mergeItemStack(itemstack1, 0, numRows * 9, false)) {
                return null;
            }

            if (itemstack1.stackSize == 0) {
                slot.putStack((ItemStack) null);
            }
            else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    @Nullable
    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
        ItemStack stack = super.slotClick(slotId, dragType, clickTypeIn, player);

        int n = itemInventory.getSizeInventory();
        if (n != numSlots) {
            updateSlots();
        }

        return stack;
    }

    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        this.itemInventory.closeInventory(playerIn);
    }

    public class SlotPlayerInv extends Slot {

        public SlotPlayerInv(IInventory iInventory, int slot, int x, int y) {
            super(iInventory, slot, x, y);
        }

        @Override
        public boolean canTakeStack(EntityPlayer player) {
            if (getHasStack() && getStack().getItem() == TTFeatures.coinPouch) {
                return false;
            }
            return super.canTakeStack(player);
        }
    }
}
