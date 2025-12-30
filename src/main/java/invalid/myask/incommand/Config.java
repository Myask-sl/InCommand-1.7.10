package invalid.myask.incommand;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class Config {

    public static boolean ancient_commands_enable = false;
    public static boolean loot_enable = true;
    public static boolean rotate_enable = true;
    public static boolean killother_enable = true;
    public static boolean fill_clone_enable = true;

    public static boolean killself_aliases = true;
    public static boolean killother_alias_kill = false;

    public static int iron_permission_level = 2;
    public static int wood_permission_level = 2;

    public static int loot_permission_level = 2;
    public static int rotate_permission_level = 2;

    public static int killother_permission_level = 2;
    public static int rotateself_permission_level = 0;
    public static int die_permission_level = 0;

    public static int clone_permission_level = 2;
    public static int fill_permission_level = 2;

    public static int coordtest_permission_level = 0;

    public static int ancient_drops_delay = 12000;

    public static boolean loot_dump_empties = true;
    public static boolean enchant_clear_enable = true;
    public static boolean verbose_enchant = true;
    public static double rotate_facing_min_dist = 0.001;
    public static double rotate_facing_min_dist_square = 0.001;


    public static void synchronizeConfiguration(File configFile) {
        Configuration configuration = new Configuration(configFile);
//Command Enables
        ancient_commands_enable = configuration.getBoolean("ancient_commands_enable","enable",
            ancient_commands_enable,"Enable really old commands (/iron, /wood, /solid)");
        loot_enable = configuration.getBoolean("loot_enable", "enable",
            loot_enable, "enable /loot command");
        rotate_enable = configuration.getBoolean("rotate_enable", "enable",
            rotate_enable, "enable /rotate command");
        killother_enable = configuration.getBoolean("killother_enable", "enable",
            killother_enable, "enable /killother command");
        fill_clone_enable = configuration.getBoolean("fill_clone_enable", "enable",
            fill_clone_enable, "enable /fill and /clone commands");
//Aliases
        killself_aliases = configuration.getBoolean("killself_aliases", "alias",
            killself_aliases, "Add aliases /die and /killself to vanilla /kill");
        killother_alias_kill = configuration.getBoolean("killother_alias_kill", "alias",
            killother_alias_kill, "Make /killother invoked on /kill instead of vanilla");

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
            killother_permission_level,0,5,"Permission level for /killother (5 prevents use)");
        rotateself_permission_level = configuration.getInt("rotateself_permission_level","permissions",
            rotateself_permission_level,0,5,"Permission level for /rotateself (5 prevents use)");
        die_permission_level = configuration.getInt("die_permission_level","permissions",
            die_permission_level,0,5,"Permission level for /die, /killself (5 prevents use). Note that this will not override vanilla /kill permission level.");

        clone_permission_level = configuration.getInt("clone_permission_level","permissions",
            clone_permission_level,0,5,"Permission level for /clone (5 prevents use)");
        fill_permission_level = configuration.getInt("fill_permission_level","permissions",
            fill_permission_level,0,5,"Permission level for /fill (5 prevents use)");

        coordtest_permission_level = configuration.getInt("coordtest_permission_level","permissions",
            coordtest_permission_level,0,5,"Permission level for /coordtest (5 prevents use)");

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

        rotate_facing_min_dist = configuration.getFloat("rotate_facing_min_dist", "rotate",
            (float) rotate_facing_min_dist, -1, Float.MAX_VALUE, "/rotate facing minimum distance (negative to leave unbounded)");

        rotate_facing_min_dist_square = rotate_facing_min_dist * rotate_facing_min_dist;


        if (configuration.hasChanged()) {
            configuration.save();
        }
    }
}
