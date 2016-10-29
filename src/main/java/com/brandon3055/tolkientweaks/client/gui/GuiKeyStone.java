package com.brandon3055.tolkientweaks.client.gui;

import com.brandon3055.brandonscore.client.gui.modulargui.MGuiElementBase;
import com.brandon3055.brandonscore.client.gui.modulargui.ModularGuiScreen;
import com.brandon3055.brandonscore.client.gui.modulargui.lib.EnumAlignment;
import com.brandon3055.brandonscore.client.gui.modulargui.lib.IMGuiListener;
import com.brandon3055.brandonscore.client.gui.modulargui.lib.ModuleBuilder.RawColumns;
import com.brandon3055.brandonscore.client.gui.modulargui.modularelements.*;
import com.brandon3055.brandonscore.network.PacketTileMessage;
import com.brandon3055.tolkientweaks.TTFeatures;
import com.brandon3055.tolkientweaks.TolkienTweaks;
import com.brandon3055.tolkientweaks.items.Key;
import com.brandon3055.tolkientweaks.network.PacketSetKey;
import com.brandon3055.tolkientweaks.tileentity.TileKeyStone;
import com.brandon3055.tolkientweaks.utils.LogHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Created by brandon3055 on 29/10/2016.
 */
public class GuiKeyStone extends ModularGuiScreen implements IMGuiListener {

    private MGuiButtonToggle consumeKey;
    private MGuiButton toggleMode;
    private MGuiButtonToggle permanent;
    private MGuiButton reset;

    private MGuiTextField keyCodeField;
    private MGuiTextField dellayField;

    private boolean keymode;
    private EntityPlayer player;
    private TileKeyStone tile;

    public GuiKeyStone(EntityPlayer player, TileKeyStone tile) {
        this.player = player;
        this.tile = tile;
        xSize = 200;
        keymode = tile == null;
        ySize = keymode ? 70 : 128;
    }

    @Override
    public void initGui() {
        super.initGui();

        manager.add(MGuiBackground.newGenericBackground(this, guiLeft(), guiTop(), xSize, ySize));

        //region Key
        if (keymode) {
            MGuiLabel label = (MGuiLabel)manager.add(new MGuiLabel(this, guiLeft(), guiTop() + 5, xSize, 30, "Set the code for this key. The code can be anything as long as it matches the keystone.").setWrap(true).setAlignment(EnumAlignment.LEFT).setTextColour(0xFF000000).setShadow(false));
            keyCodeField = (MGuiTextField)manager.add(new MGuiTextField(this, guiLeft() + 5, label.yPos + label.ySize, xSize - 10, 14, fontRendererObj));
            keyCodeField.setMaxStringLength(1024);

            ItemStack stack = player.getHeldItemMainhand();
            if (stack == null || stack.getItem() != TTFeatures.key) {
                return;
            }

            keyCodeField.setText(((Key)player.getHeldItemMainhand().getItem()).getKey(player.getHeldItemMainhand()));
            reset = (MGuiButton) manager.add(new MGuiButtonSolid(this, guiLeft() + 5, keyCodeField.yPos + keyCodeField.ySize + 2, (xSize / 2) - 6, 14, "Apply Code"));
            consumeKey = (MGuiButtonToggle) manager.add(new MGuiButtonToggle(this, guiLeft() + (xSize / 2), keyCodeField.yPos + keyCodeField.ySize + 2, (xSize / 2) - 5, 14, "Show Code").setToolTip(new String[] {"Set weather or not to show the code to the player.", "This could be useful because the code could be anything", "Even something like \"Key to stone at x:452, z:754\""}).setToolTipDelay(2));
            consumeKey.setPressed(((Key)player.getHeldItemMainhand().getItem()).getShown(player.getHeldItemMainhand()));
        }
        //endregion
        else {
            RawColumns builder = new RawColumns(guiLeft() + 5, guiTop() + 5, 1, 14, 1);

            builder.add(consumeKey = new MGuiButtonToggle(this, 0, 0, xSize - 10, 14, "Toggle Consume Key") {
                @Override
                public boolean isPressed() {
                    return tile.consumeKey.value;
                }
            });
            builder.add(toggleMode = new MGuiButton(this, 0, 0, xSize - 10, 14, "Toggle Mode") {
                @Override
                public String getDisplayString() {
                    return "Mode: " + tile.mode;
                }
            });
//            builder.add(permanent = new MGuiButtonToggle(this, 0, 0, xSize - 10, 14, "Toggle Permanent") {
//                @Override
//                public boolean isPressed() {
//                    return tile.permanent.value;
//                }
//            });
            builder.add(reset = new MGuiButton(this, 0, 0, xSize - 10, 14, "Reset state (For permanent mode)"));
            builder.add(new MGuiLabel(this, 0, 0, xSize - 10, 14, "Key Code Field").setAlignment(EnumAlignment.LEFT).setTextColour(0xFF000000).setShadow(false));
            builder.add(keyCodeField = new MGuiTextField(this, 0, 0, xSize - 10, 14, fontRendererObj));
            keyCodeField.setMaxStringLength(1024).setListener(this);
            keyCodeField.setText(tile.keyCode.value);
            builder.add(new MGuiLabel(this, 0, 0, xSize - 10, 14, "Delay Field").setAlignment(EnumAlignment.LEFT).setTextColour(0xFF000000).setShadow(false));
            builder.add(dellayField = new MGuiTextField(this, 0, 0, xSize - 10, 14, fontRendererObj));
            dellayField.addChild(new MGuiHoverPopup(this, new String[] {"For button mode, Set how long in ticks the button stays pressed"}, dellayField));
            dellayField.setText(tile.delay.value + "");
            dellayField.setMaxStringLength(10).setListener(this);
            builder.add(new MGuiLabel(this, 0, 0, xSize - 10, 14, "Changes are saved automatically").setAlignment(EnumAlignment.LEFT).setTextColour(0xFF000000).setShadow(false));

            builder.finish(manager, 0);
        }

        manager.initElements();
    }

    @Override
    public void onMGuiEvent(String eventString, MGuiElementBase eventElement) {
        LogHelper.info(eventString+" "+eventElement);
        if (keymode && (eventElement == reset || eventElement == consumeKey)) {
            TolkienTweaks.network.sendToServer(new PacketSetKey(keyCodeField.getText(), consumeKey.isPressed()));
        }
        else if (!keymode) {
            if (eventElement == consumeKey) {
                tile.sendPacketToServer(new PacketTileMessage(tile, (byte) 0, false, false));
            }
            else if (eventElement == toggleMode) {
                tile.sendPacketToServer(new PacketTileMessage(tile, (byte) 1, false, false));
            }
            else if (eventElement == permanent) {
                tile.sendPacketToServer(new PacketTileMessage(tile, (byte) 2, false, false));
            }
            else if (eventElement == reset) {
                tile.sendPacketToServer(new PacketTileMessage(tile, (byte) 3, false, false));
            }
            else if (eventElement == keyCodeField) {
                tile.sendPacketToServer(new PacketTileMessage(tile, (byte) 4, keyCodeField.getText(), false));
            }
            else if (eventElement == dellayField) {
                tile.sendPacketToServer(new PacketTileMessage(tile, (byte) 5, dellayField.getText(), false));
            }
        }
    }
}
