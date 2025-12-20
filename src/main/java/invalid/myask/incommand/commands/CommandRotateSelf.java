package invalid.myask.incommand.commands;

import invalid.myask.incommand.Config;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import java.util.ArrayList;
import java.util.List;

public class CommandRotateSelf extends CommandRotate {
    public static final CommandRotateSelf instance = new CommandRotateSelf(Config.rotateself_permission_level);
    private static final List<String> ROTATE_TAB = new ArrayList<>(1);

    static {
        ROTATE_TAB.add("~ ~");
    }

    protected CommandRotateSelf(int permissionLevel) {
        super(permissionLevel);
    }

    @Override
    public String getCommandName() {
        return "rotateself";
    }

    @Override
    public List<String> getCommandAliases() {
        return null; //don't clobber /rotate's aliases
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length < 2) throw new CommandException("commands.rotate.failure.args.few");
        String[] newArgs = new String[] {"@s", args[0], args[1]};
        super.processCommand(sender, newArgs);
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 1 || args.length == 0) return ROTATE_TAB;
        return null;
    }
}
