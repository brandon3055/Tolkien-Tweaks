package com.brandon3055.tolkientweaks.client.gui;


import codechicken.lib.math.MathHelper;
import com.brandon3055.brandonscore.client.gui.modulargui.GuiElementManager;
import com.brandon3055.brandonscore.client.gui.modulargui.MGuiElementBase;
import com.brandon3055.brandonscore.client.gui.modulargui.ModularGuiScreen;
import com.brandon3055.brandonscore.client.gui.modulargui.baseelements.GuiButton;
import com.brandon3055.brandonscore.client.gui.modulargui.guielements.GuiLabel;
import com.brandon3055.brandonscore.client.gui.modulargui.guielements.GuiTextField;
import com.brandon3055.brandonscore.client.gui.modulargui.guielements.GuiTexture;
import com.brandon3055.brandonscore.client.gui.modulargui.lib.GuiAlign;
import com.brandon3055.brandonscore.lib.datamanager.*;
import com.brandon3055.brandonscore.utils.Utils;
import com.brandon3055.tolkientweaks.tileentity.TileCamoSpawner;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by brandon3055 on 29/10/2016.
 */
public class GuiCamoSpawner extends ModularGuiScreen {

    private final EntityPlayer player;
    private final TileCamoSpawner tile;

    public GuiCamoSpawner(EntityPlayer player, TileCamoSpawner tile) {
        this.player = player;
        this.tile = tile;
        xSize = 300;
        ySize = 204;
    }

    @Override
    public void addElements(GuiElementManager manager) {
        GuiTexture container = GuiTexture.newBCTexture(xSize, ySize);
        manager.add(container);
        container.addAndFireReloadCallback(o -> container.setPos(guiLeft(), guiTop()));

        int yPos = container.yPos() + 5;

        yPos = addElement(yPos, container, createConfigElement("entityTag"));
        yPos = addElement(yPos, container, createConfigElement("minSpawnDelay"));
        yPos = addElement(yPos, container, createConfigElement("maxSpawnDelay"));
        yPos = addElement(yPos, container, createConfigElement("activationRange"));
        yPos = addElement(yPos, container, createConfigElement("spawnRange"));
        yPos = addElement(yPos, container, createConfigElement("requirePlayer"));
        yPos = addElement(yPos, container, createConfigElement("ignoreSpawnReq"));
        yPos = addElement(yPos, container, createConfigElement("spawnerParticles"));
        yPos = addElement(yPos, container, createConfigElement("spawnCount"));
        yPos = addElement(yPos, container, createConfigElement("maxCluster"));
        yPos = addElement(yPos, container, createConfigElement("clusterRange"));
        yPos = addElement(yPos, container, createConfigElement("spawnDelay"));
        yPos = addElement(yPos, container, createConfigElement("startSpawnDelay"));
    }

    private int addElement(int yPos, MGuiElementBase container, MGuiElementBase element) {
        container.addChild(element);
        element.setPos(container.xPos() + 5, yPos);
        return yPos + element.ySize() + 3;
    }

    public MGuiElementBase createConfigElement(String name) {
        IManagedData data = tile.getDataManager().getDataByName(name);

        GuiLabel label = new GuiLabel(name);
        label.setYSize(12);
        label.setXSize(95);
        label.setAlignment(GuiAlign.LEFT);

        MGuiElementBase control = createControl(data);

        label.addChild(control);
        control.setPosAndSize(label.maxXPos(), label.yPos(), xSize() - label.xSize() - 10, label.ySize());
        return label;
    }

    public MGuiElementBase createControl(IManagedData data) {
        if (data instanceof ManagedString) {
            GuiTextField field = new GuiTextField();
            field.setMaxStringLength(10000);
            field.setLinkedValue(() -> ((ManagedString) data).value);
            field.setChangeListener(() -> tile.sendPacketToServer(mcDataOutput -> mcDataOutput.writeString(data.getName()).writeString(field.getText()), 0));
            return field;
        }
        else if (data instanceof ManagedByte || data instanceof ManagedShort || data instanceof ManagedInt) {
            GuiTextField field = new GuiTextField();
            field.setValidator(value -> Utils.validInteger(value) || value.isEmpty());
            field.setLinkedValue(data::toString);
            field.setChangeListener(() -> {
                String text = field.getText();
                int value = text.isEmpty() ? 0 : Utils.parseInt(text);
                if (data instanceof ManagedByte) {
                    value = MathHelper.clip(value, Byte.MIN_VALUE, Byte.MAX_VALUE);
                }
                else if (data instanceof ManagedShort) {
                    value = MathHelper.clip(value, Short.MIN_VALUE, Short.MAX_VALUE);
                }
                else {
                    value = MathHelper.clip(value, Integer.MIN_VALUE, Integer.MAX_VALUE);
                }
                int finalValue = value;
                tile.sendPacketToServer(mcDataOutput -> mcDataOutput.writeString(data.getName()).writeInt(finalValue), 0);
            });
            return field;
        }
        else if (data instanceof ManagedBool) {
            GuiButton button = new GuiButton();
            button.setVanillaButtonRender(true);
            button.setToggleMode(true);
            button.setToggleStateSupplier(() -> ((ManagedBool) data).value);
            button.setDisplaySupplier(data::toString);
            button.setListener(() -> tile.sendPacketToServer(mcDataOutput -> mcDataOutput.writeString(data.getName()).writeBoolean(!button.getToggleState()), 0));
            return button;
        }
        return new GuiLabel("Error Invalid Field");
    }
}
