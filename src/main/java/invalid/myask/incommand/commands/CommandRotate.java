package invalid.myask.incommand.commands;

import invalid.myask.incommand.Config;
import invalid.myask.incommand.InCommand;
import invalid.myask.incommand.network.RotatePlayerMessage;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Vec3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandRotate extends InCommandBase {
    public static final CommandRotate instance = new CommandRotate(Config.rotate_permission_level);
    public static final String FACING = "facing";
    public static final String ENTITY = "entity";
    public static final ArrayList<String> ANCHOR_WORDS = new ArrayList<>(Arrays.asList("eyes", "feet", "top"));

    protected CommandRotate(int permissionLevel) {
        super(permissionLevel);
    }

    @Override
    public String getCommandName() {
        return "rotate";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length < 3) throw new CommandException("commands.rotate.failure.args.few");
        Entity subject = matchOneEntity(sender, args[0]), target = null;
        if (subject == null) throw new CommandException("commands.rotate.failure.args.nosubject");
        if (FACING.equalsIgnoreCase(args[1])) {
            Vec3 targetPos;
            boolean faceEntity = ENTITY.equalsIgnoreCase(args[2]);
            if (faceEntity) {
                if (args.length < 4) throw new CommandException("commands.rotate.failure.args.facing.entity");
                target = matchOneEntity(sender, args[3]);
                if (target == null) throw new CommandException("commands.rotate.failure.args.facing.entity");
                targetPos = Vec3.createVectorHelper(target.posX, target.posY, target.posZ);
                if (args.length == 5) {
                    if ("eyes".equalsIgnoreCase(args[4]))
                        targetPos.yCoord += target.getEyeHeight();
                    else if ("top".equalsIgnoreCase(args[4]))
                        targetPos.yCoord += target.height;
                    //else feet
                }
            } else { //raw coords
                if (args.length < 5) throw new CommandException("commands.rotate.failure.args.few");
                targetPos = parseCoordinatesCaretOrTilde(sender, args, 2);
            }
            targetPos.xCoord -= subject.posX;
            targetPos.yCoord -= subject.posY;
            targetPos.zCoord -= subject.posZ;
            if (targetPos.squareDistanceTo(0,0,0) < Config.rotate_facing_min_dist_square)
                throw new CommandException("commands.rotate.failure.facing.tooclose");
            subject.rotationYaw = (float)(Math.atan2(targetPos.xCoord, targetPos.yCoord) * 180.0D / Math.PI);

            double dist2d = targetPos.xCoord * targetPos.xCoord + targetPos.zCoord * targetPos.zCoord;
            dist2d = Math.sqrt(dist2d);
            subject.rotationPitch = (float)(Math.atan2(targetPos.yCoord, dist2d) * 180.0D / Math.PI);
            while (subject.rotationPitch - subject.prevRotationPitch < -180.0F)
                subject.prevRotationPitch -= 360.0F;

            if (faceEntity) func_152373_a(sender, this, "commands.rotate.success.facing.entity", subject, target.getCommandSenderName());
            else func_152373_a(sender, this, "commands.rotate.success.facing.coordinates", subject.getCommandSenderName(),
                    targetPos.xCoord + subject.posX,
                    targetPos.yCoord + subject.posY,
                    targetPos.zCoord + subject.posZ);

            if (subject instanceof EntityPlayerMP alex) InCommand.networkWrapper.sendTo(new RotatePlayerMessage(alex.rotationYaw, alex.rotationPitch), alex);

        } else { //raw rotations
            subject.rotationYaw = (float) parseTildeRotation(sender, subject.rotationYaw, args[1]);
            subject.rotationPitch = (float) parseTildeRotation(sender, subject.rotationPitch, args[2]);
            if (subject instanceof EntityPlayerMP alex) InCommand.networkWrapper.sendTo(new RotatePlayerMessage(alex.rotationYaw, alex.rotationPitch), alex);
            func_152373_a(sender, this,"commands.rotate.success.angles", subject.getCommandSenderName(), args[1], args[2]);
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        List<String> response = new ArrayList<>();
        switch (args.length) {
            case 0, 1 -> {
                return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
                //EntityPlayerMP player = PlayerSelector.matchOnePlayer(sender, args.length == 0 ? "" : args[0]);
                //if (player != null) response.add(player.getCommandSenderName());
          } case 2 -> {
                response.add(FACING);
                response.add("~ ~");
          } case 3 -> {
                if (FACING.equalsIgnoreCase(args[2])) {
                    response.add(ENTITY);
                    response.add("~ ~ ~");
                    response.add("~");
                }
          } case 4 -> {
                if (FACING.equalsIgnoreCase(args[2]) && ENTITY.equalsIgnoreCase(args[3])) {
                    return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
                }
          } case 5 -> {
                if (FACING.equalsIgnoreCase(args[2]) && ENTITY.equalsIgnoreCase(args[3])) {
                    return getListOfStringsFromIterableMatchingLastWord(args, ANCHOR_WORDS);
                }
            }
        }
        return response;
    }
}
