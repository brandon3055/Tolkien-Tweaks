package com.brandon3055.tolkientweaks.blocks;

import com.brandon3055.brandonscore.blocks.BlockBCore;
import com.brandon3055.brandonscore.config.Feature;
import com.brandon3055.brandonscore.config.ICustomRender;
import com.brandon3055.tolkientweaks.TTFeatures;
import com.brandon3055.tolkientweaks.client.rendering.RenderTileMilestone;
import com.brandon3055.tolkientweaks.tileentity.TileMilestone;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by brandon3055 on 3/06/2016.
 */
public class MileStone extends BlockBCore implements ITileEntityProvider, ICustomRender {

    public static final PropertyBool IS_SLAVE_BLOCK = PropertyBool.create("is_slave");

    public MileStone() {
        setDefaultState(blockState.getBaseState().withProperty(IS_SLAVE_BLOCK, false));
        this.setBlockUnbreakable();
        setIsFullCube(false);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(IS_SLAVE_BLOCK, meta == 1);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(IS_SLAVE_BLOCK) ? 1 : 0;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, IS_SLAVE_BLOCK);
    }

    //region Render

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }

    @Override
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        TileMilestone tile = world.getTileEntity(pos) instanceof TileMilestone ? (TileMilestone) world.getTileEntity(pos) : null;

        if (tile != null && tile.users.contains(Minecraft.getMinecraft().thePlayer.getName())){
            for (int i = 0; i < 4; i++){
                world.spawnParticle(EnumParticleTypes.PORTAL, pos.getX() + 0.5, pos.getY() + (rand.nextDouble() * 2), pos.getZ() + 0.5,  (rand.nextDouble() - 0.5D) * 3.0D, -rand.nextDouble(), (rand.nextDouble() - 0.5D) * 3.0D);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerRenderer(Feature feature) {
        ClientRegistry.bindTileEntitySpecialRenderer(TileMilestone.class, new RenderTileMilestone());
    }

    @Override
    public boolean registerNormal(Feature feature) {
        return true;
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
        return new AxisAlignedBB(0, 0, 0, 0, 0, 0);
    }

    //endregion

    //region Creation

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return metadata == 0 ? new TileMilestone() : null;
    }

    @Override
    public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        if (meta == 0){
            world.setBlockState(pos.add(0, 1, 0), TTFeatures.milestone.getDefaultState().withProperty(IS_SLAVE_BLOCK, true));
        }
        return super.onBlockPlaced(world, pos, facing, hitX, hitY, hitZ, meta, placer);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileMilestone tile = world.getTileEntity(pos) instanceof TileMilestone ? (TileMilestone) world.getTileEntity(pos) : null;

        if (tile != null){
            tile.milestonePos.vec.set(placer.posX, placer.posY + 0.5, placer.posZ);
            tile.markDirty();
        }
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        super.breakBlock(world, pos, state);
        if (state.getValue(IS_SLAVE_BLOCK)){
            world.setBlockToAir(pos.add(0, -1, 0));
        }
        else {
            world.setBlockToAir(pos.add(0, 1, 0));
        }
    }

    //endregion

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote){
            return true;
        }

        BlockPos masterPos = state.getValue(IS_SLAVE_BLOCK) ? pos.add(0, -1, 0) : pos;
        TileMilestone tile = world.getTileEntity(masterPos) instanceof TileMilestone ? (TileMilestone) world.getTileEntity(masterPos) : null;

        if (tile != null){
            tile.onActivated(player);
        }

        return true;
    }

    @Nullable
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        return Collections.emptyList();
    }

    @Override
    public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
        return false;
    }

    @Override
    public boolean canDropFromExplosion(Explosion p_149659_1_) {
        return false;
    }
}