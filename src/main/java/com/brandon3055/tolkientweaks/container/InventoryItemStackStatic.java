package com.brandon3055.tolkientweaks.container;

import com.brandon3055.brandonscore.utils.ItemNBTHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

/**
 * Created by brandon3055 on 16/04/2017.
 */
public class InventoryItemStackStatic implements IInventory {

    private ItemStack stack;
    private int stackLimit;
    private ItemStack[] items = new ItemStack[getSizeInventory()];

    public InventoryItemStackStatic(ItemStack stack, int stackLimit)
    {
        this.stack = stack;
        this.stackLimit = stackLimit;
        loadItems();
    }

    @Override
    public int getSizeInventory()
    {
        return stackLimit;
    }


    @Override
    public String getName()
    {
        return stack.getDisplayName();
    }

    @Override
    public boolean hasCustomName()
    {
        return false;
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return new TextComponentString(stack.getDisplayName());
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
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player) {
        loadItems();
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        saveItems();
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


    protected void loadItems() {
        if (stack == null) {
            return;
        }

        NBTTagCompound compound = ItemNBTHelper.getCompound(stack);
        NBTTagCompound[] tag = new NBTTagCompound[items.length];

        for (int i = 0; i < items.length; i++) {
            tag[i] = compound.getCompoundTag("Item" + i);
            items[i] = ItemStack.loadItemStackFromNBT(tag[i]);
        }
    }

    protected void saveItems() {
        if (stack == null) {
            return;
        }

        NBTTagCompound compound = ItemNBTHelper.getCompound(stack);
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
        items = new ItemStack[getSizeInventory()];
        saveItems();
    }
}
