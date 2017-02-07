package com.brandon3055.tolkientweaks.tileentity;

import com.brandon3055.brandonscore.BrandonsCore;
import com.brandon3055.brandonscore.lib.IActivatableTile;
import com.brandon3055.brandonscore.lib.IRedstoneEmitter;
import com.brandon3055.brandonscore.network.PacketTileMessage;
import com.brandon3055.brandonscore.network.wrappers.SyncableBool;
import com.brandon3055.brandonscore.network.wrappers.SyncableInt;
import com.brandon3055.brandonscore.network.wrappers.SyncableString;
import com.brandon3055.tolkientweaks.TTFeatures;
import com.brandon3055.tolkientweaks.client.gui.GuiKeyStone;
import com.brandon3055.tolkientweaks.items.Key;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

/**
 * Created by Brandon on 8/02/2015.
 */
public class TileKeyStone extends TileChameleon implements IRedstoneEmitter, ITickable, IActivatableTile {

    public final SyncableString keyCode = new SyncableString("", true, false);
    public final SyncableInt delay = new SyncableInt(10, true, false);
    public final SyncableBool consumeKey = new SyncableBool(false, true, false);
    public final SyncableBool toggleMode = new SyncableBool(false, true, false);
//    public final SyncableBool permanent = new SyncableBool(false, true, false);
    public final SyncableBool active = new SyncableBool(false, false, false);
    public final SyncableInt timeActive = new SyncableInt(10, false, false);
    public Mode mode = Mode.BUTTON;
    public boolean disableCamo = false;

    public TileKeyStone() {
        super(TTFeatures.camoKeystone);
        registerSyncableObject(keyCode, true);
        registerSyncableObject(delay, true);
        registerSyncableObject(consumeKey, true);
        registerSyncableObject(toggleMode, true);
//        registerSyncableObject(permanent, true);
        registerSyncableObject(active, true);
        registerSyncableObject(timeActive, true);
    }

    @Override
    public void update() {
        detectAndSendChanges();

        if (mode == Mode.BUTTON && active.value && timeActive.value++ > delay.value) {
            timeActive.value = 0;
            active.value = false;
            updateBlock();
            worldObj.notifyNeighborsOfStateChange(pos, getBlockType());
            worldObj.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_STONE_BUTTON_CLICK_OFF, SoundCategory.BLOCKS, 0.3F, 0.5F, false);
        }

        if (worldObj.isRemote) {
            EntityPlayer player = BrandonsCore.proxy.getClientPlayer();

            if (player == null) {
                return;
            }

            ItemStack key = player.getHeldItemMainhand();
            boolean shouldDisableCamo = (key != null && key.getItem() == TTFeatures.key && (((Key)key.getItem()).getKey(key).equals(keyCode.value) || key.getItemDamage() == 1));

            if (shouldDisableCamo != disableCamo) {
                disableCamo = shouldDisableCamo;
                updateBlock();
            }
        }
    }

    @Override
    public boolean onBlockActivated(IBlockState state, EntityPlayer player, EnumHand hand, @Nullable ItemStack stack, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (player.isCreative() && stack == null) {
            if (worldObj.isRemote) {
                openGUI();
            }
            return true;
        }

        if (!isKeyValid(stack, player)) {
            return true;
        }

        switch (mode) {
            case PERMANENT:
                active.value = true;
                updateBlock();
                break;
            case BUTTON:
                active.value = true;
                updateBlock();
                timeActive.value = 0;
                break;
            case TOGGLE:
                active.value = !active.value;
                updateBlock();
                break;
        }

        if (active.value) {
            worldObj.playSound(player, pos, SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON, SoundCategory.BLOCKS, 0.3F, 0.6F);
        }
        else {
            worldObj.playSound(player, pos, SoundEvents.BLOCK_STONE_BUTTON_CLICK_OFF, SoundCategory.BLOCKS, 0.3F, 0.5F);
        }

        if (worldObj.isRemote) {
            return true;
        }

        if (consumeKey.value && !player.isCreative()) {
            stack.stackSize--;
            if (stack.stackSize <= 0) {
                player.setHeldItem(hand, null);
            }
            else {
                player.setHeldItem(hand, stack);
            }
        }

        worldObj.notifyNeighborsOfStateChange(pos, getBlockType());

        return true;
    }

    @SideOnly(Side.CLIENT)
    public void openGUI() {
        Minecraft.getMinecraft().displayGuiScreen(new GuiKeyStone(Minecraft.getMinecraft().thePlayer, this));
    }

    @Override
    public void receivePacketFromClient(PacketTileMessage packet, EntityPlayerMP client) {
        if (!client.capabilities.isCreativeMode) {
            return;
        }
        switch (packet.getIndex()) {
            case 0:
                consumeKey.value = !consumeKey.value;
                break;
            case 1:
                switch (mode) {
                    case PERMANENT:
                        mode = Mode.BUTTON;
                        break;
                    case BUTTON:
                        mode = Mode.TOGGLE;
                        break;
                    case TOGGLE:
                        mode = Mode.PERMANENT;
                        break;
                }
                break;
            case 2:

                break;
            case 3:
                active.value = false;
                updateBlock();
                worldObj.notifyNeighborsOfStateChange(pos, getBlockType());
                break;
            case 4:
                keyCode.value = packet.stringValue;
                break;
            case 5:
                try {
                    delay.value = Integer.parseInt(packet.stringValue);
                }catch (Exception e) {
                    client.addChatComponentMessage(new TextComponentString("Error parsing string! Is " + packet.stringValue +" a valid number?").setStyle(new Style().setColor(TextFormatting.DARK_RED)));
                }
                break;
        }
        dirtyBlock();
        updateBlock();
    }

    @Override
    public void writeExtraNBT(NBTTagCompound compound) {
        super.writeExtraNBT(compound);
        compound.setString("ActivationMode", mode.name());
    }

    @Override
    public void readExtraNBT(NBTTagCompound compound) {
        super.readExtraNBT(compound);
        if (compound.hasKey("ActivationMode")) {
            mode = Mode.valueOf(compound.getString("ActivationMode"));
        }
    }

    private boolean isKeyValid(ItemStack key, EntityPlayer player) {
		if (key != null && key.getItem() == TTFeatures.key && (((Key)key.getItem()).getKey(key).equals(keyCode.value) || key.getItemDamage() == 1)) {
			return true;
		}
        if (worldObj.isRemote) {
            if (key != null && key.getItem() == TTFeatures.key) {
                player.addChatComponentMessage(new TextComponentTranslation("msg.tolkienaddon.keyWrong.txt"));
            }
        }
        return false;
    }

    @Override
    public int getWeakPower(IBlockState blockState, EnumFacing side) {
        return active.value ? 15 : 0;
    }

    @Override
    public int getStrongPower(IBlockState blockState, EnumFacing side) {
        return active.value ? 15 : 0;
    }

    @Override
    public boolean disableCamo() {
        return disableCamo;
    }

    @Override
    public boolean randomBool() {
        return active.value;
    }

    public enum Mode {
        PERMANENT, BUTTON, TOGGLE;
    }
}
