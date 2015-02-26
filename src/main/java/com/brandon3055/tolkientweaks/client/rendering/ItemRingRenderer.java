package com.brandon3055.tolkientweaks.client.rendering;

import lotr.client.model.LOTRModelPortal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

import java.util.Random;

/**
 * Created by Brandon on 13/01/2015.
 */
public class ItemRingRenderer implements IItemRenderer {

	public static ResourceLocation ringTexture = new ResourceLocation("lotr:misc/portal.png");
	public static ResourceLocation writingTexture = new ResourceLocation("lotr:misc/portal_writing.png");
	public static ModelBase ringModel = new LOTRModelPortal(0);
	public static ModelBase writingModelInner = new LOTRModelPortal(1);


	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return true;//type == ItemRenderType.ENTITY;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return type != ItemRenderType.ENTITY;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

		float glow = 0f;
		int explosionTicks = 0;
		if (item.hasTagCompound()){
			glow = Math.min(1f, item.getTagCompound().getFloat("Glow"));
			explosionTicks = item.getTagCompound().getInteger("ETicks");
		}


		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glNormal3f(0.0F, 0.0F, 0.0F);
		GL11.glScalef(1.0F, -1.0F, 1.0F);
		float scailModifier = 0.25F;

		if (type == ItemRenderType.EQUIPPED){
			scailModifier = 0.1f;
			GL11.glRotatef(15f, -1f, 0f, 1f);
			GL11.glTranslated(0.45D, -0.8D, 0.45D);
		}
		else if (type == ItemRenderType.EQUIPPED_FIRST_PERSON){
			scailModifier = 0.1f;
			GL11.glRotatef(15f, -1f, 0f, -1f);
			GL11.glTranslated(0.45D, -0.8D, 0.45D);
		}

		GL11.glScalef(scailModifier, scailModifier, scailModifier);


		Minecraft.getMinecraft().renderEngine.bindTexture(ringTexture);
		float scale = 0.0625F;
		ringModel.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, scale);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_BLEND);

		GL11.glColor4f(1.0F, 1.0F, 1.0F, glow);

		Minecraft.getMinecraft().renderEngine.bindTexture(writingTexture);
		writingModelInner.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, scale * 0.9F);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();



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
				tessellator.setColorRGBA_I(16777215, (int)(255.0F * (1.0F - f2/2f)));
				//tessellator.setColorRGBA_F(1f, 0f, 0f, 1f);
				tessellator.addVertex(0.0D, 0.0D, 0.0D);
				tessellator.setColorRGBA_I(16711935, 0);
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
