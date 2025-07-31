package invalid.myask.incommand.commands.fake;

import invalid.myask.incommand.IDDictionary;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.List;

public class CommandFakeEnchant extends CommandTabOnly {
    public static CommandFakeEnchant instance = new CommandFakeEnchant();
    @Override
    public String getCommandName() {
        return "enchant";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
         List<String> answers = new ArrayList<>();
         if (args.length == 2) {
             if (IDDictionary.enchLocalDict.isEmpty()) IDDictionary.initLocalEnchDict();
             answers = getListOfStringsFromIterableMatchingLastWord(args, IDDictionary.enchLocalDict.keySet());
         }
         return answers;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void processArguments(ICommandSender sender, String[] argsPlusCommand) {
        if (argsPlusCommand.length > 1) {
            argsPlusCommand[1] = IDDictionary.translateTargetSelector(argsPlusCommand[1]);
            if (argsPlusCommand.length > 2) {
                argsPlusCommand[2] = IDDictionary.deLocalizeEnchant(argsPlusCommand[2]);
            }
        }
    }
}
