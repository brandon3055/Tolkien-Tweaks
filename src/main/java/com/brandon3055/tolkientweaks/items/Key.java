package com.brandon3055.tolkientweaks.items;

import codechicken.lib.util.ItemNBTUtils;
import com.brandon3055.tolkientweaks.client.gui.GuiKeyAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Created by Brandon on 24/01/2015.
 */
public class Key extends Item {
	public Key() {
		this.setMaxStackSize(1);
	}

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        if (player.isCreative() && world.isRemote && stack.getItemDamage() == 0) {
            openGUI(player);
        }

        return super.onItemRightClick(stack, world, player, hand);
    }

    @SideOnly(Side.CLIENT)
    private void openGUI(EntityPlayer player) {
        Minecraft.getMinecraft().displayGuiScreen(new GuiKeyAccess(player, null));
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> subItems) {
        subItems.add(new ItemStack(item));
        subItems.add(new ItemStack(item, 1, 1));
        subItems.add(new ItemStack(item, 1, 2));
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return stack.getItemDamage() == 1;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        if (stack.getItemDamage() == 0) {
            if (getShown(stack)) {
                if (playerIn.isCreative()) {
                    tooltip.add(TextFormatting.GREEN + "Key visible to non-creative players");
                }
                tooltip.add(TextFormatting.GOLD + getKey(stack));
            }
            else if (playerIn.isCreative()) {
                tooltip.add(TextFormatting.RED + "Key hidden from non-creative players");
                tooltip.add(TextFormatting.RED + getKey(stack));
            }
        }
        if (stack.getItemDamage() == 1) {
            tooltip.add(TextFormatting.GOLD + "Master Key");
        }


        super.addInformation(stack, playerIn, tooltip, advanced);
    }

    public void setKey(ItemStack stack, String key) {
        ItemNBTUtils.setString(stack, "KeyCode", key);
    }

    public String getKey(ItemStack stack) {
        return ItemNBTUtils.getString(stack, "KeyCode");
    }

    public void setShown(ItemStack stack, boolean show) {
        ItemNBTUtils.setBoolean(stack, "ShowCode", show);
    }

    public boolean getShown(ItemStack stack) {
        return ItemNBTUtils.getBoolean(stack, "ShowCode");
    }
}
