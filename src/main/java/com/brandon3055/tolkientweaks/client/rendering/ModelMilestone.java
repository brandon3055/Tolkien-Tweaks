package com.brandon3055.tolkientweaks.client.rendering;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * milestone - brandon3055
 * Created using Tabula 5.1.0
 */
public class ModelMilestone extends ModelBase {
    public ModelRenderer back;
    public ModelRenderer Base;
    public ModelRenderer left;
    public ModelRenderer right;
    public ModelRenderer front;

    public ModelMilestone() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.Base = new ModelRenderer(this, 16, 0);
        this.Base.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.Base.addBox(-5.5F, 0.0F, -5.5F, 11, 8, 11, 0.0F);
        this.back = new ModelRenderer(this, 0, 0);
        this.back.setRotationPoint(0.0F, 16.0F, 4.8F);
        this.back.addBox(-2.0F, -24.0F, -4.0F, 4, 24, 4, 0.0F);
        this.setRotateAngle(back, 0.05235987755982988F, 0.0F, 0.0F);
        this.front = new ModelRenderer(this, 0, 0);
        this.front.setRotationPoint(0.0F, 16.0F, -4.8F);
        this.front.addBox(-2.0F, -24.0F, 0.0F, 4, 24, 4, 0.0F);
        this.setRotateAngle(front, -0.05235987755982988F, 0.0F, 0.0F);
        this.right = new ModelRenderer(this, 0, 0);
        this.right.setRotationPoint(4.8F, 16.0F, 0.0F);
        this.right.addBox(-4.0F, -24.0F, -2.0F, 4, 24, 4, 0.0F);
        this.setRotateAngle(right, 0.0F, 0.0F, -0.05235987755982988F);
        this.left = new ModelRenderer(this, 0, 0);
        this.left.setRotationPoint(-4.8F, 16.0F, 0.0F);
        this.left.addBox(0.0F, -24.0F, -2.0F, 4, 24, 4, 0.0F);
        this.setRotateAngle(left, 0.0F, 0.0F, 0.05235987755982988F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.Base.render(f5);
        this.back.render(f5);
        this.front.render(f5);
        this.right.render(f5);
        this.left.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
