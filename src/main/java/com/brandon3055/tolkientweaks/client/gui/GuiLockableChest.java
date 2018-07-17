package com.brandon3055.tolkientweaks.client.gui;

import com.brandon3055.tolkientweaks.container.ContainerLockableChest;
import com.brandon3055.tolkientweaks.container.InventoryLockableChest;
import com.brandon3055.tolkientweaks.tileentity.TileLockableChest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

/**
 * Created by brandon3055 on 16/04/2017.
 */
public class GuiLockableChest extends GuiContainer
{
    private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");
    private final InventoryLockableChest upperChestInventory;
    private final IInventory lowerChestInventory;
    private final int inventoryRows;

    public GuiLockableChest(TileLockableChest tile, InventoryLockableChest chestInv, IInventory playerInv)
    {
        super(new ContainerLockableChest(tile, playerInv, chestInv, Minecraft.getMinecraft().player));
        this.upperChestInventory = chestInv;
        this.lowerChestInventory = playerInv;
        this.allowUserInput = false;
        int i = 222;
        int j = 114;
        this.inventoryRows = chestInv.getSizeInventory() / 9;
        this.ySize = 114 + this.inventoryRows * 18;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.fontRenderer.drawString((upperChestInventory.isDouble() ? TextFormatting.DARK_PURPLE : TextFormatting.DARK_AQUA) + this.upperChestInventory.getDisplayName().getUnformattedText(), 8, 6, 4210752);
        this.fontRenderer.drawString(this.lowerChestInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(CHEST_GUI_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.inventoryRows * 18 + 17);
        this.drawTexturedModalRect(i, j + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
    }
}