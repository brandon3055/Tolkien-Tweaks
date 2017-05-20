package com.brandon3055.tolkientweaks.client.gui;

import com.brandon3055.tolkientweaks.TolkienTweaks;
import com.brandon3055.tolkientweaks.container.ContainerCamoChest;
import com.brandon3055.tolkientweaks.container.ContainerCoinPouch;
import com.brandon3055.tolkientweaks.container.ContainerKeyChain;
import com.brandon3055.tolkientweaks.container.InventoryItemStackDynamic;
import com.brandon3055.tolkientweaks.items.CoinPouch;
import com.brandon3055.tolkientweaks.items.KeyChain;
import com.brandon3055.tolkientweaks.tileentity.TileCamoChest;
import com.brandon3055.tolkientweaks.tileentity.TileLockableChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

/**
 * Created by Brandon on 8/03/2015.
 */
public class GuiHandler implements IGuiHandler {

    public static final int ID_CAMO_CHEST = 0;
    public static final int ID_KEYSTONE = 1;
    public static final int ID_LOCKABLE_CHEST = 2;
    public static final int ID_KEYCHAIN = 3;
    public static final int ID_COIN_POUCH = 4;

    public GuiHandler() {
        NetworkRegistry.INSTANCE.registerGuiHandler(TolkienTweaks.instance, this);
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity tile = world.getTileEntity(pos);

        switch (ID) {
            case ID_CAMO_CHEST: {
                if (tile instanceof TileCamoChest) {
                    return new ContainerCamoChest((TileCamoChest) tile, player.inventory);
                }
            }
            case ID_LOCKABLE_CHEST: {
                if (tile instanceof TileLockableChest) {
                    return ((TileLockableChest) tile).createContainer(player.inventory, player);
                }
            }
            case ID_KEYCHAIN: {
                ItemStack stack = player.getHeldItemMainhand();
                EnumHand hand = EnumHand.MAIN_HAND;
                if (stack == null || !(stack.getItem() instanceof KeyChain)) {
                    stack = player.getHeldItemOffhand();
                    hand = EnumHand.OFF_HAND;
                }
                if (stack == null || !(stack.getItem() instanceof KeyChain)) {
                    return null;
                }

                return new ContainerKeyChain(player.inventory, new InventoryItemStackDynamic(stack, 54), player, hand);
            }
            case ID_COIN_POUCH: {
                ItemStack stack = player.getHeldItemMainhand();
                EnumHand hand = EnumHand.MAIN_HAND;
                if (stack == null || !(stack.getItem() instanceof CoinPouch)) {
                    stack = player.getHeldItemOffhand();
                    hand = EnumHand.OFF_HAND;
                }
                if (stack == null || !(stack.getItem() instanceof CoinPouch)) {
                    return null;
                }

                return new ContainerCoinPouch(player.inventory, new InventoryItemStackDynamic(stack, 54), player, hand);
            }
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity tile = world.getTileEntity(pos);

        switch (ID) {
            case ID_CAMO_CHEST: {
                if (tile instanceof TileCamoChest) {
                    return new GuiCamoChest((TileCamoChest) tile, player.inventory);
                }
            }
            case ID_LOCKABLE_CHEST: {
                if (tile instanceof TileLockableChest) {
                    return new GuiLockableChest(((TileLockableChest) tile).getInventory(player.getHeldItemMainhand()), player.inventory);
                }
            }
            case ID_KEYCHAIN: {
                ItemStack stack = player.getHeldItemMainhand();
                EnumHand hand = EnumHand.MAIN_HAND;
                if (stack == null || !(stack.getItem() instanceof KeyChain)) {
                    stack = player.getHeldItemOffhand();
                    hand = EnumHand.OFF_HAND;
                }
                if (stack == null || !(stack.getItem() instanceof KeyChain)) {
                    return null;
                }

                return new GuiKeyChain(new InventoryItemStackDynamic(stack, 54), player.inventory, hand);
            }
            case ID_COIN_POUCH: {
                ItemStack stack = player.getHeldItemMainhand();
                EnumHand hand = EnumHand.MAIN_HAND;
                if (stack == null || !(stack.getItem() instanceof CoinPouch)) {
                    stack = player.getHeldItemOffhand();
                    hand = EnumHand.OFF_HAND;
                }
                if (stack == null || !(stack.getItem() instanceof CoinPouch)) {
                    return null;
                }

                return new GuiCoinPouch(new InventoryItemStackDynamic(stack, 54), player.inventory, hand);
            }
        }
        return null;
    }
}
