package com.brandon3055.tolkientweaks.client.rendering;

import com.brandon3055.brandonscore.client.ResourceHelperBC;
import com.brandon3055.tolkientweaks.tileentity.TileMilestone;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 * Created by Brandon on 8/03/2015.
 */
public class RenderTileMilestone extends TileEntitySpecialRenderer<TileMilestone>
{

    public static ModelMilestone model = new ModelMilestone();
    public static ResourceLocation texture = new ResourceLocation("tolkientweaks:textures/blocks/milestone.png");

    @Override
    public void renderTileEntityAt(TileMilestone te, double x, double y, double z, float partialTick, int destroyStage) {
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y + 1.79, z + 0.5);

        GL11.glScalef(1.2F, 1.2F, 1.2F);
        GL11.glRotated(180, 0, 0, 1);
        bindTexture(texture);

        boolean active = te.users.contains(Minecraft.getMinecraft().thePlayer.getName());
//        LogHelper.info(te.users+" "+te.getPos());

        model.render(null, 0, 0, 0, 0, 0, 1F/16F);

        FontRenderer fr = Minecraft.getMinecraft().standardGalacticFontRenderer;

        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
        double scale = 1D / 28D;
        GL11.glScaled(scale, scale, scale);
        GL11.glRotated(90, 0, 0, 1);

        if (active){
            GL11.glDisable(GL11.GL_LIGHTING);
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 200F, 200F);
        }

        GL11.glNormal3f(0F, 0F, -0.0111F);
        GL11.glDepthMask(false);

        for (int i = 0; i < 4; i++)
        {
            GL11.glPushMatrix();
            GL11.glRotated(i * 90, 1, 0, 0);
            GL11.glRotatef(-3, 0, 1, 0);
            GL11.glTranslated(2, -3.5, -10.2);

            int colour = active ? 0xFFFFFF : 0x404040;

            fr.drawString("TheOneRing", 0, 0, colour);

            GL11.glPopMatrix();
        }

        GL11.glDepthMask(true);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glTranslated(x + 0.5, y + 2, z + 0.5);
        GL11.glColor4f(1F, 1F, 1F, 1F);
        GL11.glScalef(1F, -1F, -1F);
        GL11.glRotated(0, 0, 1, 0);

        ResourceHelperBC.bindTexture(ResourceHelperBC.getResourceRAW("draconicevolution:textures/blocks/dislocator_pedestal.png"));//todo

        drawNameString(te, te.markerName.value.replace("_", " "), 0, partialTick);

        GL11.glPopMatrix();

        if (active){
            GL11.glEnable(GL11.GL_LIGHTING);
        }
    }

    private void drawNameString(TileEntity tileentity, String name, float rotation, float f) {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

        if (name.isEmpty()) {
            return;
        }


        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;//RenderManager.instance.getFontRenderer();
        Tessellator tess = Tessellator.getInstance();

        GL11.glPushMatrix();
        GL11.glScalef(0.02f, 0.02f, 0.02f);
        GL11.glRotated(180, 0, 1, 0);
        GL11.glTranslated(0, -40, 0);

        BlockPos tilePos = tileentity.getPos();

        double xDiff = player.posX - (tilePos.getX() + 0.5);
        double yDiff = player.posY - (tilePos.getY() + 0.5);
        double zDiff = player.posZ - (tilePos.getZ() + 0.5);
        double yawAngle = Math.toDegrees(Math.atan2(zDiff, xDiff));
        double pitchAngle = 0;

        GL11.glRotated(yawAngle + 90 - rotation, 0, 1, 0);
        GL11.glRotated(-pitchAngle, 1, 0, 0);

        int xmin = -1 - fontRenderer.getStringWidth(name) / 2;
        int xmax = 1 + fontRenderer.getStringWidth(name) / 2;
        int ymin = -1;
        int ymax = fontRenderer.FONT_HEIGHT;

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(0f, 0f, 0f, 0.5f);


        VertexBuffer buffer = tess.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

        buffer.pos(xmin, ymax, 0).tex(xmin / 64, 1);
        buffer.pos(xmax, ymax, 0).tex(xmax / 64, 1);
        buffer.pos(xmax, ymin, 0).tex(xmax / 64, 0.75);
        buffer.pos(xmin, ymin, 0).tex(xmin / 64, 0.75);
        tess.draw();

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glTranslated(0, 0, -0.1);
        GL11.glColor4f(1f, 1f, 1f, 1f);
        GL11.glDisable(GL11.GL_LIGHTING);

        fontRenderer.drawString(name, -fontRenderer.getStringWidth(name) / 2, 0, 0xffffff);

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();

    }
}
