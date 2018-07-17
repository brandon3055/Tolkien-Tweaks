package com.brandon3055.tolkientweaks.client.rendering;

import codechicken.lib.render.CCModel;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.OBJParser;
import codechicken.lib.render.item.IItemRenderer;
import codechicken.lib.util.TransformUtils;
import codechicken.lib.vec.Scale;
import com.brandon3055.tolkientweaks.TolkienTweaks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.lwjgl.opengl.GL11;

import java.util.Map;
import java.util.Random;

/**
 * Created by brandon3055 on 29/9/2015.
 */
public class ItemRenderPalantir implements IItemRenderer{

	private static CCModel model;
	public static ResourceLocation texture = new ResourceLocation(TolkienTweaks.MODID.toLowerCase()+":textures/items/palantir.png");

	public ItemRenderPalantir() {
		Map<String, CCModel> map = OBJParser.parseModels(new ResourceLocation(TolkienTweaks.MODID.toLowerCase()+":models/palantir.obj"));
		model = CCModel.combine(map.values());
		model.apply(new Scale(0.35, 0.35, 0.35));
		model.computeNormals();
	}

	//region Unused

	@Override
	public boolean isAmbientOcclusion() {
		return false;
	}

	@Override
	public boolean isGui3d() {
		return false;
	}

	//endregion

	@Override
	public void renderItem(ItemStack stack, ItemCameraTransforms.TransformType transformType) {
		GlStateManager.pushMatrix();

		GlStateManager.scale(0.7, 0.7, 0.7);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1F);
		GlStateManager.translate(0.7, 0.5, 0.7);

		FMLClientHandler.instance().getClient().renderEngine.bindTexture(texture);

		CCRenderState ccrs = CCRenderState.instance();
		ccrs.startDrawing(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION_TEX_NORMAL);
		model.render(ccrs, new Scale(-1));
		ccrs.draw();

		GlStateManager.popMatrix();

		int explosionTicks = 0;
		if (stack.hasTagCompound()) {
			explosionTicks = stack.getTagCompound().getInteger("ETicks");
		}

		if (explosionTicks > 0) {
			float motionOffset = 0;//(float) (explosionTicks + Minecraft.getMinecraft().getRenderPartialTicks()) / 200F;//50f;
			float effectScale = (float) (explosionTicks + Minecraft.getMinecraft().getRenderPartialTicks()) / 2F;//50f;;//(BCClientEventHandler.elapsedTicks % 100) / 10F;

			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder buffer = tessellator.getBuffer();
			RenderHelper.disableStandardItemLighting();
			Random random = new Random(432L);
			GlStateManager.disableTexture2D();
			GlStateManager.shadeModel(7425);
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
			GlStateManager.disableAlpha();
			GlStateManager.enableCull();
			GlStateManager.depthMask(false);
			GlStateManager.pushMatrix();
			GlStateManager.translate(0.5, 0.5, 0.5);

//            (f1 + f1 * f1) / 2.0F *
			float effectProgress = effectScale;
			for (int i = 0; (float) i < (effectProgress + effectProgress * effectProgress) / 2.0F * 60.0F; ++i) {
				effectProgress = -1.F + effectScale;

				GlStateManager.rotate(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
				GlStateManager.rotate(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
				GlStateManager.rotate(random.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
				GlStateManager.rotate(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
				GlStateManager.rotate(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
				GlStateManager.rotate(random.nextFloat() * 360.0F + motionOffset * 90.0F, 0.0F, 0.0F, 1.0F);

				float f2 = random.nextFloat() * 20.0F + 5.0F + effectProgress * 10.0F;
				float f3 = random.nextFloat() * 2.0F + 1.0F + effectProgress * 2.0F;
				effectProgress = 5F;

				buffer.begin(6, DefaultVertexFormats.POSITION_COLOR);
				buffer.pos(0.0D, 0.0D, 0.0D).color(255, 255, 255, (int)(255.0F * (1.0F - effectProgress))).endVertex();
				buffer.pos(-0.866D * (double)f3, (double)f2, (double)(-0.5F * f3)).color(255, 0, 0, 0).endVertex();
				buffer.pos(0.866D * (double)f3, (double)f2, (double)(-0.5F * f3)).color(255, 0, 0, 0).endVertex();
				buffer.pos(0.0D, (double)f2, (double)(1.0F * f3)).color(255, 0, 0, 0).endVertex();
				buffer.pos(-0.866D * (double)f3, (double)f2, (double)(-0.5F * f3)).color(255, 0, 0, 0).endVertex();
				tessellator.draw();
			}

			GlStateManager.popMatrix();
			GlStateManager.depthMask(true);
			GlStateManager.disableCull();
			GlStateManager.disableBlend();
			GlStateManager.shadeModel(7424);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.enableTexture2D();
			GlStateManager.enableAlpha();
			RenderHelper.enableStandardItemLighting();
		}
	}

	@Override
	public IModelState getTransforms() {
		return TransformUtils.DEFAULT_ITEM;
	}
}
