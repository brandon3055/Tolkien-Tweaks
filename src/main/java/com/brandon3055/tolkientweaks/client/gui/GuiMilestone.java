package com.brandon3055.tolkientweaks.client.gui;

import com.brandon3055.tolkientweaks.ConfigHandler;
import com.brandon3055.tolkientweaks.TolkienTweaks;
import com.brandon3055.tolkientweaks.network.PacketMilestone;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.TextFormatting;

import java.io.IOException;

/**
 * Created by brandon3055 on 14/06/2016.
 */
public class GuiMilestone extends GuiScreen {

    public String milestone = "Please Wait...";
    public String error = null;
    public int time = 0;
    private GuiScreen parent;
    private GuiButton goButton;

    public GuiMilestone(GuiScreen parent) {
        this.parent = parent;
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.clear();
        buttonList.add(goButton = new GuiButton(0, width / 2 - 50, height / 2 - 20, 100, 20, "Go To Milestone!"));
        buttonList.add(new GuiButton(1, width / 2 - 25, height / 2 + 20, 50, 20, "Cancel"));
    }

    @Override
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
        drawDefaultBackground();

        drawCenteredString(fontRendererObj, "Milestone", width / 2, height / 2 - 100, 0xFFFFFF);
        if (error == null){
            drawCenteredString(fontRendererObj, TextFormatting.DARK_GREEN + "Destination: " + TextFormatting.GOLD + milestone.replace("_", " "), width / 2, height / 2 - 85, 0xFFFFFF);
        }
        else {
            drawCenteredString(fontRendererObj, "Error: " + error, width / 2, height / 2 - 85, 0xFF0000);
        }

        if (time > 0){
            drawCenteredString(fontRendererObj, TextFormatting.DARK_RED + "Cool Down: " + TextFormatting.DARK_PURPLE + "" + time / 20 + " seconds", width / 2, height / 2 - 70, 0xFFFFFF);
        }

        drawGradientRect(width / 2 - 205, height / 2 + 45, width / 2 + 210, height / 2 + 98, 0xFFFFFFFF, 0xFFFFFFFF);
        drawGradientRect(width / 2 - 204, height / 2 + 46, width / 2 + 209, height / 2 + 97, 0xFF000000, 0xFF000000);

        fontRendererObj.drawSplitString("Milestones are special markers located in a variety of locations throughout Middle-earth and the overworld that will allow you to quickly return once it has been activated.  Only one is allowed to be activated at any given time and you can only choose a new one every "+ ConfigHandler.milestoneCoolDown / 60 + " minutes. They are simply for the purpose of returning to the selected location and are a one-way trip.", width / 2 - 200,  height / 2 + 50, 400, 0x999999);

        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0){
            TolkienTweaks.network.sendToServer(new PacketMilestone(PacketMilestone.TP, ""));
            this.mc.displayGuiScreen((GuiScreen)null);
            this.mc.setIngameFocus();
        }
        else if (button.id == 1){
            mc.displayGuiScreen(parent);
        }
    }


    @Override
    protected void keyTyped(char p_73869_1_, int p_73869_2_) throws IOException {
        super.keyTyped(p_73869_1_, p_73869_2_);


        if (p_73869_2_ == this.mc.gameSettings.keyBindInventory.getKeyCode()){
            mc.displayGuiScreen(parent);
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        if (time > 0) {
            time--;
        }

        goButton.enabled = error == null && time <= 0;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
