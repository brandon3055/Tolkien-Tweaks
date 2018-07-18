package com.brandon3055.tolkientweaks.container;

import com.brandon3055.brandonscore.utils.ItemNBTHelper;
import com.brandon3055.brandonscore.utils.LogHelperBC;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import java.util.LinkedList;

/**
 * Created by brandon3055 on 16/04/2017.
 */
public class InventoryItemStackDynamic implements IInventory {

    private ItemStack stack;
    private int stackLimit;
    private LinkedList<ItemStack> stacks = new LinkedList<>();

    public InventoryItemStackDynamic(ItemStack stack, int stackLimit) {
        this.stack = stack;
        this.stackLimit = stackLimit;
        loadItems();
    }

    //region Dynamic Inventory

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if (index < 0) {
            return;
        }

        if (stack.isEmpty()) {
            if (index < stacks.size()) {
                stacks.remove(index);
            }
        }
        else if (index < stacks.size()) {
            stacks.set(index, stack);
        }
        else {
            stacks.add(stack);
        }
        markDirty();
    }

    @Override
    public int getSizeInventory() {
        return Math.min(stacks.size() + 1, stackLimit);
    }

    @Override
    public boolean isEmpty() {
        return stacks.isEmpty();
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return index >= 0 && index < stacks.size() ? stacks.get(index) : ItemStack.EMPTY;
    }

    public ItemStack decrStackSize(int index, int count) {
        ItemStack itemstack = getStackInSlot(index);

        if (itemstack.isEmpty()) {
            setInventorySlotContents(index, ItemStack.EMPTY);
        }
        else {
            itemstack = itemstack.splitStack(count);
            if (itemstack.isEmpty()) {
                setInventorySlotContents(index, ItemStack.EMPTY);
            }
        }
        return itemstack;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack item = getStackInSlot(index);

        if (!item.isEmpty()) {
            setInventorySlotContents(index, ItemStack.EMPTY);
        }

        return item;
    }

    //endregion

    @Override
    public String getName() {
        return stack.getDisplayName();
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentString(stack.getDisplayName());
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
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
    public void markDirty() {
        stacks.removeIf(ItemStack::isEmpty);
        saveItems();
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    public void saveItems() {
        if (stack.isEmpty()) {
            LogHelperBC.bigError("TolkienTweaks: Tried to load Dynamic Item Stack inventory from null item!");
            return;
        }

        NBTTagCompound compound = ItemNBTHelper.getCompound(stack);
        NBTTagList list = new NBTTagList();

        for (ItemStack stack : stacks) {
            if (!stack.isEmpty()) {
                NBTTagCompound tag = new NBTTagCompound();
                stack.writeToNBT(tag);
                list.appendTag(tag);
            }
        }

        compound.setTag("InvItems", list);
    }

    public void loadItems() {
        if (stack.isEmpty()) {
            LogHelperBC.bigError("TolkienTweaks: Tried to save Dynamic Item Stack inventory to null item!");
            return;
        }

        NBTTagCompound compound = ItemNBTHelper.getCompound(stack);
        NBTTagList list = compound.getTagList("InvItems", 10);
        stacks.clear();

        for (int i = 0; i < list.tagCount(); i++) {
            stacks.add(new ItemStack(list.getCompoundTagAt(i)));
        }
    }

    @Override
    public void clear() {
        stacks.clear();
        saveItems();
    }
}
