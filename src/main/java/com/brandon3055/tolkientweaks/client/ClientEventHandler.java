package com.brandon3055.tolkientweaks.client;

import com.brandon3055.tolkientweaks.ModItems;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;

import java.lang.reflect.Field;

/**
 * Created by Brandon on 14/01/2015.
 */
@SideOnly(Side.CLIENT)
public class ClientEventHandler {

	@SubscribeEvent
	public void renderPlayerPre(RenderPlayerEvent.Pre event) {
		if (event.entityPlayer.getHeldItem() != null && event.entityPlayer.getHeldItem().getItem() == ModItems.ring)
			event.setCanceled(true);
	}


	@SubscribeEvent
	public void guiOpen(GuiOpenEvent event)
	{
		if (event.gui instanceof GuiShareToLan)
		{
			GuiShareToLan gui = (GuiShareToLan) event.gui;

			try
			{
				Field parentGui = ReflectionHelper.findField(GuiShareToLan.class, "field_146598_a");
				parentGui.setAccessible(true);

				Object parent = parentGui.get(gui);
				event.gui = new GuiLane((GuiIngameMenu)parent);
			}
			catch (IllegalAccessException e)
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

		public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
		{
			this.drawDefaultBackground();
			this.drawCenteredString(this.fontRendererObj, I18n.format("lanServer.title", new Object[0]), this.width / 2, 50, 16777215);
			this.drawCenteredString(this.fontRendererObj, StatCollector.translateToLocal("info.tt.guiLane1.txt"), this.width / 2, 82, 16777215);
			this.drawCenteredString(this.fontRendererObj, StatCollector.translateToLocal("info.tt.guiLane2.txt"), this.width / 2, 92, 16777215);
			this.drawCenteredString(this.fontRendererObj, StatCollector.translateToLocal("info.tt.guiLane3.txt"), this.width / 2, 102, 16777215);

			int k;

			for (k = 0; k < this.buttonList.size(); ++k)
			{
				((GuiButton)this.buttonList.get(k)).drawButton(this.mc, p_73863_1_, p_73863_2_);
			}

			for (k = 0; k < this.labelList.size(); ++k)
			{
				((GuiLabel)this.labelList.get(k)).func_146159_a(this.mc, p_73863_1_, p_73863_2_);
			}
		}
	}
}
