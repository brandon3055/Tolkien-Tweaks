package com.brandon3055.tolkientweaks.tileentity;

import codechicken.lib.util.ItemNBTUtils;
import com.brandon3055.brandonscore.blocks.TileBCBase;
import com.brandon3055.brandonscore.blocks.TileInventoryBase;
import com.brandon3055.brandonscore.network.PacketTileMessage;
import com.brandon3055.brandonscore.network.wrappers.SyncableInt;
import com.brandon3055.brandonscore.network.wrappers.SyncableString;
import com.brandon3055.brandonscore.utils.ItemNBTHelper;
import com.brandon3055.tolkientweaks.TTFeatures;
import com.brandon3055.tolkientweaks.blocks.LockableChest;
import com.brandon3055.tolkientweaks.client.gui.GuiKeyAccess;
import com.brandon3055.tolkientweaks.container.ContainerLockableChest;
import com.brandon3055.tolkientweaks.container.InventoryLockableChest;
import com.brandon3055.tolkientweaks.items.Key;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

/**
 * Created by brandon3055 on 16/04/2017.
 */
public class TileLockableChest extends TileInventoryBase implements ISidedInventory, ITickable, IKeyAccessTile {

    public final SyncableString keyCode = new SyncableString("", true, false);

    //    private ItemStack[] chestContents = new ItemStack[27];
    public boolean adjacentChestChecked;
    public TileLockableChest adjacentChestZNeg;
    public TileLockableChest adjacentChestXPos;
    public TileLockableChest adjacentChestXNeg;
    public TileLockableChest adjacentChestZPos;
    public float lidAngle;
    public float prevLidAngle;
    public SyncableInt numPlayersUsing = new SyncableInt(0, true, false);
    private int ticksSinceSync;
//    private String customName;

