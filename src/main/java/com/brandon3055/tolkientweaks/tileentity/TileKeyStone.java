package com.brandon3055.tolkientweaks.tileentity;

import codechicken.lib.data.MCDataInput;
import com.brandon3055.brandonscore.BrandonsCore;
import com.brandon3055.brandonscore.blocks.TileBCBase;
import com.brandon3055.brandonscore.lib.IActivatableTile;
import com.brandon3055.brandonscore.lib.IRedstoneEmitter;
import com.brandon3055.brandonscore.lib.datamanager.ManagedBool;
import com.brandon3055.brandonscore.lib.datamanager.ManagedInt;
import com.brandon3055.brandonscore.lib.datamanager.ManagedString;
import com.brandon3055.tolkientweaks.TTFeatures;
import com.brandon3055.tolkientweaks.client.gui.GuiKeyAccess;
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

/**
 * Created by Brandon on 8/02/2015.
 */
public class TileKeyStone extends TileChameleon implements IRedstoneEmitter, ITickable, IActivatableTile, IKeyAccessTile {

    public final ManagedString keyCode = register("key_code", new ManagedString("")).syncViaTile().saveToTile().finish();
    public final ManagedInt delay = register("delay", new ManagedInt(10)).syncViaTile().saveToTile().finish();
    public final ManagedBool consumeKey = register("consume_key", new ManagedBool(false)).syncViaTile().saveToTile().finish();
    public final ManagedBool active = register("active", new ManagedBool(false)).saveToTile().finish();
    public final ManagedInt timeActive = register("time_active", new ManagedInt(10)).saveToTile().finish();
    public Mode mode = Mode.BUTTON;
    public boolean disableCamo = false;

    public TileKeyStone() {
        super(TTFeatures.camoKeystone);
    }

    @Override
    public void update() {
        super.update();

        if (mode == Mode.BUTTON && active.value && timeActive.value++ > delay.value) {
            timeActive.value = 0;
            active.value = false;
            updateBlock();
            world.notifyNeighborsOfStateChange(pos, getBlockType(), true);
            world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_STONE_BUTTON_CLICK_OFF, SoundCategory.BLOCKS, 0.3F, 0.5F, false);
        }

        if (world.isRemote) {
            EntityPlayer player = BrandonsCore.proxy.getClientPlayer();

            if (player == null) {
                return;
            }

            ItemStack key = player.getHeldItemMainhand();
            boolean shouldDisableCamo = (!key.isEmpty() && key.getItem() == TTFeatures.key && (((Key) key.getItem()).getKey(key).equals(keyCode.value) || key.getItemDamage() == 1));

            if (shouldDisableCamo != disableCamo) {
                disableCamo = shouldDisableCamo;
                updateBlock();
            }
        }
    }


    @Override
    public boolean onBlockActivated(IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if (player.isCreative() && stack.isEmpty()) {
            if (world.isRemote) {
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
            world.playSound(player, pos, SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON, SoundCategory.BLOCKS, 0.3F, 0.6F);
        }
        else {
            world.playSound(player, pos, SoundEvents.BLOCK_STONE_BUTTON_CLICK_OFF, SoundCategory.BLOCKS, 0.3F, 0.5F);
        }

        if (world.isRemote) {
            return true;
        }

        if (consumeKey.value && !player.isCreative()) {
            stack.shrink(1);
            player.setHeldItem(hand, stack);
        }

        world.notifyNeighborsOfStateChange(pos, getBlockType(), true);

        return true;
    }

    @SideOnly(Side.CLIENT)
    public void openGUI() {
        Minecraft.getMinecraft().displayGuiScreen(new GuiKeyAccess(Minecraft.getMinecraft().player, this));
    }

    @Override
    public void receivePacketFromClient(MCDataInput data, EntityPlayerMP client, int id) {
        if (!client.capabilities.isCreativeMode) {
            return;
        }
        switch (id) {
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
                world.notifyNeighborsOfStateChange(pos, getBlockType(), true);
                break;
            case 4:
                keyCode.value = data.readString();
                break;
            case 5:
                try {
                    delay.value = Integer.parseInt(data.readString());
                }
                catch (Exception e) {
                    client.sendMessage(new TextComponentString("Error parsing string! Is " + data.readString() + " a valid number?").setStyle(new Style().setColor(TextFormatting.DARK_RED)));
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
        if (key != null && key.getItem() == TTFeatures.key && (((Key) key.getItem()).getKey(key).equals(keyCode.value) || key.getItemDamage() == 1)) {
            return true;
        }
        if (world.isRemote) {
            if (key != null && key.getItem() == TTFeatures.key) {
                player.sendMessage(new TextComponentTranslation("msg.tolkienaddon.keyWrong.txt"));
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

    //region KeyAccess

    @Override
    public TileBCBase getTile() {
        return this;
    }

    @Override
    public boolean hasCK() {
        return true;
    }

    @Override
    public boolean consumeKey() {
        return consumeKey.value;
    }

    @Override
    public boolean hasMode() {
        return true;
    }

    @Override
    public Mode mode() {
        return mode;
    }

    @Override
    public String getCode() {
        return keyCode.value;
    }

    @Override
    public boolean hasDelay() {
        return true;
    }

    @Override
    public int getDelay() {
        return delay.value;
    }

    //endregion

    public enum Mode {
        PERMANENT,
        BUTTON,
        TOGGLE;
    }
}
