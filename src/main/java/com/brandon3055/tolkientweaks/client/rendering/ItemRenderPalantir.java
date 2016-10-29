package com.brandon3055.tolkientweaks.client.rendering;

import codechicken.lib.render.CCModel;
import codechicken.lib.render.CCOBJParser;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.item.IItemRenderer;
import codechicken.lib.render.state.GlStateManagerHelper;
import codechicken.lib.util.TransformUtils;
import codechicken.lib.vec.Scale;
import com.brandon3055.tolkientweaks.TolkienTweaks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by brandon3055 on 29/9/2015.
 */
public class ItemRenderPalantir implements IItemRenderer, IPerspectiveAwareModel {

	private static CCModel model;
//	public static IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation(TolkienTweaks.MODID.toLowerCase()+":models/palantir.obj"));
	public static ResourceLocation texture = new ResourceLocation(TolkienTweaks.MODID.toLowerCase()+":textures/items/palantir.png");


//	public static CCModelState RING_TRANSFORM;
//
//	static {
//		TRSRTransformation thirdPerson;
//		TRSRTransformation firstPerson;
//		thirdPerson = get(0, 3, 1, 0, 0, 0, 0.55f);
//		firstPerson = get(1.13f, 3.2f, 1.13f, 0, 90, 25, 0.22f);
//		ImmutableMap.Builder<ItemCameraTransforms.TransformType, TRSRTransformation> defaultItemBuilder = ImmutableMap.builder();
//		defaultItemBuilder.put(ItemCameraTransforms.TransformType.GUI, get(0, 0, 0, 90, 0, 0, 0.6f));
//		defaultItemBuilder.put(ItemCameraTransforms.TransformType.GROUND, get(0, 2, 0, 0, 0, 0, 0.3f));
//		defaultItemBuilder.put(ItemCameraTransforms.TransformType.HEAD, get(0, 13, 7, 0, 180, 0, 1));
//		defaultItemBuilder.put(ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, thirdPerson);
//		defaultItemBuilder.put(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, leftify(thirdPerson));
//		defaultItemBuilder.put(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND, firstPerson);
//		defaultItemBuilder.put(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, leftify(firstPerson));
//		RING_TRANSFORM = new CCModelState(defaultItemBuilder.build());
//	}

	public ItemRenderPalantir(){
		Map<String, CCModel> map = CCOBJParser.parseObjModels(new ResourceLocation(TolkienTweaks.MODID.toLowerCase()+":models/palantir.obj")); //Note dont generate the model evey render frame move this to constructor
		model = CCModel.combine(map.values());
		model.apply(new Scale(0.35, 0.35, 0.35));
		model.computeNormals();
	}

	//region Unused
	@Override
	public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
		return new ArrayList<>();
	}

	@Override
	public boolean isAmbientOcclusion() {
		return false;
	}

	@Override
	public boolean isGui3d() {
		return false;
	}

	@Override
	public boolean isBuiltInRenderer() {
		return true;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return null;
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return ItemCameraTransforms.DEFAULT;
	}

	@Override
	public ItemOverrideList getOverrides() {
		return ItemOverrideList.NONE;
	}

	//endregion

	@Override
	public void renderItem(ItemStack item) {
		GlStateManager.pushMatrix();
		GlStateManagerHelper.pushState();

		GlStateManager.scale(0.7, 0.7, 0.7);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1F);
		GlStateManager.translate(0.7, 0.5, 0.7);

		FMLClientHandler.instance().getClient().renderEngine.bindTexture(texture);

		CCRenderState ccrs = CCRenderState.instance();
		ccrs.startDrawing(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION_TEX_NORMAL);
		model.render(ccrs);
		ccrs.draw();

		GlStateManager.popMatrix();

		int explosionTicks = 0;
		if (item.hasTagCompound()) {
			explosionTicks = item.getTagCompound().getInteger("ETicks");
		}

		if (explosionTicks > 0) {
			float motionOffset = 0;//(float) (explosionTicks + Minecraft.getMinecraft().getRenderPartialTicks()) / 200F;//50f;
			float effectScale = (float) (explosionTicks + Minecraft.getMinecraft().getRenderPartialTicks()) / 2F;//50f;;//(BCClientEventHandler.elapsedTicks % 100) / 10F;

			Tessellator tessellator = Tessellator.getInstance();
			VertexBuffer buffer = tessellator.getBuffer();
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
		GlStateManagerHelper.popState();
	}

	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
		return MapWrapper.handlePerspective(this, TransformUtils.DEFAULT_ITEM.getTransforms(), cameraTransformType);
	}

