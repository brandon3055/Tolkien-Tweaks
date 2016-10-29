package com.brandon3055.tolkientweaks.client.rendering;

import codechicken.lib.render.CCModelState;
import codechicken.lib.render.item.IItemRenderer;
import codechicken.lib.render.state.GlStateManagerHelper;
import com.brandon3055.tolkientweaks.items.Ring;
import com.google.common.collect.ImmutableMap;
import lotr.client.model.LOTRModelPortal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
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
import net.minecraftforge.common.model.TRSRTransformation;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static codechicken.lib.util.TransformUtils.get;
import static codechicken.lib.util.TransformUtils.leftify;

/**
 * Created by Brandon on 13/01/2015.
 */
public class ItemRingRenderer implements IItemRenderer, IPerspectiveAwareModel {

    public static ResourceLocation ringTexture = new ResourceLocation("lotr:misc/portal.png");
    public static ResourceLocation writingTexture = new ResourceLocation("lotr:misc/portal_writing.png");
    public static ModelBase ringModel = new LOTRModelPortal(0);
    public static ModelBase writingModelInner = new LOTRModelPortal(1);

    public static CCModelState RING_TRANSFORM;

    static {
        TRSRTransformation thirdPerson;
        TRSRTransformation firstPerson;
        thirdPerson = get(0, 3, 1, 0, 0, 0, 0.55f);
        firstPerson = get(1.13f, 3.2f, 1.13f, 0, 90, 25, 0.22f);
        ImmutableMap.Builder<ItemCameraTransforms.TransformType, TRSRTransformation> defaultItemBuilder = ImmutableMap.builder();
        defaultItemBuilder.put(ItemCameraTransforms.TransformType.GUI, get(0, 0, 0, 90, 0, 0, 0.6f));
        defaultItemBuilder.put(ItemCameraTransforms.TransformType.GROUND, get(0, 2, 0, 0, 0, 0, 0.3f));
        defaultItemBuilder.put(ItemCameraTransforms.TransformType.HEAD, get(0, 13, 7, 0, 180, 0, 1));
        defaultItemBuilder.put(ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, thirdPerson);
        defaultItemBuilder.put(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, leftify(thirdPerson));
        defaultItemBuilder.put(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND, firstPerson);
        defaultItemBuilder.put(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, leftify(firstPerson));
        RING_TRANSFORM = new CCModelState(defaultItemBuilder.build());
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
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        return MapWrapper.handlePerspective(this, RING_TRANSFORM.getTransforms(), cameraTransformType);
    }

    @Override
    public void renderItem(ItemStack item) {
        float glow = 0f;
        int explosionTicks = 0;
        if (item.hasTagCompound()) {
            glow = Math.min(1f, Ring.glow);
            explosionTicks = item.getTagCompound().getInteger("ETicks");
        }

//        explosionTicks = BCClientEventHandler.elapsedTicks % 200;
//        glow = 1F;

        GlStateManager.pushMatrix();
        GlStateManagerHelper.pushState();
        GlStateManager.disableCull();
        GlStateManager.translate(0.5, 0.5, 0.5);
        GlStateManager.glNormal3f(0.0F, 0.0F, 0.0F);
        GlStateManager.scale(1.0F, -1.0F, 1.0F);

        GlStateManager.scale(0.25F, 0.25F, 0.25F);

        Minecraft.getMinecraft().renderEngine.bindTexture(ringTexture);
        float scale = 0.0625F;
        ringModel.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, scale);
        GlStateManager.disableLighting();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableBlend();


        GlStateManager.color(1.0F, 1.0F, 1.0F, glow);

        Minecraft.getMinecraft().renderEngine.bindTexture(writingTexture);
        writingModelInner.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, scale * 0.9F);
        GlStateManager.enableLighting();
        GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();


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
}
