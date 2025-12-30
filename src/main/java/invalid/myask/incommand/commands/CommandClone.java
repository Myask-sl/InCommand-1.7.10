package invalid.myask.incommand.commands;

import invalid.myask.incommand.Config;
import net.minecraft.command.ICommandSender;

public class CommandClone extends InCommandBase {
    public static final CommandClone instance = new CommandClone(Config.clone_permission_level);

    protected CommandClone(int permissionLevel) {
        super(permissionLevel);
    }

    @Override
    public String getCommandName() {
        return "clone";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {

    }
}
