package com.brandon3055.tolkientweaks;

import com.brandon3055.draconicevolution.client.gui.GuiButtonAHeight;
import com.brandon3055.tolkientweaks.client.gui.GuiMilestone;
import com.brandon3055.tolkientweaks.network.PacketMilestone;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;

/**
 * Created by Brandon on 22/01/2015.
 */
public class ForgeEventHandler {

    public static int tick = 0;
    public static final int MILESTONE_BUTTON_ID = 24261;

    @SubscribeEvent
    public void tickEvent(TickEvent.ServerTickEvent event){
        if (event.phase == TickEvent.Phase.START) {
            tick++;
        }
    }

	@SubscribeEvent
	public void itemTossEvent(ItemTossEvent event){
		if (event.entityItem != null && event.entityItem.getEntityItem() != null && (event.entityItem.getEntityItem().getItem() == ModItems.ring || event.entityItem.getEntityItem().getItem() == ModItems.palantir) && event.player != null)
		{
			ItemStack stack = event.entityItem.getEntityItem();
			if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setString("PlayerUUID", event.player.getUniqueID().toString());
		}
	}

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void guiInit(GuiScreenEvent.InitGuiEvent.Post event){
        if (event.gui instanceof GuiInventory){
            int x = event.gui.width / 2 - 30;
            int y = event.gui.height / 2 + 90;
            event.buttonList.add(new GuiButtonAHeight(MILESTONE_BUTTON_ID, x, y, 60, 18, "Milestone"));
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void buttonClick(GuiScreenEvent.ActionPerformedEvent.Post event){
        if (event.gui instanceof GuiInventory && event.button.id == MILESTONE_BUTTON_ID){
            Minecraft.getMinecraft().displayGuiScreen(new GuiMilestone(event.gui));
            TolkienTweaks.network.sendToServer(new PacketMilestone(PacketMilestone.REQ_NAME, ""));
        }
    }

}
