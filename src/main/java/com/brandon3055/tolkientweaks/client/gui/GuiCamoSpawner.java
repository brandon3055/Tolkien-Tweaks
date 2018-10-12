package com.brandon3055.tolkientweaks.client.gui;


import codechicken.lib.math.MathHelper;
import com.brandon3055.brandonscore.client.gui.modulargui.GuiElementManager;
import com.brandon3055.brandonscore.client.gui.modulargui.MGuiElementBase;
import com.brandon3055.brandonscore.client.gui.modulargui.ModularGuiScreen;
import com.brandon3055.brandonscore.client.gui.modulargui.baseelements.GuiButton;
import com.brandon3055.brandonscore.client.gui.modulargui.guielements.*;
import com.brandon3055.brandonscore.client.gui.modulargui.lib.GuiAlign;
import com.brandon3055.brandonscore.lib.DelayedTask;
import com.brandon3055.brandonscore.lib.datamanager.*;
import com.brandon3055.brandonscore.utils.Utils;
import com.brandon3055.tolkientweaks.tileentity.TileCamoSpawner;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

        GuiButton listButton = new GuiButton("Entity List");
        listButton.setPos(container.xPos() + 100, yPos);
        listButton.setSize(xSize() - 105, 14);
        listButton.setVanillaButtonRender(true);
        listButton.setListener(() -> openEntitySelector(container)); container.addChild(listButton);
    }

    private void openEntitySelector(MGuiElementBase parent) {
        GuiSelectDialog<EntityData> selector = new GuiSelectDialog<>(parent);
        selector.setSize(200, 250).setInsets(1, 1, 12, 1).setCloseOnSelection(false);
        selector.addChild(new GuiBorderedRect().setPosAndSize(selector).setColours(0xFFFFFFFF, 0xFF000000));
        selector.setRendererBuilder(data -> {
            MGuiElementBase base = new GuiBorderedRect().setColours(0xFF000000, 0xFF707070).setSize(130, 40);
            base.addChild(new GuiEntityRenderer().setTrackMouse(true).setForce2dSize(true).setPosAndSize(7, 11, 24, 24).setEntity(data.entity).setSilentErrors(true));
            base.addChild(new GuiLabel().setDisplaySupplier(data::toString).setShadow(false).setPosAndSize(35, 0, 160, 40).setWrap(true));
            return base;
        });

        GuiTextField filter = new GuiTextField();
        selector.addChild(filter);
        filter.setSize(selector.xSize(), 14).setPos(selector.xPos(), selector.maxYPos() - 12);


        Runnable reload = () -> {
            selector.clearItems();
            String filterText = filter.getText();
            Map<String, EntityData> dataMap = new HashMap<>();
            List<String> tags = new LinkedList<>(tile.entityTags);
            tags.sort(String::compareTo);
            for (String rawTag : tags) {
                if (dataMap.containsKey(rawTag)) {
                    dataMap.get(rawTag).count++;
                }
                else {
                    EntityData data = new EntityData(parent.mc.world, rawTag);
                    dataMap.put(rawTag, data);

                    if (filterText.isEmpty() || data.name.toLowerCase().contains(filterText.toLowerCase())) {
                        selector.addItem(data);
                    }
                }
            }
        };

        reload.run();
        filter.setListener((event1, eventSource1) -> reload.run());

        selector.showCenter();
        selector.getScrollElement().setListSpacing(1).reloadElement();
        selector.setSelectionListener(s -> {
            GuiPopupDialogs dialog = GuiPopupDialogs.createDialog(parent, GuiPopupDialogs.DialogType.YES_NO_CANCEL_OPTION, "Would you like do delete this entity?\n" + s.toString());
            dialog.yesButton.setText("Delete 1").setListener(() -> {
                delete(s.data, false);
                DelayedTask.run(10, reload);
            });
            dialog.noButton.setText("Delete all").setListener(() -> {
                delete(s.data, true);
                DelayedTask.run(10, reload);
            });
            dialog.showCenter(800);
        });
    }

    private void delete(String tag, boolean all) {
        tile.sendPacketToServer(output -> output.writeString(tag).writeBoolean(all), 2);
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

    private static class EntityData {
        private String data;
        private Entity entity;
        private String name;
        private int count = 1;

        public EntityData(World world, String data) {
            this.data = data;
            this.entity = TileCamoSpawner.createEntity(world, data);
            this.name = entity.getDisplayName().getFormattedText();
        }

        @Override
        public String toString() {
            return count + "x" + name;
        }
    }
}
