package invalid.myask.incommand.commands.fake;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public abstract class CommandTabOnly extends CommandBase {

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        throw new CommandException("Attempted to execute fake command");
    }

    public abstract void processArguments(ICommandSender sender, String[] args);
}
