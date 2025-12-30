package invalid.myask.incommand.commands;

import invalid.myask.incommand.Config;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.Vec3;

public class CommandCoordTest extends InCommandBase {
    public static final CommandCoordTest instance = new CommandCoordTest(Config.coordtest_permission_level);
    protected CommandCoordTest(int permissionLevel) {
        super(permissionLevel);
    }

    @Override
    public String getCommandName() {
        return "coordtest";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length < 3) throw new CommandException("commands.coordtest.failure.args.few");
        Vec3 result = parseCoordinatesCaretOrTilde(sender, args, 0);
        func_152373_a(sender, this,"commands.coordtest.success", result.xCoord, result.yCoord, result.zCoord);
    }
}
