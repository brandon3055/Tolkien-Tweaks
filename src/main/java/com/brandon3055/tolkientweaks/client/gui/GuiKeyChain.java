package com.brandon3055.tolkientweaks.client.gui;

import com.brandon3055.tolkientweaks.container.ContainerKeyChain;
import com.brandon3055.tolkientweaks.container.InventoryItemStackDynamic;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;

/**
 * Created by brandon3055 on 16/04/2017.
 */
public class GuiKeyChain extends GuiContainer
{
    private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");
    private final InventoryItemStackDynamic inventoryItem;
    private final InventoryPlayer playerInventory;
    private EnumHand hand;
    private int inventoryRows;
    private int inventorySlots;
    private ItemStack stackCache;

    public GuiKeyChain(InventoryItemStackDynamic inventoryItem, InventoryPlayer playerInv, EnumHand hand)
    {
        super(new ContainerKeyChain(playerInv, inventoryItem, Minecraft.getMinecraft().player, hand));
        this.inventoryItem = inventoryItem;
        this.playerInventory = playerInv;
        this.hand = hand;
        this.allowUserInput = false;
        this.inventoryRows = 1 + (inventoryItem.getSizeInventory() / 9);
        this.ySize = 114 + inventoryRows * 18;
        this.stackCache = playerInv.player.getHeldItem(hand);
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
        this.fontRenderer.drawString(I18n.format(inventoryItem.getDisplayName().getUnformattedText()), 8, 6, 4210752);
        this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(CHEST_GUI_TEXTURE);
        int i = (width - xSize) / 2;
        int j = (height - ySize) / 2;
        drawTexturedModalRect(i, j, 0, 0, xSize, inventoryRows * 18 + 17);
        drawTexturedModalRect(i, j + inventoryRows * 18 + 17, 0, 126, xSize, 96);

        if (inventorySlots % 9 > 0){
            drawGradientRect(guiLeft + 7 + ((inventorySlots % 9) * 18), guiTop + ySize - 115, guiLeft + xSize - 7, guiTop + ySize - 97, 0xFFc5c5c5, 0xFFc5c5c5);
        }
    }

    @Override
    public void updateScreen() {
        inventoryRows = 1 + ((inventoryItem.getSizeInventory() - 1) / 9);
        inventorySlots = inventoryItem.getSizeInventory();

        this.ySize = 114 + inventoryRows * 18;
        this.guiTop = (this.height - this.ySize) / 2;

//        ((ContainerKeyChain)super.inventorySlots).checkSlots();

        super.updateScreen();
    }
}