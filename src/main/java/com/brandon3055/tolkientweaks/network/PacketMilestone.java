package com.brandon3055.tolkientweaks.network;

import com.brandon3055.brandonscore.network.MessageHandlerWrapper;
import com.brandon3055.tolkientweaks.ForgeEventHandler;
import com.brandon3055.tolkientweaks.TolkienTweaks;
import com.brandon3055.tolkientweaks.client.gui.GuiMilestone;
import com.brandon3055.tolkientweaks.tileentity.TileMilestone;
import com.brandon3055.tolkientweaks.utils.TTWorldData;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by brandon3055 on 13/06/2016.
 */
public class PacketMilestone implements IMessage {

    public static final byte TP = 0;
    public static final byte REQ_NAME = 1;
    public static final byte NAME = 2;
    public static final byte ERROR = 3;

    public String message;
    public byte function;
    public short coolDown;

    public PacketMilestone() {
    }

    public PacketMilestone(int function, String message) {
        this.function = (byte) function;
        this.message = message;
    }

    public PacketMilestone(int function, String message, int coolDown) {
        this.coolDown = (short)coolDown;
        this.function = (byte) function;
        this.message = message;
    }


    @Override
    public void fromBytes(ByteBuf buf) {
        function = buf.readByte();
        switch (function) {
            case TP:
                break;
            case REQ_NAME:
                break;
            case NAME:
                coolDown = buf.readShort();
            case ERROR:
                message = ByteBufUtils.readUTF8String(buf);
                break;
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(function);
        switch (function) {
            case TP:
                break;
            case REQ_NAME:
                break;
            case NAME:
                buf.writeShort(coolDown);
            case ERROR:
                ByteBufUtils.writeUTF8String(buf, message);
                break;
        }
    }

    public static class Handler extends MessageHandlerWrapper<PacketMilestone,IMessage> {

        @Override
        public IMessage handleMessage(PacketMilestone message, MessageContext ctx) {
            if (ctx.side == Side.CLIENT) {
                doClientStuff(message, ctx);
            } else {


                if (message.function == REQ_NAME) {
                    //region Get Name

                    EntityPlayerMP playerMP = ctx.getServerHandler().player;

                    if (!TTWorldData.getMap(playerMP.world).containsKey(playerMP.getName())) {
                        return new PacketMilestone(ERROR, "No milestone set!");
                    }

                    TTWorldData.MilestoneMarker marker = TTWorldData.getMap(playerMP.world).get(playerMP.getName());

                    MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
                    World world = server.getWorld(marker.dimension);

                    if (world == null) {
                        TolkienTweaks.network.sendTo(new PacketMilestone(ERROR, String.format("Dimension %s not found!", marker.dimension)), playerMP);
                        return null;
                    }

                    TileEntity tile = world.getTileEntity(new BlockPos(marker.x, marker.y, marker.z));

                    if (!(tile instanceof TileMilestone)) {
                        TolkienTweaks.network.sendTo(new PacketMilestone(ERROR, "Did not find your bound milestone..."), playerMP);
                        return null;
                    }

                    int time = ((TileMilestone) tile).cooldowns.containsKey(playerMP.getName()) ? (((TileMilestone) tile).cooldowns.get(playerMP.getName()) + ((TileMilestone) tile).coolDown) - ForgeEventHandler.tick : 0;
                    TolkienTweaks.network.sendTo(new PacketMilestone(NAME, ((TileMilestone) tile).markerName.value, time), playerMP);
                    return null;

                    //endregion
                } else if (message.function == TP) {
                    //region Teleport

                    EntityPlayerMP playerMP = ctx.getServerHandler().player;

                    if (!TTWorldData.getMap(playerMP.world).containsKey(playerMP.getName())) {
                        TolkienTweaks.network.sendTo(new PacketMilestone(ERROR, "No milestone set!"), playerMP);
                        return null;
                    }

                    TTWorldData.MilestoneMarker marker = TTWorldData.getMap(playerMP.world).get(playerMP.getName());

                    MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
                    World world = server.getWorld(marker.dimension);

                    if (world == null) {
                        TolkienTweaks.network.sendTo(new PacketMilestone(ERROR, String.format("Dimension %s not found!", marker.dimension)), playerMP);
                        return null;
                    }

                    TileEntity tile = world.getTileEntity(new BlockPos(marker.x, marker.y, marker.z));

                    if (!(tile instanceof TileMilestone)) {
                        TolkienTweaks.network.sendTo(new PacketMilestone(ERROR, "Did not find your bound milestone..."), playerMP);
                        return null;
                    }

                    ((TileMilestone) tile).handleTeleport(playerMP, false);
                    return null;

                    //endregion
                }
            }

            return null;
        }

        @SideOnly(Side.CLIENT)
        private static void doClientStuff(PacketMilestone message, MessageContext ctx) {
            if (message.function == NAME){
                if (Minecraft.getMinecraft().currentScreen instanceof GuiMilestone){
                    ((GuiMilestone) Minecraft.getMinecraft().currentScreen).milestone = message.message;
                    ((GuiMilestone) Minecraft.getMinecraft().currentScreen).time = message.coolDown;
                }
            }
            else if (message.function == ERROR){
                if (Minecraft.getMinecraft().currentScreen instanceof GuiMilestone){
                    ((GuiMilestone) Minecraft.getMinecraft().currentScreen).error = message.message;
                }
                else {
                    Minecraft.getMinecraft().player.sendMessage(new TextComponentString(message.message));
                }
            }
        }
    }
}
