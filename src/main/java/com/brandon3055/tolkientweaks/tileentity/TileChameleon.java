package com.brandon3055.tolkientweaks.tileentity;

import com.brandon3055.brandonscore.blocks.TileBCBase;
import com.brandon3055.brandonscore.client.ResourceHelperBC;
import com.brandon3055.brandonscore.network.PacketTileMessage;
import com.brandon3055.brandonscore.network.wrappers.SyncableByte;
import com.brandon3055.brandonscore.network.wrappers.SyncableString;
import com.brandon3055.tolkientweaks.blocks.ChameleonBlock;
import com.brandon3055.tolkientweaks.utils.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

/**
 * Created by brandon3055 on 28/10/2016.
 */
public class TileChameleon extends TileBCBase implements IChameleonStateProvider {

    private final SyncableString blockName = new SyncableString("minecraft:stone", true, false, false);
    private final SyncableByte blockMeta = new SyncableByte((byte) 0, true, false, false);
    private ChameleonBlock thisBlock;
    private IBlockState chameleonState = null;
    private boolean usingFallbackState = true;

    public TileChameleon(ChameleonBlock thisBlock) {
        this.thisBlock = thisBlock;
        registerSyncableObject(blockName, true);
        registerSyncableObject(blockMeta, true);
        setShouldRefreshOnBlockChange();
    }

    @Override
    public final IBlockState getChameleonBlockState() {
        Block block = Block.REGISTRY.getObject(ResourceHelperBC.getResourceRAW(blockName.value));
        if (block == Blocks.AIR) {
            LogHelper.warn("TileChameleon: Could not load state from block - " + blockName.value + " With Meta " + blockMeta.value + " Using fallback state.");
            return Blocks.STONE.getDefaultState();
        }
        else {
            return block.getStateFromMeta(blockMeta.value);
        }
    }

    @Override
    public boolean disableCamo() {
        return false;
    }

    @Override
    public boolean randomBool() {
        return false;
    }

    public void setChameleonState(IBlockState chameleonState) {
        ResourceLocation name = chameleonState.getBlock().getRegistryName();
        if (name == null) {
            blockName.value = "minecraft:stone";
            LogHelper.warn("TileChameleon: The given block dose not seem to be registered " + chameleonState + " Umm..... So.... What?!?! That should not be possible........");
        }
        else {
            blockName.value = name.toString();
        }

        blockMeta.value = (byte) chameleonState.getBlock().getMetaFromState(chameleonState);
        thisBlock.setNewChameleonState(worldObj, pos, worldObj.getBlockState(pos), chameleonState);
        detectAndSendChanges();
        NBTTagCompound compound = new NBTTagCompound();
        compound.setByte("Meta", blockMeta.value);
        compound.setString("Name", blockName.value);
        if (!worldObj.isRemote) {
            sendPacketToClients(new PacketTileMessage(this, (byte) 0, compound, false), syncRange());
        }
    }

    @Override
    public void receivePacketFromServer(PacketTileMessage packet) {
        if (packet.isNBT()) {
            blockMeta.value = packet.compound.getByte("Meta");
            blockName.value = packet.compound.getString("Name");
            updateBlock();
        }
    }

    public boolean attemptSetFromStack(ItemStack stack) {
        if (stack == null || !(stack.getItem() instanceof ItemBlock)) {
            return false;
        }

        Block block = ((ItemBlock) stack.getItem()).getBlock();

        if (block instanceof ChameleonBlock) {
            return false;
        }

        setChameleonState(block.getStateFromMeta(stack.getItemDamage()));
        return true;
    }
}
