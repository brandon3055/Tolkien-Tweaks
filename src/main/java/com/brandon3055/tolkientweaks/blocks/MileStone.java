package com.brandon3055.tolkientweaks.blocks;

import com.brandon3055.tolkientweaks.ModBlocks;
import com.brandon3055.tolkientweaks.TolkienTweaks;
import com.brandon3055.tolkientweaks.tileentity.TileMilestone;
import com.brandon3055.tolkientweaks.utills.LogHelper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by brandon3055 on 3/06/2016.
 */
public class MileStone extends Block{
    public MileStone() {
        super(Material.rock);
        this.setBlockTextureName("stone");
        this.setBlockName(TolkienTweaks.RPREFIX + "milestone");
        this.setBlockUnbreakable();

        GameRegistry.registerBlock(this, "milestone");
    }

    //region Render

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isNormalCube() {
        return false;
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        return AxisAlignedBB.getBoundingBox(0, 0, 0, 0, 0, 0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random)
    {
        TileMilestone tile = world.getTileEntity(x, y, z) instanceof TileMilestone ? (TileMilestone) world.getTileEntity(x, y, z) : null;

        if (tile != null && tile.users.contains(Minecraft.getMinecraft().thePlayer.getCommandSenderName())){
            for (int i = 0; i < 4; i++){
                world.spawnParticle("portal", x + 0.5, y + (random.nextDouble() * 2), z + 0.5,  (random.nextDouble() - 0.5D) * 3.0D, -random.nextDouble(), (random.nextDouble() - 0.5D) * 3.0D);
            }
        }




//        for (int l = x - 2; l <= x + 2; ++l)
//        {
//            for (int i1 = z - 2; i1 <= z + 2; ++i1)
//            {
//                if (l > x - 2 && l < x + 2 && i1 == z - 1)
//                {
//                    i1 = z + 2;
//                }
//
//                if (random.nextInt(16) == 0)
//                {
//                    for (int j1 = y; j1 <= y + 1; ++j1)
//                    {
//                        world.spawnParticle("enchantmenttable", (double) x + 0.5D, (double) y + 2.0D, (double) z + 0.5D, (double) ((float) (l - x) + random.nextFloat()) - 0.5D, (double) ((float) (j1 - y) - random.nextFloat() - 1.0F), (double) ((float) (i1 - z) + random.nextFloat()) - 0.5D);
//                    }
//                }
//            }
//        }
    }

    //endregion

    //region Creation

    @Override
    public boolean hasTileEntity(int metadata) {
        return metadata == 0;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileMilestone();
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase placer, ItemStack p_149689_6_) {
        TileMilestone tile = world.getTileEntity(x, y, z) instanceof TileMilestone ? (TileMilestone) world.getTileEntity(x, y, z) : null;

        if (tile != null){
            tile.x = placer.posX;
            tile.y = placer.posY + 0.5;
            tile.z = placer.posZ;
        }
    }

    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int p_149660_5_, float p_149660_6_, float p_149660_7_, float p_149660_8_, int meta) {
        if (meta == 0){
            world.setBlock(x, y+1, z, ModBlocks.milestone, 1, 2);
        }
        return meta;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block p_149749_5_, int meta) {
        super.breakBlock(world, x, y, z, p_149749_5_, meta);
        if (meta == 0){
            LogHelper.info(0);
            world.setBlockToAir(x, y+1, z);
        }
        else {
            LogHelper.info("Not "+0);
            world.setBlockToAir(x, y - 1, z);
        }
    }

    //endregion

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
        if (world.isRemote){
            return true;
        }
        
        int tileY = world.getBlockMetadata(x, y, z) == 0 ? y : y - 1;

        TileMilestone tile = world.getTileEntity(x, tileY, z) instanceof TileMilestone ? (TileMilestone) world.getTileEntity(x, tileY, z) : null;

        if (tile != null){
            tile.onActivated(player);
        }

        return true;
    }

    @Override
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
        return null;
    }

    @Override
    public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity) {
        return false;
    }

    @Override
    public boolean canDropFromExplosion(Explosion p_149659_1_) {
        return false;
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
        return null;
    }
}