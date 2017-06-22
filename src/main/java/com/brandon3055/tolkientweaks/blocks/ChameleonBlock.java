package com.brandon3055.tolkientweaks.blocks;

import codechicken.lib.block.property.unlisted.UnlistedBooleanProperty;
import codechicken.lib.model.ModelRegistryHelper;
import com.brandon3055.brandonscore.blocks.BlockBCore;
import com.brandon3055.brandonscore.config.Feature;
import com.brandon3055.brandonscore.config.ICustomRender;
import com.brandon3055.tolkientweaks.client.UnlistedStateProperty;
import com.brandon3055.tolkientweaks.client.rendering.model.ModelChameleon;
import com.brandon3055.tolkientweaks.tileentity.IChameleonStateProvider;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by brandon3055 on 28/10/2016.
 *
 */
public abstract class ChameleonBlock<T extends TileEntity & IChameleonStateProvider> extends BlockBCore implements ICustomRender, ITileEntityProvider {

    public static final UnlistedStateProperty TARGET_BLOCK_STATE = new UnlistedStateProperty("target_state");//Contains the state of the block who's model this block is trying to emulate
    public static final UnlistedBooleanProperty DISABLE_CAMO = new UnlistedBooleanProperty("disable_camo");
    //Because i need a random bool for stuff and things... This isnt exactly a "polished" abstract block.
    public static final UnlistedBooleanProperty RANDOM_BOOL = new UnlistedBooleanProperty("random_bool");
    public static final PropertyBool FULL_CUBE = PropertyBool.create("full_cube");

    public ChameleonBlock(Material material) {
        super(material);
    }

    /**
     * Don't forget to call super(); if you create a new constructor because this is where the default state is set.
     */
    public ChameleonBlock() {
//        setDefaultState(getDefaultState().withProperty(FULL_CUBE, false));
    }

    //region Normal Block & BlockState Stuff

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FULL_CUBE, meta == 1);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FULL_CUBE) ? 1 : 0;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return super.getActualState(state, worldIn, pos);//super.getActualState(state, worldIn, pos).withProperty(FULL_CUBE, true);
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return state.getValue(FULL_CUBE);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return state.getValue(FULL_CUBE);
    }

    //endregion

    //region Extender BlockState

    @Override
    protected BlockStateContainer createBlockState() {
        return new ExtendedBlockState(this, new IProperty[] {FULL_CUBE}, new IUnlistedProperty[] {TARGET_BLOCK_STATE, DISABLE_CAMO, RANDOM_BOOL});
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof IChameleonStateProvider) {
            return ((IExtendedBlockState)state).withProperty(TARGET_BLOCK_STATE, ((IChameleonStateProvider) tile).getChameleonBlockState()).withProperty(DISABLE_CAMO, ((IChameleonStateProvider) tile).disableCamo()).withProperty(RANDOM_BOOL, ((IChameleonStateProvider) tile).randomBool());
        }

        return super.getExtendedState(state, world, pos);
    }

    //endregion

    //region IChameleonStateProvider

    @Override
    public final TileEntity createNewTileEntity(World worldIn, int meta) {
        return createChameleonTile(worldIn, meta);
    }

    public abstract T createChameleonTile(World worldIn, int meta);

    /**
     * This method should be called every time the chameleon state is changes to update the isFullCube property.
     * @param world The world.
     * @param pos The block position.
     * @param state This block's state.
     * @param newChameleonState the new chameleon state.
     */
    public void setNewChameleonState(World world, BlockPos pos, IBlockState state, IBlockState newChameleonState) {
        world.setBlockState(pos, state.withProperty(FULL_CUBE, newChameleonState.isFullCube() && newChameleonState.isOpaqueCube()));
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    //endregion

    //region Render Registry

    @SideOnly(Side.CLIENT)
    @Override
    public void registerRenderer(Feature feature) {
        StateMap deviceStateMap = new StateMap.Builder().ignore(FULL_CUBE).build();
        ModelLoader.setCustomStateMapper(this, deviceStateMap);
        ModelRegistryHelper.register(new ModelResourceLocation(getRegistryName(), "normal"), new ModelChameleon());
    }

    @Override
    public boolean registerNormal(Feature feature) {
        return true;
    }

    //endregion

    @SideOnly(Side.CLIENT)
    public List<BakedQuad> getQuadOverrides(@Nullable IBlockState state, @Nullable EnumFacing side) {
        return null;
    }
}
