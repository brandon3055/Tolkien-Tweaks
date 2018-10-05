package com.brandon3055.tolkientweaks;


import codechicken.lib.inventory.InventoryUtils;
import com.brandon3055.brandonscore.client.gui.GuiButtonAHeight;
import com.brandon3055.brandonscore.utils.ItemNBTHelper;
import com.brandon3055.tolkientweaks.client.gui.GuiMilestone;
import com.brandon3055.tolkientweaks.container.ContainerCoinPouch;
import com.brandon3055.tolkientweaks.container.InventoryItemStackDynamic;
import com.brandon3055.tolkientweaks.items.Coin;
import com.brandon3055.tolkientweaks.network.PacketMilestone;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

/**
 * Created by Brandon on 22/01/2015.
 */
public class ForgeEventHandler {

    public static int tick = 0;
    public static final int MILESTONE_BUTTON_ID = 24261;


    private static WeakHashMap<EntityLiving, Long> ttSpawnedMobs = new WeakHashMap<>();
    private static Random random = new Random();

    public static int serverTicks = 0;


    //region Ticking

    public static void onMobSpawnedBySpawner(EntityLiving entity) {
        ttSpawnedMobs.put(entity, System.currentTimeMillis());
    }

    @SubscribeEvent
    public void tickEvent(TickEvent.ServerTickEvent event){
        if (event.phase == TickEvent.Phase.END) {
            tick++;

            if (!ttSpawnedMobs.isEmpty()) {
                List<EntityLiving> toRemove = new ArrayList<>();
                long time = System.currentTimeMillis();

                for (Map.Entry<EntityLiving, Long> entry : ttSpawnedMobs.entrySet()) {
                    EntityLiving key = entry.getKey();
                    Long aLong = entry.getValue();
                    if (time - aLong > 30000) {
                        key.persistenceRequired = false;
                        toRemove.add(key);
                    }
                }

                toRemove.forEach(entity -> ttSpawnedMobs.remove(entity));
            }
        }
    }

    @SubscribeEvent
    public void itemPickup(EntityItemPickupEvent event) {
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
	public void itemTossEvent(ItemTossEvent event){
        ItemStack stack = event.getEntityItem().getItem();
		if (!stack.isEmpty() && (stack.getItem() == TTFeatures.ring || stack.getItem() == TTFeatures.palantir) && event.getPlayer() != null)
		{
			if (!stack.hasTagCompound()) {
			    stack.setTagCompound(new NBTTagCompound());
            }
			stack.getTagCompound().setString("PlayerUUID", event.getPlayer().getUniqueID().toString());
		}
	}

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.LOWEST)
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
