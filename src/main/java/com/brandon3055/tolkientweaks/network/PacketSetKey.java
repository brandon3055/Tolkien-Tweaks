package com.brandon3055.tolkientweaks.network;

import com.brandon3055.brandonscore.network.MessageHandlerWrapper;
import com.brandon3055.tolkientweaks.TTFeatures;
import com.brandon3055.tolkientweaks.items.Key;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by brandon3055 on 13/06/2016.
 */
public class PacketSetKey implements IMessage {

    public String keyCode;
    public boolean show;

    public PacketSetKey() {
    }

    public PacketSetKey(String keyCode, boolean show) {
        this.keyCode = keyCode;
        this.show = show;
    }


    @Override
    public void fromBytes(ByteBuf buf) {
        keyCode = ByteBufUtils.readUTF8String(buf);
        show = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, keyCode);
        buf.writeBoolean(show);
    }

    public static class Handler extends MessageHandlerWrapper<PacketSetKey,IMessage> {

        @Override
        public IMessage handleMessage(PacketSetKey message, MessageContext ctx) {
            if (ctx.side == Side.SERVER && ctx.getServerHandler().playerEntity.isCreative()) {
                ItemStack stack = ctx.getServerHandler().playerEntity.getHeldItemMainhand();
                if (stack == null || stack.getItem() != TTFeatures.key) {
                    ctx.getServerHandler().playerEntity.addChatComponentMessage(new TextComponentString("You are not holding a key!... Wait... Didnt you just right click a key to get that GUI? Im confused..."));
                    return null;
                }
                ((Key)stack.getItem()).setKey(stack, message.keyCode);
                ((Key)stack.getItem()).setShown(stack, message.show);
            }

            return null;
        }
    }
}
