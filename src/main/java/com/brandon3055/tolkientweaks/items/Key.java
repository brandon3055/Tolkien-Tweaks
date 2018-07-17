package com.brandon3055.tolkientweaks.items;

import codechicken.lib.util.ItemNBTUtils;
import com.brandon3055.brandonscore.items.ItemBCore;
import com.brandon3055.tolkientweaks.TolkienTweaks;
import com.brandon3055.tolkientweaks.client.gui.GuiKeyAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Brandon on 24/01/2015.
 */
public class Key extends ItemBCore {
	public Key() {
		this.setMaxStackSize(1);
        this.setHasSubtypes(true);
        this.addName(0, "key");
        this.addName(1, "key");
        this.addName(2, "key");
        this.addName(3, "copper_key");
        this.addName(4, "silver_key");
        this.addName(5, "gold_key");
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
	    ItemStack stack = player.getHeldItem(hand);
        if (player.isCreative() && world.isRemote && stack.getItemDamage() != 1 && !player.isSneaking()) {
            openGUI(player);
        }
        else if (!world.isRemote && player.isCreative() && ItemNBTUtils.hasKey(stack, "playerUUID") && player.isSneaking()) {
            stack.getTagCompound().removeTag("playerUUID");
        }

        return super.onItemRightClick(world, player, hand);
    }

    @SideOnly(Side.CLIENT)
    private void openGUI(EntityPlayer player) {
        Minecraft.getMinecraft().displayGuiScreen(new GuiKeyAccess(player, null));
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            items.add(new ItemStack(this));
            items.add(new ItemStack(this, 1, 1));
            items.add(new ItemStack(this, 1, 2));
            items.add(new ItemStack(this, 1, 3));
            items.add(new ItemStack(this, 1, 4));
            items.add(new ItemStack(this, 1, 5));
        }
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return stack.getItemDamage() == 1;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
	    EntityPlayer player = TolkienTweaks.proxy.getClientPlayer();
        if (stack.getItemDamage() != 1) {
            if (getShown(stack)) {
                if (player.isCreative()) {
                    tooltip.add(TextFormatting.GREEN + "Key visible to non-creative players");
                }
                tooltip.add(TextFormatting.GOLD + getKey(stack));
            }
            else if (player != null && player.isCreative()) {
                tooltip.add(TextFormatting.RED + "Key hidden from non-creative players");
                tooltip.add(TextFormatting.RED + getKey(stack));
            }
            if (ItemNBTUtils.hasKey(stack, "playerUUID") && player != null && player.isCreative()) {
                tooltip.add(TextFormatting.GOLD + "OwnerID: " + ItemNBTUtils.getString(stack, "playerUUID"));
            }
        }
        if (stack.getItemDamage() == 1) {
            tooltip.add(TextFormatting.GOLD + "Master Key");
        }


        super.addInformation(stack, worldIn, tooltip, flagIn);
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
