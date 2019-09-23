package com.brandon3055.tolkientweaks.client;

import codechicken.lib.reflect.ObfMapping;
import codechicken.lib.reflect.ReflectionManager;
import com.brandon3055.brandonscore.handlers.HandHelper;
import com.brandon3055.tolkientweaks.TTFeatures;
import net.minecraft.client.gui.*;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;

/**
 * Created by Brandon on 14/01/2015.
 */
@SideOnly(Side.CLIENT)
public class ClientEventHandler {

	@SubscribeEvent
	public void renderPlayerPre(RenderPlayerEvent.Pre event) {
		if (HandHelper.isHoldingItemEther(event.getEntityPlayer(), TTFeatures.ring)){
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void guiOpen(GuiOpenEvent event)
	{
		if (event.getGui() instanceof GuiShareToLan)
		{
			GuiShareToLan gui = (GuiShareToLan) event.getGui();

			try
			{
//				Field parentGui = ReflectionHelper.findField(GuiShareToLan.class, "field_146598_a", "lastScreen");
//				parentGui.setAccessible(true);
//
//				Object parent = parentGui.get(gui);

				ObfMapping bufferMapping = new ObfMapping("net/minecraft/client/gui/GuiShareToLan", "lastScreen");
				GuiScreen parent = ReflectionManager.getField(bufferMapping, gui, GuiScreen.class);

				event.setGui(new GuiLane(parent));
			}
			catch (Throwable e)
			{
				e.printStackTrace();
			}
		}
	}

	private static class GuiLane extends GuiShareToLan {

		public GuiLane(GuiScreen p_i1055_1_) {
			super(p_i1055_1_);
		}

		@Override
		public void initGui() {
			super.initGui();
			if (buttonList.size() >= 4) {
				buttonList.remove(3);
				buttonList.remove(2);
			}
		}

		@Override
		public void drawScreen(int mouseX, int mouseY, float partialTicks)
		{
			this.drawDefaultBackground();
			this.drawCenteredString(this.fontRenderer, I18n.format("lanServer.title"), this.width / 2, 50, 16777215);
			this.drawCenteredString(this.fontRenderer, I18n.format("info.tt.guiLane1.txt"), this.width / 2, 82, 16777215);
			this.drawCenteredString(this.fontRenderer, I18n.format("info.tt.guiLane2.txt"), this.width / 2, 92, 16777215);
			this.drawCenteredString(this.fontRenderer, I18n.format("info.tt.guiLane3.txt"), this.width / 2, 102, 16777215);

			int k;

			for (k = 0; k < this.buttonList.size(); ++k)
			{
				((GuiButton)this.buttonList.get(k)).drawButton(this.mc, mouseX, mouseY, partialTicks);
			}

			for (k = 0; k < this.labelList.size(); ++k)
			{
				((GuiLabel)this.labelList.get(k)).drawLabel(this.mc, mouseX, mouseY);
			}
		}
	}
}