package invalid.myask.incommand.commands;

import invalid.myask.incommand.Config;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CommandKillOther extends InCommandBase {
    public static final CommandKillOther instance = new CommandKillOther(Config.killother_permission_level);

    protected static final List<String> alias = new ArrayList<>(1);
    static {
        if (Config.killother_alias_kill) alias.add("kill");
    }

    protected CommandKillOther(int killotherPermissionLevel) {
        super(killotherPermissionLevel);
    }

    @Override
    public String getCommandName() {
        return "killother";
    }

    @Override
    public List<String> getCommandAliases() {
        return alias;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length < 1) throw new CommandException("commands.killother.failure.args.few");
        Set<Entity> matchedEntities = new HashSet<>(1);
        for (String arg : args) {
            Entity[] entities = matchEntities(sender, arg);
            for (Entity e : entities) matchedEntities.add(e);
        }
        for (Entity e: matchedEntities) {
            if (e instanceof EntityLivingBase elb)
                elb.attackEntityFrom(new DamageSource("command_killother"), Float.MAX_VALUE);
            else e.setDead();
        }
        func_152373_a(sender, instance, "commands.killother.success", sender.getCommandSenderName(), matchedEntities);
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
    }
}
