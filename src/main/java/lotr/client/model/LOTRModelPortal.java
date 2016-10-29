package lotr.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class LOTRModelPortal extends ModelBase {
    private ModelRenderer[] ringParts = new ModelRenderer[60];

    public LOTRModelPortal(int i) {
        for (int j = 0; j < 60; j++) {
            if (i == 0) {
                this.ringParts[j] = new ModelRenderer(this, 0, 0).setTextureSize(64, 32);
                this.ringParts[j].addBox(-2.0F, -3.5F, -38.0F, 4, 7, 3);
            }
            else {
                this.ringParts[j] = new ModelRenderer(this, j % 30 * 8, 0).setTextureSize(240, 5);
                this.ringParts[j].mirror = true;
                this.ringParts[j].addBox(-2.0F, -2.5F, -38.0F, 4, 5, 0);
            }
            this.ringParts[j].setRotationPoint(0.0F, 0.0F, 0.0F);
            this.ringParts[j].rotateAngleY = (j * 6.0F / 180.0F * 3.141593F);
        }
    }

    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        for (int i = 0; i < 60; i++) {
            this.ringParts[i].render(scale);
        }
    }
}