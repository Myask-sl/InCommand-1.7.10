package invalid.myask.incommand;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import invalid.myask.incommand.commands.CommandIron;
import invalid.myask.incommand.commands.CommandLoot;
import invalid.myask.incommand.commands.CommandWood;

public class CommonProxy {

    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {
        Config.synchronizeConfiguration(event.getSuggestedConfigurationFile());

    }

    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {}

    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {}

    // register server commands in this event handler (Remove if not needed)
    public void serverStarting(FMLServerStartingEvent event) {
        IDDictionary.refreshAllDicts();
        if (Config.ancient_commands_enable) {
            event.registerServerCommand(CommandIron.instance);
            event.registerServerCommand(CommandWood.instance);
        }
        if (Config.loot_enable)
            event.registerServerCommand(CommandLoot.instance);
    }
}
