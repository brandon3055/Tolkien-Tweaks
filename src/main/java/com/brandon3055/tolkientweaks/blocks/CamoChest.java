package com.brandon3055.tolkientweaks.blocks;

import codechicken.lib.model.ModelRegistryHelper;
import com.brandon3055.brandonscore.blocks.BlockBCore;
import com.brandon3055.brandonscore.registry.Feature;
import com.brandon3055.brandonscore.registry.IRenderOverride;
import com.brandon3055.tolkientweaks.TolkienTweaks;
import com.brandon3055.tolkientweaks.client.gui.GuiHandler;
import com.brandon3055.tolkientweaks.client.rendering.CustomBlockItemRenderer;
import com.brandon3055.tolkientweaks.client.rendering.RenderTileCamoChest;
import com.brandon3055.tolkientweaks.tileentity.TileCamoChest;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Brandon on 8/03/2015.
 */
public class CamoChest extends BlockBCore implements ITileEntityProvider, IRenderOverride {
//	public static IIcon icon_side;

    private AxisAlignedBB AABB = new AxisAlignedBB(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);

    public CamoChest() {
        this.setHardness(2.5F);
        this.setResistance(5F);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB;
    }

    @Override
    public boolean uberIsBlockFullCube() {
        return false;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileCamoChest) {
            if (player.isCreative() && ((TileCamoChest) tile).attemptSetFromStack(player.getHeldItem(hand))) {
                return true;
            }
            else if (!world.isRemote) {
                player.openGui(TolkienTweaks.instance, GuiHandler.ID_CAMO_CHEST, world, pos.getX(), pos.getY(), pos.getZ());
            }
        }

        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileCamoChest();
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerRenderer(Feature feature) {
        ClientRegistry.bindTileEntitySpecialRenderer(TileCamoChest.class, new RenderTileCamoChest());
        ModelRegistryHelper.registerItemRenderer(Item.getItemFromBlock(this), new CustomBlockItemRenderer(this));
    }

    @Override
    public boolean registerNormal(Feature feature) {
        return false;
    }
}
