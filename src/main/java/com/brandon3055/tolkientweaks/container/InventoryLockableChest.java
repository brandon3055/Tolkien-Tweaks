package com.brandon3055.tolkientweaks.container;

import com.brandon3055.brandonscore.utils.ItemNBTHelper;
import com.brandon3055.tolkientweaks.tileentity.TileLockableChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

/**
 * Created by brandon3055 on 16/04/2017.
 */
public class InventoryLockableChest implements IInventory/*, IInteractionObject*/ {
    public TileLockableChest tile;
    private ItemStack key;
    private ItemStack[] items = new ItemStack[getSizeInventory()];
//    private final IInventory upperChest;
//    private final IInventory lowerChest;

    public InventoryLockableChest(TileLockableChest tile, ItemStack key)
    {
        this.tile = tile;
        this.key = key;
//
//        if (upperChestIn == null)
//        {
//            upperChestIn = lowerChestIn;
//        }
//
//        if (lowerChestIn == null)
//        {
//            lowerChestIn = upperChestIn;
//        }
//
//        this.upperChest = upperChestIn;
//        this.lowerChest = lowerChestIn;
        loadItems();
    }

    @Override
    public int getSizeInventory()
    {
        return 54;
    }
//
//    public boolean isPartOfLargeChest(IInventory inventoryIn)
//    {
//        return this.upperChest == inventoryIn || this.lowerChest == inventoryIn;
//    }

    @Override
    public String getName()
    {
        return tile.getName();
    }

    @Override
    public boolean hasCustomName()
    {
        return false;
    }

    @Override
    public ITextComponent getDisplayName()
    {
//        if (tile.getAdjacentChest() != null) {
//            return new TextComponentString("Locked Chest");
//        }
//        else {
            return new TextComponentString("Locked Chest");
//        }
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return index < items.length && index >= 0 ? items[index] : null;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack itemstack = getStackInSlot(index);

        if (itemstack != null) {
            if (itemstack.stackSize <= count) {
                setInventorySlotContents(index, null);
            } else {
                itemstack = itemstack.splitStack(count);
                if (itemstack.stackSize == 0) {
                    setInventorySlotContents(index, null);
                }
            }
        }
        return itemstack;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack item = getStackInSlot(index);

        if (item != null) {
            setInventorySlotContents(index, null);
        }

        return item;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if (index < 0 || index >= items.length){
            return;
        }

        items[index] = stack;

        if (stack != null && stack.stackSize > getInventoryStackLimit()) {
            stack.stackSize = getInventoryStackLimit();
        }
        markDirty();
    }

    @Override
    public int getInventoryStackLimit() {
        return 54;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player) {
        loadItems();
        tile.openChest(player);
        TileLockableChest adj = tile.getAdjacentChest();
        if (adj != null) {
            adj.openChest(player);
        }
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        saveItems();
        tile.closeChest(player);
        TileLockableChest adj = tile.getAdjacentChest();
        if (adj != null) {
            adj.closeChest(player);
        }
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }

    @Override
    public void markDirty()
    {
        saveItems();
    }

    @Override
    public int getField(int id)
    {
        return 0;
    }

    @Override
    public void setField(int id, int value)
    {
    }

    @Override
    public int getFieldCount()
    {
        return 0;
    }

    public boolean isDouble() {
        return tile.getAdjacentChest() != null;
    }

/*    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
        return new ContainerLockableChest(playerInventory, this, playerIn);
    }

    @Override
    public String getGuiID() {
        return "minecraft:chest";
    }*/

    protected void loadItems() {
        if (key == null) {
            return;
        }

        NBTTagCompound compound = ItemNBTHelper.getCompound(key);
        NBTTagCompound[] tag = new NBTTagCompound[items.length];

        for (int i = 0; i < items.length; i++) {
            tag[i] = compound.getCompoundTag("Item" + i);
            items[i] = ItemStack.loadItemStackFromNBT(tag[i]);
        }
    }

    protected void saveItems() {
        if (key == null) {
            return;
        }

        NBTTagCompound compound = ItemNBTHelper.getCompound(key);
        NBTTagCompound[] tag = new NBTTagCompound[items.length];

        for (int i = 0; i < items.length; i++) {
            tag[i] = new NBTTagCompound();

            if (items[i] != null) {
                tag[i] = items[i].writeToNBT(tag[i]);
            }

            compound.setTag("Item" + i, tag[i]);
        }
    }

    @Override
    public void clear()
    {

    }
}
