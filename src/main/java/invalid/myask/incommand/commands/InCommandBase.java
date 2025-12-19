package invalid.myask.incommand.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerSelector;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.Vec3;

import java.util.regex.Pattern;

public abstract class InCommandBase extends CommandBase {
    private final int permissionLevel;
    public static final String SENDER_SELECTOR = "@s";
    public static final String ALL_ENTITIES_SELECTOR = "@e";

    private static final Pattern ALL_ENTITY_PATTERN = Pattern.compile("^@(e)(?:\\[([\\w=,!-]*)\\])?$");

    protected InCommandBase(int permissionLevel) {
        this.permissionLevel = permissionLevel;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands." + getCommandName() + ".usage";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return permissionLevel;
    }

    public static Entity getCommandSenderAsEntity(ICommandSender sender) {
        if (sender instanceof Entity)
        {
            return (Entity)sender;
        }
        else
        {
            throw new SyntaxErrorException("Somehow executing command from not-Entity.");
        }
    }

    public static Entity matchOneEntity(ICommandSender sender, String arg) {
        Entity[] matchedEntities = matchEntities(sender, arg);
        return matchedEntities != null && matchedEntities.length == 1 ? matchedEntities[0] : null;
    }

    public static Entity[] matchEntities(ICommandSender sender, String arg) {
        if (SENDER_SELECTOR.equalsIgnoreCase(arg)) return new Entity[] {getCommandSenderAsEntity(sender)}; //this is why not making it EntitySelector
        else if (ALL_ENTITIES_SELECTOR.equalsIgnoreCase(arg)) return sender.getEntityWorld().getLoadedEntityList().toArray(new Entity[0]);
        return PlayerSelector.matchPlayers(sender, arg);
    }

    //todo: worldbound, mixin CommandBase's impl?
    public Vec3 parseCaretNotation(ICommandSender sender, String[] args, int firstIndex) {
        EntityPlayerMP steve = getCommandSenderAsPlayer(sender);
        Vec3 result = Vec3.createVectorHelper(steve.posX, steve.posY, steve.posZ),
             up, left, forth;
        float oldPitch = steve.rotationPitch, oldYaw = steve.rotationYaw;
        steve.rotationPitch = 0; steve.rotationYaw -= 90;
        left = steve.getLookVec().normalize();
        steve.rotationPitch = oldPitch - 90; steve.rotationYaw = oldYaw;
        up = steve.getLookVec().normalize();
        steve.rotationPitch = oldPitch; steve.rotationYaw = oldYaw;
        forth = steve.getLookVec().normalize();
        double sway = parseDouble(sender, args[firstIndex].substring( 1)),
            heave = parseDouble(sender, args[firstIndex + 1].substring( 1)),
            surge = parseDouble(sender, args[firstIndex + 2].substring( 1));
        result.xCoord += sway * left.xCoord + heave * up.xCoord + surge * forth.xCoord;
        result.yCoord += sway * left.yCoord + heave * up.yCoord + surge * forth.yCoord;
        result.zCoord += sway * left.zCoord + heave * up.zCoord + surge * forth.zCoord;
        return result;
    }

    public Vec3 parseCoordinatesCaretOrTilde(ICommandSender sender, String[] args, int firstIndex) {
        int caretArgs = 0;
        for (int i = 0; i < 3; i++)
            if (args[firstIndex + i].startsWith("^")) caretArgs++;
        if (caretArgs == 1 || caretArgs == 2)
            throw new SyntaxErrorException("commands.coords.caret.mixed");
        if (caretArgs == 3) return parseCaretNotation(sender, args, firstIndex);

        EntityPlayerMP steve = getCommandSenderAsPlayer(sender);
        return Vec3.createVectorHelper(
            func_110666_a(sender, steve.posX, args[firstIndex]), //tilde-or-none parser
            func_110666_a(sender, steve.posY, args[firstIndex + 1]),
            func_110666_a(sender, steve.posZ, args[firstIndex + 2])
        );
    }

    public double parseTildeRotation(ICommandSender sender, double rot, String arg) {
        if (arg.startsWith("~")) {
            return rot + parseDouble(sender, arg.substring(1));
        }
        else return parseDouble(sender, arg);
    }
}
