package com.brandon3055.tolkientweaks.tileentity;

import codechicken.lib.data.MCDataInput;
import com.brandon3055.brandonscore.blocks.TileBCBase;
import com.brandon3055.brandonscore.client.ResourceHelperBC;
import com.brandon3055.brandonscore.lib.datamanager.ManagedByte;
import com.brandon3055.brandonscore.lib.datamanager.ManagedString;
import com.brandon3055.tolkientweaks.blocks.ChameleonBlock;
import com.brandon3055.tolkientweaks.utils.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Created by brandon3055 on 28/10/2016.
 */
public class TileChameleon extends TileBCBase implements IChameleonStateProvider {

    private final ManagedString blockName = register("block_name", new ManagedString("minecraft:stone")).syncViaTile().saveToTile().saveToItem().finish();
    private final ManagedByte blockMeta = register("block_meta", new ManagedByte((byte) 0)).syncViaTile().saveToTile().saveToItem().finish();
    private ChameleonBlock thisBlock;
    private IBlockState chameleonState = null;
    private boolean usingFallbackState = true;

    public TileChameleon(ChameleonBlock thisBlock) {
        this.thisBlock = thisBlock;
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
        thisBlock.setNewChameleonState(world, pos, world.getBlockState(pos), chameleonState);
        dataManager.detectAndSendChanges();

        if (!world.isRemote) {
            sendPacketToClient(mcDataOutput -> mcDataOutput.writeByte(blockMeta.value).writeString(blockName.value), 0).sendToChunk(this);
        }
    }

    @Override
    public void receivePacketFromServer(MCDataInput data, int id) {
        if (id == 0) {
            blockMeta.value = data.readByte();
            blockName.value = data.readString();
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
