package invalid.myask.incommand.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class RotatePlayerMessage implements IMessage {
    double pitch, yaw;//, roll;
    public RotatePlayerMessage() {} //noargs ctor needed for receiver

    public RotatePlayerMessage(double yaw, double pitch
        /*, double roll*/
    ) {
        this.yaw = yaw;
        this.pitch = pitch;
        // this.roll = roll;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        yaw = buf.readDouble();
        pitch = buf.readDouble();
//        roll = buf.readDouble();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeDouble(yaw);
        buf.writeDouble(pitch);
//        buf.writeDouble(roll);
    }
}
