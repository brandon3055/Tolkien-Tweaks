package com.brandon3055.tolkientweaks.client.rendering;

import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

/**
 * Created by brandon3055 on 23/4/2016.
 */
public class RenderEntityBackpack extends RendererLivingEntity {

    public static ResourceLocation texture = new ResourceLocation("tolkientweaks:textures/model/backpack.png");

    public RenderEntityBackpack() {
        super(new ModelBackpack(), 1.0F);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
        return texture;
    }

    @Override
    protected void passSpecialRender(EntityLivingBase p_77033_1_, double p_77033_2_, double p_77033_4_, double p_77033_6_) {
        //super.passSpecialRender(p_77033_1_, p_77033_2_, p_77033_4_, p_77033_6_);
    }
}
