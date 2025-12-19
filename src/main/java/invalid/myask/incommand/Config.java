package invalid.myask.incommand;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class Config {

    public static boolean ancient_commands_enable = false;
    public static boolean loot_enable = true;

    public static int iron_permission_level = 2;
    public static int wood_permission_level = 2;
    public static int loot_permission_level = 2;

    public static int ancient_drops_delay = 12000;

    public static boolean loot_dump_empties = true;
    public static boolean enchant_clear_enable = true;
    public static boolean verbose_enchant = true;


    public static void synchronizeConfiguration(File configFile) {
        Configuration configuration = new Configuration(configFile);
//Command Enables
        ancient_commands_enable = configuration.getBoolean("ancient_commands_enable","enable",
            ancient_commands_enable,"Enable really old commands (/iron, /wood, /solid)");
        loot_enable = configuration.getBoolean("loot_enable", "enable",
            loot_enable, "enable /loot command");
//Permission levels
        iron_permission_level = configuration.getInt("iron_permission_level","permissions",
            iron_permission_level,0,5,
            "Permission level for /iron [drop four saplings] (5 prevents use)");
        wood_permission_level = configuration.getInt("wood_permission_level","permissions",
            wood_permission_level,0,5,
            "Permission level for /wood [drop four saplings] (5 prevents use)");

        loot_permission_level = configuration.getInt("loot_permission_level","permissions",
            loot_permission_level,0,5,"Permission level for /loot (5 prevents use)");
        rotate_permission_level = configuration.getInt("rotate_permission_level","permissions",
            rotate_permission_level,0,5,"Permission level for /rotate (5 prevents use)");

        killother_permission_level = configuration.getInt("killother_permission_level","permissions",
            killother_permission_level,0,5,"Permission level for /killother (5 prevents use)");;
        rotateself_permission_level = configuration.getInt("rotateself_permission_level","permissions",
            rotateself_permission_level,0,5,"Permission level for /rotateself (5 prevents use)");;

//command options
        ancient_drops_delay = configuration.getInt("ancient_drops_delay","ancient",ancient_drops_delay,
            0,Integer.MAX_VALUE,
            "Minimum delay between /iron, /wood uses (in ticks, default 12000 = 10 minutes)");

        loot_dump_empties = configuration.getBoolean("loot_dump_empties", "loot",
            loot_dump_empties, "/loot destination:dump logs empty itemstacks");

        enchant_clear_enable = configuration.getBoolean("enchant_clear_enable", "enchant",
            enchant_clear_enable, "/enchant clear to clear enchants enable");

        verbose_enchant = configuration.getBoolean("verbose_enchant", "enchant",
            verbose_enchant, "/enchant reports command sender, target, item, and enchantment on success.");


        if (configuration.hasChanged()) {
            configuration.save();
        }
    }
}
