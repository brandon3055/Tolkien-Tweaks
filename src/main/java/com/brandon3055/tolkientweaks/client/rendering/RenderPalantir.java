package com.brandon3055.tolkientweaks.client.rendering;

import com.brandon3055.tolkientweaks.TolkienTweaks;
import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

import java.util.Random;

/**
 * Created by brandon3055 on 29/9/2015.
 */
public class RenderPalantir implements IItemRenderer {

	public static IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation(TolkienTweaks.MODID.toLowerCase()+":models/palantir.obj"));
	public static ResourceLocation texture = new ResourceLocation(TolkienTweaks.MODID.toLowerCase()+":textures/items/palantir.png");

	public RenderPalantir(){}

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		GL11.glPushMatrix();

		if (type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON)GL11.glTranslated(0.5, 0.5, 0.5);

		GL11.glScaled(0.7, 0.7, 0.7);
		GL11.glColor4d(1.0, 1.0, 1.0, 1);

		FMLClientHandler.instance().getClient().renderEngine.bindTexture(texture);
		model.renderAll();

		GL11.glPopMatrix();


		int explosionTicks = 0;
		if (item.hasTagCompound()) explosionTicks = item.getTagCompound().getInteger("ETicks");

		Tessellator tessellator = Tessellator.instance;

		if (explosionTicks > 0)
		{
			RenderHelper.disableStandardItemLighting();
			float f1 = (float)explosionTicks / 50f;
			float f2 = 0.0F;

			if (f1 > 0.8F)
			{
				f2 = (f1 - 0.8F) / 0.2F;
			}

			Random random = new Random(432L);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glShadeModel(GL11.GL_SMOOTH);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glDepthMask(false);
			GL11.glPushMatrix();
			//GL11.glTranslatef(0.0F, -1.0F, -2.0F);

			for (int i = 0; (float)i < (f1 + f1 * f1) / 2.0F * 60.0F; ++i)
			{
				GL11.glRotatef(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(random.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
				GL11.glRotatef(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(random.nextFloat() * 360.0F + f1 * 90.0F, 0.0F, 0.0F, 1.0F);
				tessellator.startDrawing(6);
				float f3 = random.nextFloat() * 20.0F + 5.0F + f2 * 10.0F;
				float f4 = random.nextFloat() * 2.0F + 1.0F + f2 * 2.0F;
				tessellator.setColorRGBA_I(0xFFFFFF, (int)(255.0F * (1.0F - f2/2f)));
				//tessellator.setColorRGBA_F(1f, 0f, 0f, 1f);
				tessellator.addVertex(0.0D, 0.0D, 0.0D);
				tessellator.setColorRGBA_I(0xFF0000, 0);
				tessellator.addVertex(-0.866D * (double)f4, (double)f3, (double)(-0.5F * f4));
				tessellator.addVertex(0.866D * (double)f4, (double)f3, (double)(-0.5F * f4));
				tessellator.addVertex(0.0D, (double)f3, (double)(1.0F * f4));
				tessellator.addVertex(-0.866D * (double)f4, (double)f3, (double)(-0.5F * f4));
				tessellator.draw();
			}

			GL11.glPopMatrix();
			GL11.glDepthMask(true);
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glShadeModel(GL11.GL_FLAT);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			RenderHelper.enableStandardItemLighting();
		}

	}
}
