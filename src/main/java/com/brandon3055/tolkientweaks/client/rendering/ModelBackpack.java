package com.brandon3055.tolkientweaks.client.rendering;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

/**
 * Created by brandon3055 on 23/4/2016.
 */
public class ModelBackpack extends ModelBase {
    public ModelRenderer bagTop;
    public ModelRenderer bagMain;
    public ModelRenderer pocketLeft;
    public ModelRenderer pocketRight;
    public ModelRenderer pocketFront;
    public ModelRenderer ledgeFront1;
    public ModelRenderer ledgeFront2;
    public ModelRenderer ledgeFront3;
    public ModelRenderer stringTopLeft;
    public ModelRenderer stringTopRight;
    public ModelRenderer stringBackLeft;
    public ModelRenderer stringBackRight;
    public ModelRenderer stringBottomLeft;
    public ModelRenderer stringBottomRight;

    public ModelBackpack() {
        this(0.0F);
    }

    public ModelBackpack(float par1) {
        this(par1, 0.0F, 64, 32);
    }

    public ModelBackpack(float enlargement, float yShift, int textureWidth, int textureHeight) {
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.bagTop = new ModelRenderer(this, 44, 0);
        this.bagTop.addBox(-3.0F, -0.5F + yShift, 2.0F, 6, 1, 4, enlargement);
        this.setRotation(this.bagTop, 0.0F, 0.0F, 0.0F);
        this.bagMain = new ModelRenderer(this, 42, 6);
        this.bagMain.addBox(-3.5F, 0.0F + yShift, 2.0F, 7, 10, 4, enlargement);
        this.setRotation(this.bagMain, 0.0F, 0.0F, 0.0F);
        this.pocketLeft = new ModelRenderer(this, 33, 5);
        this.pocketLeft.addBox(3.5F, 5.0F + yShift, 2.5F, 1, 4, 3, enlargement);
        this.setRotation(this.pocketLeft, 0.0F, 0.0F, 0.0F);
        this.pocketRight = new ModelRenderer(this, 33, 13);
        this.pocketRight.addBox(-4.5F, 4.7F + yShift, 2.5F, 1, 4, 3, enlargement);
        this.setRotation(this.pocketRight, 0.0F, 0.0F, 0.0F);
        this.pocketFront = new ModelRenderer(this, 15, 27);
        this.pocketFront.addBox(-2.0F, 4.7F + yShift, 6.2F, 4, 4, 1, enlargement);
        this.setRotation(this.pocketFront, 0.0F, 0.0F, 0.0F);
        this.ledgeFront1 = new ModelRenderer(this, 0, 23);
        this.ledgeFront1.addBox(-3.0F, 1.0F + yShift, 5.3F, 6, 8, 1, enlargement);
        this.setRotation(this.ledgeFront1, 0.0F, 0.0F, 0.0F);
        this.ledgeFront2 = new ModelRenderer(this, 1, 20);
        this.ledgeFront2.addBox(-2.0F, 0.6F + yShift, 5.3F, 4, 1, 1, enlargement);
        this.setRotation(this.ledgeFront2, 0.0F, 0.0F, 0.0F);
        this.ledgeFront3 = new ModelRenderer(this, 1, 17);
        this.ledgeFront3.addBox(-2.0F, 8.5F + yShift, 5.3F, 4, 1, 1, enlargement);
        this.setRotation(this.ledgeFront3, 0.0F, 0.0F, 0.0F);
        this.stringTopLeft = new ModelRenderer(this, 54, 21);
        this.stringTopLeft.addBox(2.5F, -0.1F + yShift, -2.0F, 1, 0, 4, enlargement);
        this.setRotation(this.stringTopLeft, 0.0F, 0.0F, 0.0F);
        this.stringTopRight = new ModelRenderer(this, 41, 21);
        this.stringTopRight.addBox(-3.5F, -0.1F + yShift, -2.0F, 1, 0, 4, enlargement);
        this.setRotation(this.stringTopRight, 0.0F, 0.0F, 0.0F);
        this.stringBackLeft = new ModelRenderer(this, 62, 21);
        this.stringBackLeft.addBox(2.5F, -0.1F + yShift, -2.1F, 1, 10, 0, enlargement);
        this.setRotation(this.stringBackLeft, 0.0F, 0.0F, 0.0F);
        this.stringBackRight = new ModelRenderer(this, 49, 21);
        this.stringBackRight.addBox(-3.5F, -0.1F + yShift, -2.1F, 1, 10, 0, enlargement);
        this.setRotation(this.stringBackRight, 0.0F, 0.0F, 0.0F);
        this.stringBottomLeft = new ModelRenderer(this, 54, 27);
        this.stringBottomLeft.addBox(2.5F, 10.0F + yShift, -2.0F, 1, 0, 5, enlargement);
        this.setRotation(this.stringBottomLeft, 0.0F, 0.0F, 0.0F);
        this.stringBottomRight = new ModelRenderer(this, 41, 27);
        this.stringBottomRight.addBox(-3.5F, 10.0F + yShift, -2.0F, 1, 0, 5, enlargement);
        this.setRotation(this.stringBottomRight, 0.0F, 0.0F, 0.0F);
        this.bagMain.addChild(this.bagTop);
        this.bagMain.addChild(this.pocketLeft);
        this.bagMain.addChild(this.pocketRight);
        this.bagMain.addChild(this.pocketFront);
        this.bagMain.addChild(this.ledgeFront1);
        this.bagMain.addChild(this.ledgeFront2);
        this.bagMain.addChild(this.ledgeFront3);
        this.bagMain.addChild(this.stringTopLeft);
        this.bagMain.addChild(this.stringTopRight);
        this.bagMain.addChild(this.stringBackLeft);
        this.bagMain.addChild(this.stringBackRight);
        this.bagMain.addChild(this.stringBottomLeft);
        this.bagMain.addChild(this.stringBottomRight);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        GL11.glPushMatrix();
        GL11.glTranslated(0, 0.85, 0);
        this.bagMain.render(f5);
        GL11.glPopMatrix();
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        if (entity != null && entity.isSneaking()) {
            this.bagMain.rotateAngleX = 0.5F;
        } else {
            this.bagMain.rotateAngleX = 0.0F;
        }

    }
}