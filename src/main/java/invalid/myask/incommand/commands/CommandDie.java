package invalid.myask.incommand.commands;

import invalid.myask.incommand.Config;
import net.minecraft.command.CommandKill;

import java.util.ArrayList;
import java.util.List;

public class CommandDie extends CommandKill {
    public static final CommandDie instance = new CommandDie();

    protected static final List<String> alias = new ArrayList<>(1);

    static {
        alias.add("killself");
    }

    @Override
    public String getCommandName() {
        return "die";
    }

    @Override
    public List<String> getCommandAliases() {
        return alias;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return Config.die_permission_level;
    }
}