    public TileLockableChest() {
        setInventorySize(27);
        registerSyncableObject(numPlayersUsing, false);
        registerSyncableObject(keyCode, true);
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return this.worldObj.getTileEntity(this.pos) != this ? false : player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public void updateContainingBlockInfo() {
        super.updateContainingBlockInfo();
        this.adjacentChestChecked = false;
    }

    @SuppressWarnings("incomplete-switch")
    private void setNeighbor(TileLockableChest chestTe, EnumFacing side) {
        if (chestTe.isInvalid()) {
            this.adjacentChestChecked = false;
        }
        else if (this.adjacentChestChecked) {
            switch (side) {
                case NORTH:

                    if (this.adjacentChestZNeg != chestTe) {
                        this.adjacentChestChecked = false;
                    }

                    break;
                case SOUTH:

                    if (this.adjacentChestZPos != chestTe) {
                        this.adjacentChestChecked = false;
                    }

                    break;
                case EAST:

                    if (this.adjacentChestXPos != chestTe) {
                        this.adjacentChestChecked = false;
                    }

                    break;
                case WEST:

                    if (this.adjacentChestXNeg != chestTe) {
                        this.adjacentChestChecked = false;
                    }
            }
        }
    }

    public void checkForAdjacentChests() {
        if (!this.adjacentChestChecked) {
            this.adjacentChestChecked = true;
            this.adjacentChestXNeg = this.getAdjacentChest(EnumFacing.WEST);
            this.adjacentChestXPos = this.getAdjacentChest(EnumFacing.EAST);
            this.adjacentChestZNeg = this.getAdjacentChest(EnumFacing.NORTH);
            this.adjacentChestZPos = this.getAdjacentChest(EnumFacing.SOUTH);
        }
    }

    @Nullable
    protected TileLockableChest getAdjacentChest(EnumFacing side) {
        BlockPos blockpos = this.pos.offset(side);

        if (this.isChestAt(blockpos)) {
            TileEntity tileentity = this.worldObj.getTileEntity(blockpos);

            if (tileentity instanceof TileLockableChest) {
                TileLockableChest tileentitychest = (TileLockableChest) tileentity;
                tileentitychest.setNeighbor(this, side.getOpposite());
                return tileentitychest;
            }
        }

        return null;
    }

    private boolean isChestAt(BlockPos posIn) {
        if (this.worldObj == null) {
            return false;
        }
        else {
            Block block = this.worldObj.getBlockState(posIn).getBlock();
            return block instanceof LockableChest;
        }
    }

    @Override
    public void update() {
        detectAndSendChanges();
        this.checkForAdjacentChests();
        int i = this.pos.getX();
        int j = this.pos.getY();
        int k = this.pos.getZ();
        ++this.ticksSinceSync;

        if (!this.worldObj.isRemote && this.numPlayersUsing.value != 0 && (this.ticksSinceSync + i + j + k) % 200 == 0) {
            this.numPlayersUsing.value = 0;
            float f = 5.0F;

            for (EntityPlayer entityplayer : this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB((double) ((float) i - 5.0F), (double) ((float) j - 5.0F), (double) ((float) k - 5.0F), (double) ((float) (i + 1) + 5.0F), (double) ((float) (j + 1) + 5.0F), (double) ((float) (k + 1) + 5.0F)))) {
                if (entityplayer.openContainer instanceof ContainerLockableChest) {
                    IInventory iinventory = ((ContainerLockableChest) entityplayer.openContainer).getLowerChestInventory();

                    if (iinventory == this || iinventory instanceof InventoryLockableChest && ((InventoryLockableChest) iinventory).isPartOfLargeChest(this)) {
                        ++this.numPlayersUsing.value;
                    }
                }
            }
        }

        this.prevLidAngle = this.lidAngle;
        float f1 = 0.1F;

        if (this.numPlayersUsing.value > 0 && this.lidAngle == 0.0F && this.adjacentChestZNeg == null && this.adjacentChestXNeg == null) {
            double d1 = (double) i + 0.5D;
            double d2 = (double) k + 0.5D;

            if (this.adjacentChestZPos != null) {
                d2 += 0.5D;
            }

            if (this.adjacentChestXPos != null) {
                d1 += 0.5D;
            }

            this.worldObj.playSound((EntityPlayer) null, d1, (double) j + 0.5D, d2, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
        }

        if (this.numPlayersUsing.value == 0 && this.lidAngle > 0.0F || this.numPlayersUsing.value > 0 && this.lidAngle < 1.0F) {
            float f2 = this.lidAngle;

            if (this.numPlayersUsing.value > 0) {
                this.lidAngle += 0.1F;
            }
            else {
                this.lidAngle -= 0.1F;
            }

            if (this.lidAngle > 1.0F) {
                this.lidAngle = 1.0F;
            }

            float f3 = 0.5F;

            if (this.lidAngle < 0.5F && f2 >= 0.5F && this.adjacentChestZNeg == null && this.adjacentChestXNeg == null) {
                double d3 = (double) i + 0.5D;
                double d0 = (double) k + 0.5D;

                if (this.adjacentChestZPos != null) {
                    d0 += 0.5D;
                }

                if (this.adjacentChestXPos != null) {
                    d3 += 0.5D;
                }

                this.worldObj.playSound((EntityPlayer) null, d3, (double) j + 0.5D, d0, SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
            }

            if (this.lidAngle < 0.0F) {
                this.lidAngle = 0.0F;
            }
        }
    }

    @Override
    public void openInventory(EntityPlayer player) {
        if (!player.isSpectator()) {
            if (this.numPlayersUsing.value < 0) {
                this.numPlayersUsing.value = 0;
            }

            ++this.numPlayersUsing.value;
//            this.worldObj.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing.value);
            this.worldObj.notifyNeighborsOfStateChange(this.pos, this.getBlockType());
            this.worldObj.notifyNeighborsOfStateChange(this.pos.down(), this.getBlockType());
        }
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        if (!player.isSpectator() && this.getBlockType() instanceof LockableChest) {
            --this.numPlayersUsing.value;
//            this.worldObj.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing.value);
            this.worldObj.notifyNeighborsOfStateChange(this.pos, this.getBlockType());
            this.worldObj.notifyNeighborsOfStateChange(this.pos.down(), this.getBlockType());
        }
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }

    @Override
    public void invalidate() {
        super.invalidate();
        this.updateContainingBlockInfo();
        this.checkForAdjacentChests();
    }

    @Override
    public String getName() {
        return "container.chest";
    }
//
//    @Override
//    public String getGuiID() {
//        return "minecraft:chest";
//    }
//
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new ContainerLockableChest(playerInventory, getInventory(), playerIn);
    }

    @Nullable
    public IInventory getInventory()
    {
        TileEntity tileentity = worldObj.getTileEntity(pos);

        if (!(tileentity instanceof TileLockableChest))
        {
            return null;
        }
        else
        {
            IInventory ilockablecontainer = (TileLockableChest)tileentity;

            if (TTFeatures.lockableChest.isBlocked(worldObj, pos))
            {
                return null;
            }
            else
            {
                for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
                {
                    BlockPos blockpos = pos.offset(enumfacing);
                    Block block = worldObj.getBlockState(blockpos).getBlock();

                    if (block == this.getBlockType())
                    {
                        if (TTFeatures.lockableChest.isBlocked(worldObj, blockpos))
                        {
                            return null;
                        }

                        TileEntity tileentity1 = worldObj.getTileEntity(blockpos);

                        if (tileentity1 instanceof TileLockableChest)
                        {
                            if (enumfacing != EnumFacing.WEST && enumfacing != EnumFacing.NORTH)
                            {
                                ilockablecontainer = new InventoryLockableChest("container.chestDouble", ilockablecontainer, (TileLockableChest)tileentity1);
                            }
                            else
                            {
                                ilockablecontainer = new InventoryLockableChest("container.chestDouble", (TileLockableChest)tileentity1, ilockablecontainer);
                            }
                        }
                    }
                }

                return ilockablecontainer;
            }
        }
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

//    public net.minecraftforge.items.VanillaDoubleChestItemHandler doubleChestHandler;
//
//    @SuppressWarnings("unchecked")
//    @Override
//    public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, net.minecraft.util.EnumFacing facing)
//    {
//        if (capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
//        {
//            if(doubleChestHandler == null || doubleChestHandler.needsRefresh()){
//                doubleChestHandler = net.minecraftforge.items.VanillaDoubleChestItemHandler.get(this);
//            }
//            if (doubleChestHandler != null && doubleChestHandler != net.minecraftforge.items.VanillaDoubleChestItemHandler.NO_ADJACENT_CHESTS_INSTANCE) {
//                return (T) doubleChestHandler;
//            }
//        }
//        return super.getCapability(capability, facing);
//    }
//
//    public net.minecraftforge.items.IItemHandler getSingleChestHandler()
//    {
//        return super.getCapability(net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
//    }


    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return false;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return false;
    }

    @Override
    public boolean canRenderBreaking() {
        return true;
    }

    //region lock

    private TileLockableChest getAdjacentChest() {
        checkForAdjacentChests();
        if (adjacentChestXNeg != null) {
            return adjacentChestXNeg;
        }
        else if (adjacentChestXPos != null) {
            return adjacentChestXPos;
        }
        else if (adjacentChestZNeg != null) {
            return adjacentChestZNeg;
        }
        else if (adjacentChestZPos != null) {
            return adjacentChestZPos;
        }
        return null;
    }

    @Override
    public void receivePacketFromClient(PacketTileMessage packet, EntityPlayerMP client) {
        if (!client.capabilities.isCreativeMode) {
            return;
        }
        switch (packet.getIndex()) {
            case 4:
                keyCode.value = packet.stringValue;
                TileLockableChest adj = getAdjacentChest();
                if (adj != null) {
                    adj.keyCode.value = keyCode.value;
                }
                break;
        }
        dirtyBlock();
        updateBlock();
    }

    public boolean isKeyValid(ItemStack key, EntityPlayer player) {
        if (key != null && key.getItem() == TTFeatures.key && (((Key) key.getItem()).getKey(key).equals(keyCode.value) || key.getItemDamage() == 1)) {
            if (ItemNBTUtils.hasKey(key, "playerUUID")) {
                String id = ItemNBTHelper.getString(key, "playerUUID", "");
                if (!id.equals(player.getUniqueID().toString())) {
                    player.addChatComponentMessage(new TextComponentTranslation("msg.tolkienaddon.keyWrongOwner.txt"));
                    return false;
                }
            }
            else {
                ItemNBTUtils.setString(key, "playerUUID", player.getUniqueID().toString());
            }

            return true;
        }
        if (key != null && key.getItem() == TTFeatures.key) {
            player.addChatComponentMessage(new TextComponentTranslation("msg.tolkienaddon.keyWrong.txt"));
        }

        return false;
    }

    @SideOnly(Side.CLIENT)
    public void openGUI() {
        Minecraft.getMinecraft().displayGuiScreen(new GuiKeyAccess(Minecraft.getMinecraft().thePlayer, this));
    }

    //region KeyAccess

    @Override
    public TileBCBase getTile() {
        return this;
    }

    @Override
    public boolean hasCK() {
        return false;
    }

    @Override
    public boolean consumeKey() {
        return false;
    }

    @Override
    public boolean hasMode() {
        return false;
    }

    @Override
    public TileKeyStone.Mode mode() {
        return null;
    }

    @Override
    public String getCode() {
        return keyCode.value;
    }

    @Override
    public boolean hasDelay() {
        return false;
    }

    @Override
    public int getDelay() {
        return 0;
    }

    //endregion

    //endregion
}
