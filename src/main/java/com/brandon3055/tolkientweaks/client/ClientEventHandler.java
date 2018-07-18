package com.brandon3055.tolkientweaks.client;

import codechicken.lib.inventory.InventoryUtils;
import com.brandon3055.brandonscore.handlers.HandHelper;
import com.brandon3055.brandonscore.utils.ItemNBTHelper;
import com.brandon3055.tolkientweaks.TTFeatures;
import com.brandon3055.tolkientweaks.container.ContainerCoinPouch;
import com.brandon3055.tolkientweaks.container.InventoryItemStackDynamic;
import com.brandon3055.tolkientweaks.items.Coin;
import net.minecraft.client.gui.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
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
	public void renderPlayerPre(EntityItemPickupEvent event) {
		EntityItem item = event.getItem();
		ItemStack coins = item.getItem();

		if (!coins.isEmpty() && coins.getItem() instanceof Coin && !item.isDead) {
			EntityPlayer player = event.getEntityPlayer();

			for (ItemStack stack : player.inventory.mainInventory) {
				if (!stack.isEmpty() && stack.getItem() == TTFeatures.coinPouch) {
					InventoryItemStackDynamic inventory = new InventoryItemStackDynamic(stack, 54);
					if (player.openContainer instanceof ContainerCoinPouch && ItemNBTHelper.getInteger(stack, "itemTrackingNumber", -1) == ((ContainerCoinPouch) player.openContainer).itemTrackingNumber) {
						inventory = ((ContainerCoinPouch) player.openContainer).itemInventory;
						player.openContainer.detectAndSendChanges();
						((ContainerCoinPouch) player.openContainer).updateSlots();
					}

					int remainder = InventoryUtils.insertItem(inventory, coins, false);
					coins.setCount(remainder);
					if (coins.isEmpty()) {
						item.setDead();
						event.setResult(Event.Result.DENY);
						player.world.playSound(null, player.posX, player.posY, player.posY, SoundEvents.ENTITY_ITEM_PICKUP, player.getSoundCategory(), 0.2F, ((player.world.rand.nextFloat() - player.world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
						player.world.playSound(null, player.posX, player.posY, player.posY, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, player.getSoundCategory(), 0.2F, ((player.world.rand.nextFloat() - player.world.rand.nextFloat()) * 0.7F + 1.0F));
						return;
					}
				}
			}
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
				Field parentGui = ReflectionHelper.findField(GuiShareToLan.class, "field_146598_a");
				parentGui.setAccessible(true);

				Object parent = parentGui.get(gui);
				event.setGui(new GuiLane((GuiIngameMenu)parent));
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
