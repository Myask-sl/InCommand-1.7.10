package invalid.myask.incommand.commands;

import invalid.myask.incommand.Config;
import invalid.myask.incommand.IDDictionary;
import invalid.myask.incommand.world.TeleporterNoPortal;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraftforge.common.DimensionManager;

import java.util.ArrayList;
import java.util.List;

public class CommandWarp extends InCommandBase {
    public static final CommandWarp instance = new CommandWarp(Config.warp_permission_level);

    protected CommandWarp(int permissionLevel) {
        super(permissionLevel);
    }

    @Override
    public String getCommandName() {
        return "warp";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        Entity target;
        if (args.length < 1) throw new CommandException ("commands.warp.failure.args.few");

        else if (args.length == 1)
            target = getCommandSenderAsEntity(sender);
        else
            target = getOnePlayerOrEntity(sender, args[0]);
        if (target == null) throw new CommandException("commands.warp.failure.entity.dne", args[0]);
        Integer id;
        if (args[args.length - 1].matches("^[+-]?\\d+$")) {
            id = parseInt(sender, args[args.length - 1]);
            if (!DimensionManager.isDimensionRegistered(id))
                throw new CommandException("commands.warp.failure.dimension.dne", args[args.length - 1]);
        }
        else{
            id = IDDictionary.lookupDimension(args[args.length - 1]);
            if (id == null) throw new CommandException("commands.warp.failure.dimension.dne", args[args.length - 1]);
        }
        if (target.dimension == id) {
            if (args.length >= 2 && !args[0].equals(SENDER_SELECTOR))
                func_152373_a(sender, this, "commands.warp.success.already", args[0], args[args.length - 1]);
            else
                func_152373_a(sender, this, "commands.warp.success.already", sender.getCommandSenderName(), args[args.length - 1]);
            return;
        }

        target.timeUntilPortal = target.getPortalCooldown();
        if (id == 0 || id == -1) {
            cleanlyTransferToDimension(target, id);
        } else target.travelToDimension(id);

        if (args.length >= 2 && !args[0].equals(SENDER_SELECTOR))
            func_152373_a(sender, this, "commands.warp.success", args[0], args[args.length - 1]);
        else
            func_152373_a(sender, this, "commands.warp.success", sender.getCommandSenderName(), args[args.length - 1]);
    }

    private void cleanlyTransferToDimension(Entity target, int id) {
        MinecraftServer server = MinecraftServer.getServer();
        ServerConfigurationManager manager = server.getConfigurationManager();

        if (target instanceof EntityPlayerMP bill)
            manager.transferPlayerToDimension(bill, id, TeleporterNoPortal.to(id));
        else {
            manager.transferEntityToWorld(target, target.dimension, server.worldServerForDimension(target.dimension),
                server.worldServerForDimension(id), TeleporterNoPortal.to(id));
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return switch (args.length) {
            case 1 -> getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
            case 2 -> getListOfStringsFromIterableMatchingLastWord(args, IDDictionary.dimDict.keySet());
            default -> new ArrayList<>(0);
        };
    }
}
