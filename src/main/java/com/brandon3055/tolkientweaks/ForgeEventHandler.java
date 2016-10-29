package com.brandon3055.tolkientweaks;


import com.brandon3055.brandonscore.client.gui.GuiButtonAHeight;
import com.brandon3055.tolkientweaks.client.gui.GuiMilestone;
import com.brandon3055.tolkientweaks.network.PacketMilestone;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
		if (event.getEntityItem() != null && event.getEntityItem().getEntityItem() != null && (event.getEntityItem().getEntityItem().getItem() == TTFeatures.ring || event.getEntityItem().getEntityItem().getItem() == TTFeatures.palantir) && event.getPlayer() != null)
		{
			ItemStack stack = event.getEntityItem().getEntityItem();
			if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setString("PlayerUUID", event.getPlayer().getUniqueID().toString());
		}
	}

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void guiInit(GuiScreenEvent.InitGuiEvent.Post event){
        if (event.getGui() instanceof GuiInventory){
            int x = event.getGui().width / 2 - 30;
            int y = event.getGui().height / 2 + 90;
            event.getButtonList().add(new GuiButtonAHeight(MILESTONE_BUTTON_ID, x, y, 60, 18, "Milestone"));
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void buttonClick(GuiScreenEvent.ActionPerformedEvent.Post event){
        if (event.getGui() instanceof GuiInventory && event.getButton().id == MILESTONE_BUTTON_ID){
            Minecraft.getMinecraft().displayGuiScreen(new GuiMilestone(event.getGui()));
            TolkienTweaks.network.sendToServer(new PacketMilestone(PacketMilestone.REQ_NAME, ""));
        }
    }

}
