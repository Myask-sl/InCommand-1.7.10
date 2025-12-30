package invalid.myask.incommand;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;

import invalid.myask.incommand.commands.CommandClone;
import invalid.myask.incommand.commands.CommandDie;
import invalid.myask.incommand.commands.CommandFill;
import invalid.myask.incommand.commands.CommandIron;
import invalid.myask.incommand.commands.CommandKillOther;
import invalid.myask.incommand.commands.CommandLoot;
import invalid.myask.incommand.commands.CommandRotate;
import invalid.myask.incommand.commands.CommandRotateSelf;
import invalid.myask.incommand.commands.CommandWood;
import invalid.myask.incommand.network.RotatePlayerMessage;
import invalid.myask.incommand.network.RotatePlayerMessageHandler;
import net.minecraft.world.GameRules;

public class CommonProxy {

    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {
        Config.synchronizeConfiguration(event.getSuggestedConfigurationFile());

        InCommand.networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(InCommand.MODID);
        InCommand.networkWrapper.registerMessage(RotatePlayerMessageHandler.class, RotatePlayerMessage.class, 0, Side.CLIENT);
    }

    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {}

    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {}

    // register server commands in this event handler (Remove if not needed)
    public void serverStarting(FMLServerStartingEvent event) {
        IDDictionary.refreshAllDicts();
        GameRules rules = event.getServer().getEntityWorld().getWorldInfo().getGameRulesInstance();
        if (Config.ancient_commands_enable) {
            event.registerServerCommand(CommandIron.instance);
            event.registerServerCommand(CommandWood.instance);
        }
        if (Config.loot_enable)
            event.registerServerCommand(CommandLoot.instance);
        if (Config.rotate_enable) {
            event.registerServerCommand(CommandRotate.instance);
            event.registerServerCommand(CommandRotateSelf.instance);
        }
        if (Config.killself_aliases)
            event.registerServerCommand(CommandDie.instance);
        if (Config.killother_enable)
            event.registerServerCommand(CommandKillOther.instance);
        if (Config.fill_clone_enable) {
            //event.registerServerCommand(CommandClone.instance);
            event.registerServerCommand(CommandFill.instance);
            rules.addGameRule("max_block_modifications", "32768");
            rules.addGameRule("max_block_meta", "15");
        }
    }
}