//	@Override
//	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
//		return true;
//	}
//
//	@Override
//	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
//		return true;
//	}
//
//	@Override
//	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
//		GlStateManager.pushMatrix();
//
//		if (type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON)GlStateManager.glTranslated(0.5, 0.5, 0.5);
//
//		GlStateManager.scale(0.7, 0.7, 0.7);
//		GlStateManager.color(1.0, 1.0, 1.0, 1);
//
//		FMLClientHandler.instance().getClient().renderEngine.bindTexture(texture);
//		model.renderAll();
//
//		GlStateManager.glPopMatrix();
//
//
//		int explosionTicks = 0;
//		if (item.hasTagCompound()) explosionTicks = item.getTagCompound().getInteger("ETicks");
//
//		Tessellator tessellator = Tessellator.instance;
//
//		if (explosionTicks > 0)
//		{
//			RenderHelper.disableStandardItemLighting();
//			float f1 = (float)explosionTicks / 50f;
//			float f2 = 0.0F;
//
//			if (f1 > 0.8F)
//			{
//				f2 = (f1 - 0.8F) / 0.2F;
//			}
//
//			Random random = new Random(432L);
//			GlStateManager.glDisable(GlStateManager.GL_TEXTURE_2D);
//			GlStateManager.glShadeModel(GlStateManager.GL_SMOOTH);
//			GlStateManager.glEnable(GlStateManager.GL_BLEND);
//			GlStateManager.glBlendFunc(GlStateManager.GL_SRC_ALPHA, GlStateManager.GL_ONE);
//			GlStateManager.glDisable(GlStateManager.GL_ALPHA_TEST);
//			GlStateManager.glEnable(GlStateManager.GL_CULL_FACE);
//			GlStateManager.glDepthMask(false);
//			GlStateManager.pushMatrix();
//			//GlStateManager.glTranslatef(0.0F, -1.0F, -2.0F);
//
//			for (int i = 0; (float)i < (f1 + f1 * f1) / 2.0F * 60.0F; ++i)
//			{
//				GlStateManager.glRotatef(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
//				GlStateManager.glRotatef(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
//				GlStateManager.glRotatef(random.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
//				GlStateManager.glRotatef(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
//				GlStateManager.glRotatef(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
//				GlStateManager.glRotatef(random.nextFloat() * 360.0F + f1 * 90.0F, 0.0F, 0.0F, 1.0F);
//				tessellator.startDrawing(6);
//				float f3 = random.nextFloat() * 20.0F + 5.0F + f2 * 10.0F;
//				float f4 = random.nextFloat() * 2.0F + 1.0F + f2 * 2.0F;
//				tessellator.setColorRGBA_I(0xFFFFFF, (int)(255.0F * (1.0F - f2/2f)));
//				//tessellator.setColorRGBA_F(1f, 0f, 0f, 1f);
//				tessellator.addVertex(0.0D, 0.0D, 0.0D);
//				tessellator.setColorRGBA_I(0xFF0000, 0);
//				tessellator.addVertex(-0.866D * (double)f4, (double)f3, (double)(-0.5F * f4));
//				tessellator.addVertex(0.866D * (double)f4, (double)f3, (double)(-0.5F * f4));
//				tessellator.addVertex(0.0D, (double)f3, (double)(1.0F * f4));
//				tessellator.addVertex(-0.866D * (double)f4, (double)f3, (double)(-0.5F * f4));
//				tessellator.draw();
//			}
//
//			GlStateManager.glPopMatrix();
//			GlStateManager.glDepthMask(true);
//			GlStateManager.glDisable(GlStateManager.GL_CULL_FACE);
//			GlStateManager.glDisable(GlStateManager.GL_BLEND);
//			GlStateManager.glShadeModel(GlStateManager.GL_FLAT);
//			GlStateManager.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//			GlStateManager.glEnable(GlStateManager.GL_TEXTURE_2D);
//			GlStateManager.glEnable(GlStateManager.GL_ALPHA_TEST);
//			RenderHelper.enableStandardItemLighting();
//		}
//
//	}
}
