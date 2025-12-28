package invalid.myask.incommand.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class RotatePlayerMessageHandler implements IMessageHandler<RotatePlayerMessage, IMessage> {
    @Override
    public IMessage onMessage(RotatePlayerMessage message, MessageContext ctx) {
        EntityPlayer alex = Minecraft.getMinecraft().thePlayer;
        alex.setPositionAndRotation(alex.posX, alex.posY, alex.posZ, (float) message.yaw, (float) message.pitch);
        return null;
    }
}
