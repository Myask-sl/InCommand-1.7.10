package invalid.myask.incommand.commands.fake;

import invalid.myask.incommand.IDDictionary;
import net.minecraft.command.ICommandSender;

import java.util.ArrayList;
import java.util.List;

public class CommandFakeEffect extends CommandTabOnly {
    public static CommandFakeEffect instance = new CommandFakeEffect();
    @Override
    public String getCommandName() {
        return "effect";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
         List<String> answers = new ArrayList<>();
         if (args.length == 2) {
             if (IDDictionary.effLocalDict.isEmpty()) IDDictionary.initLocalEffDict();
             answers = getListOfStringsFromIterableMatchingLastWord(args, IDDictionary.effLocalDict.keySet());
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
                argsPlusCommand[2] = IDDictionary.deLocalizeEffect(argsPlusCommand[2]);
            }
        }
    }
}
