package com.brandon3055.tolkientweaks;


import codechicken.lib.inventory.InventoryUtils;
import com.brandon3055.brandonscore.client.gui.GuiButtonAHeight;
import com.brandon3055.brandonscore.utils.ItemNBTHelper;
import com.brandon3055.tolkientweaks.ConfigHandler.ChatCommand;
import com.brandon3055.tolkientweaks.client.gui.GuiMilestone;
import com.brandon3055.tolkientweaks.container.ContainerCoinPouch;
import com.brandon3055.tolkientweaks.container.InventoryItemStackDynamic;
import com.brandon3055.tolkientweaks.items.Coin;
import com.brandon3055.tolkientweaks.network.PacketMilestone;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
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
        EntityPlayer player = event.getEntityPlayer();

        if (coins.isEmpty()) {
            return;
        }

        boolean flag = false;

        int[] ids = OreDictionary.getOreIDs(coins);
        for(int id: ids)
            if(!player.inventory.getStackInSlot(id).equals(ItemStack.EMPTY)) {
            if (OreDictionary.getOreName(id).contains("itemCoin") || OreDictionary.getOreName(id).contains("itemToken")) {
                flag = true;
                break;
            }
        }

        if (!coins.isEmpty() && coins.getItem() instanceof Coin || flag && !item.isDead) {

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

    @SubscribeEvent
    public void chatEvent(ServerChatEvent event) {
        String message = event.getComponent().getFormattedText().toLowerCase();

        for (ChatCommand command : ConfigHandler.chatCommands) {
            if (message.contains(command.trigger)) {
                EntityPlayerMP player = event.getPlayer();
                MinecraftServer server = player.mcServer;
                if (server == null) return;
                COMMAND_SENDER.update(server, player.world, player.getPosition());
                command.checkRunCommand(player, server, COMMAND_SENDER);
            }
        }
    }

    private static TTCommandSender COMMAND_SENDER = new TTCommandSender();
    public static class TTCommandSender implements ICommandSender {

        private World world;
        private MinecraftServer server;
        private BlockPos position;

        public void update(MinecraftServer server, World world, BlockPos position) {
            this.server = server;
            this.world = world;
            this.position = position;
        }

        @Override
        public String getName() {
            return "[Tolkien Tweaks]";
        }

        @Override
        public boolean canUseCommand(int permLevel, String commandName) {
            return true;
        }

        @Override
        public World getEntityWorld() {
            return world;
        }

        @Nullable
        @Override
        public MinecraftServer getServer() {
            return server;
        }

        @Override
        public BlockPos getPosition() {
            return position;
        }

        @Override
        public Vec3d getPositionVector() {
            return new Vec3d(position.getX() + 0.5D, position.getY() + 0.5D, position.getZ() + 0.5D);
        }
    }
}
