package com.brandon3055.tolkientweaks.items;

import com.brandon3055.brandonscore.items.ItemBCore;
import com.brandon3055.tolkientweaks.TolkienTweaks;
import com.brandon3055.tolkientweaks.client.gui.GuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

/**
 * Created by brandon3055 on 20/05/2017.
 */
public class CoinPouch extends ItemBCore {

    public CoinPouch() {
        this.setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
        if (!worldIn.isRemote) {
            playerIn.openGui(TolkienTweaks.instance, GuiHandler.ID_COIN_POUCH, worldIn, 0, 0, 0);
        }
        return super.onItemRightClick(worldIn, playerIn, hand);
    }
}
