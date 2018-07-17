package com.brandon3055.tolkientweaks.items;

import codechicken.lib.model.ModelRegistryHelper;
import com.brandon3055.brandonscore.registry.Feature;
import com.brandon3055.brandonscore.registry.IRenderOverride;
import com.brandon3055.tolkientweaks.client.rendering.ItemRingRenderer;
import com.brandon3055.tolkientweaks.entity.EntityRing;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Brandon on 13/01/2015.
 */
public class Ring extends Item implements IRenderOverride {

    public static float glow = 0;

    public Ring() {
        this.setMaxStackSize(1);
    }
//
//	@Override
//	public void registerIcons(IIconRegister p_94581_1_) {
//
//	}

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemstack) {
        return new EntityRing(world, location, itemstack);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return newStack == null || oldStack == null || newStack.getItem() != oldStack.getItem() || slotChanged;
    }

    @Override
    public void onUpdate(ItemStack stack, World p_77663_2_, Entity p_77663_3_, int p_77663_4_, boolean p_77663_5_) {
//        LogHelper.info("Update "+stack.hasTagCompound());
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Glow")) {
            if (stack.getTagCompound().getFloat("Glow") > 0f) {
                stack.getTagCompound().setFloat("Glow", stack.getTagCompound().getFloat("Glow") - 0.01f);
                glow = stack.getTagCompound().getFloat("Glow");
                stack.getTagCompound().setInteger("ETicks", 0);
            }
            else {
                stack.setTagCompound(null);
                glow = 0;
            }
        }

        super.onUpdate(stack, p_77663_2_, p_77663_3_, p_77663_4_, p_77663_5_);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerRenderer(Feature feature) {
        ModelRegistryHelper.registerItemRenderer(this, new ItemRingRenderer());
    }

    @Override
    public boolean registerNormal(Feature feature) {
        return false;
    }
}
