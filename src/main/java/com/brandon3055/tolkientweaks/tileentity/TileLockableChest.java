package com.brandon3055.tolkientweaks.tileentity;

import codechicken.lib.data.MCDataInput;
import codechicken.lib.util.ItemNBTUtils;
import com.brandon3055.brandonscore.blocks.TileBCBase;
import com.brandon3055.brandonscore.lib.datamanager.ManagedInt;
import com.brandon3055.brandonscore.lib.datamanager.ManagedString;
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
public class TileLockableChest extends TileBCBase implements ITickable, IKeyAccessTile {

    public final ManagedString keyCode = register("key_code", new ManagedString("")).syncViaTile().saveToTile().finish();

    public boolean adjacentChestChecked;
    public TileLockableChest adjacentChestZNeg;
    public TileLockableChest adjacentChestXPos;
    public TileLockableChest adjacentChestXNeg;
    public TileLockableChest adjacentChestZPos;
    public float lidAngle;
    public float prevLidAngle;
    public ManagedInt numPlayersUsing = register("num_using", new ManagedInt(0)).syncViaTile().finish();
    private int ticksSinceSync;

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
            TileEntity tileentity = this.world.getTileEntity(blockpos);

            if (tileentity instanceof TileLockableChest) {
                TileLockableChest tileentitychest = (TileLockableChest) tileentity;
                tileentitychest.setNeighbor(this, side.getOpposite());
                return tileentitychest;
            }
        }

        return null;
    }

    private boolean isChestAt(BlockPos posIn) {
        if (this.world == null) {
            return false;
        }
        else {
            Block block = this.world.getBlockState(posIn).getBlock();
            return block instanceof LockableChest;
        }
    }

    @Override
    public void update() {
        super.update();
        this.checkForAdjacentChests();
        int i = this.pos.getX();
        int j = this.pos.getY();
        int k = this.pos.getZ();
        ++this.ticksSinceSync;

        if (!this.world.isRemote && this.numPlayersUsing.value != 0 && (this.ticksSinceSync + i + j + k) % 200 == 0) {
            this.numPlayersUsing.value = 0;
            float f = 5.0F;

            for (EntityPlayer entityplayer : this.world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB((double) ((float) i - 5.0F), (double) ((float) j - 5.0F), (double) ((float) k - 5.0F), (double) ((float) (i + 1) + 5.0F), (double) ((float) (j + 1) + 5.0F), (double) ((float) (k + 1) + 5.0F)))) {
                if (entityplayer.openContainer instanceof ContainerLockableChest) {
                    InventoryLockableChest iinventory = ((ContainerLockableChest) entityplayer.openContainer).lockableInventory;

                    if (iinventory.tile == this || iinventory.tile.getAdjacentChest() == this) {
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

            this.world.playSound((EntityPlayer) null, d1, (double) j + 0.5D, d2, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
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

                this.world.playSound((EntityPlayer) null, d3, (double) j + 0.5D, d0, SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
            }

            if (this.lidAngle < 0.0F) {
                this.lidAngle = 0.0F;
            }
        }
    }

    public void openChest(EntityPlayer player) {
        if (!player.isSpectator()) {
            if (this.numPlayersUsing.value < 0) {
                this.numPlayersUsing.value = 0;
            }

            ++this.numPlayersUsing.value;
            this.world.notifyNeighborsOfStateChange(this.pos, this.getBlockType(), true);
            this.world.notifyNeighborsOfStateChange(this.pos.down(), this.getBlockType(), true);
        }
    }

    public void closeChest(EntityPlayer player) {
        if (!player.isSpectator() && this.getBlockType() instanceof LockableChest) {
            --this.numPlayersUsing.value;
            this.world.notifyNeighborsOfStateChange(this.pos, this.getBlockType(), true);
            this.world.notifyNeighborsOfStateChange(this.pos.down(), this.getBlockType(), true);
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
        this.updateContainingBlockInfo();
        this.checkForAdjacentChests();
    }

    public String getName() {
        return "container.chest";
    }
    

    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new ContainerLockableChest(this, playerInventory, getInventory(playerIn.getHeldItemMainhand()), playerIn);
    }

    public InventoryLockableChest getInventory(ItemStack key)
    {
        return new InventoryLockableChest(this, key);
    }

    @Override
    public boolean canRenderBreaking() {
        return true;
    }

    //region lock

    public TileLockableChest getAdjacentChest() {
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
    public void receivePacketFromClient(MCDataInput data, EntityPlayerMP client, int id) {
        super.receivePacketFromClient(data, client, id);
        if (!client.capabilities.isCreativeMode) {
            return;
        }
        switch (id) {
            case 4:
                keyCode.value = data.readString();
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
        if (!key.isEmpty() && key.getItem() == TTFeatures.key && (((Key) key.getItem()).getKey(key).equals(keyCode.value) || key.getItemDamage() == 1)) {
            if (ItemNBTUtils.hasKey(key, "playerUUID")) {
                String id = ItemNBTHelper.getString(key, "playerUUID", "");
                if (!id.equals(player.getUniqueID().toString())) {
                    player.sendMessage(new TextComponentTranslation("msg.tolkienaddon.keyWrongOwner.txt"));
                    return false;
                }
            }
            else {
                ItemNBTUtils.setString(key, "playerUUID", player.getUniqueID().toString());
            }

            return true;
        }
        if (!key.isEmpty() && key.getItem() == TTFeatures.key) {
            player.sendMessage(new TextComponentTranslation("msg.tolkienaddon.keyWrong.txt"));
        }

        return false;
    }

    @SideOnly(Side.CLIENT)
    public void openGUI() {
        Minecraft.getMinecraft().displayGuiScreen(new GuiKeyAccess(Minecraft.getMinecraft().player, this));
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
