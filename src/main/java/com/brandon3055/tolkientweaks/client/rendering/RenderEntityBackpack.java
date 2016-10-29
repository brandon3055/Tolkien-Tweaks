package com.brandon3055.tolkientweaks.client.rendering;


import com.brandon3055.tolkientweaks.entity.EntityBackpack;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

/**
 * Created by brandon3055 on 23/4/2016.
 */
public class RenderEntityBackpack extends RenderLiving<EntityBackpack> {

    public static ResourceLocation texture = new ResourceLocation("tolkientweaks:textures/model/backpack.png");

    public RenderEntityBackpack(RenderManager rendermanagerIn) {
        super(rendermanagerIn, new ModelBackpack(), 1.0F);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityBackpack p_110775_1_) {
        return texture;
    }

    @Override
    public void renderMultipass(EntityBackpack p_188300_1_, double p_188300_2_, double p_188300_4_, double p_188300_6_, float p_188300_8_, float p_188300_9_) {
        super.renderMultipass(p_188300_1_, p_188300_2_, p_188300_4_, p_188300_6_, p_188300_8_, p_188300_9_);
    }


}
