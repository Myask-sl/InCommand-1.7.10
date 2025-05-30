package invalid.myask.incommand.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;

import invalid.myask.incommand.Config;

public class CommandIron extends InCommandBase {

    public static CommandIron instance = new CommandIron(Config.iron_permission_level);
    private Map<UUID, Long> lastInvoke = new HashMap<>();

    protected CommandIron(int permissionLevel) {
        super(permissionLevel);
    }

    @Override
    public String getCommandName() {
        return "iron";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        EntityPlayer player = getCommandSenderAsPlayer(sender);
        UUID id = player.getPersistentID();
        Long l = lastInvoke.get(id);
        if (l == null || l.longValue() + Config.ancient_drops_delay <= player.worldObj.getWorldTime()) {
            lastInvoke.put(id, player.worldObj.getWorldTime());
            ItemStack itS = new ItemStack(Items.iron_ingot, 4, 0);
            EntityItem it = player.dropPlayerItemWithRandomChoice(itS, false);
            func_152373_a(
                sender,
                this,
                "commands.iron.success",
                new Object[] { itS.func_151000_E(), 4, player.getCommandSenderName() }); //TODO: make params actually in template.
        } else sender.addChatMessage(new ChatComponentTranslation("commands.iron.failure.hasty"));
    }
}
