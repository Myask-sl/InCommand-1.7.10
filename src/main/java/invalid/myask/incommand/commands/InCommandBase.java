package invalid.myask.incommand.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public abstract class InCommandBase extends CommandBase {
    private final int permissionLevel;

    protected InCommandBase(int permissionLevel) {
        this.permissionLevel = permissionLevel;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "command." + getCommandName() + ".usage";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return permissionLevel;
    }
}
