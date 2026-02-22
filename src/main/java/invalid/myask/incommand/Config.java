package invalid.myask.incommand;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class Config {

    public static final String CAT_PERMISSIONS = "permissions";
    public static final String CAT_ALIAS = "alias";
    public static final String CAT_ENABLE = "enable";

    public static boolean ancient_commands_enable = false;
    public static boolean loot_enable = true;
    public static boolean rotate_enable = true;
    public static boolean killother_enable = true;
    public static boolean fill_clone_enable = true;
    public static boolean warp_enable = true;

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

    public static int warp_permission_level = 2; //vanilla April Fools 0

    public static int coordtest_permission_level = 0;

    public static int ancient_drops_delay = 12000;

    public static boolean loot_dump_empties = true;
    public static boolean enchant_clear_enable = true;
    public static boolean verbose_enchant = true;
    public static double rotate_facing_min_dist = 0.001;
    public static double rotate_facing_min_dist_square = 0.001;

    public static boolean modid_dimension_dictionary = true;


    public static void synchronizeConfiguration(File configFile) {
        Configuration configuration = new Configuration(configFile);
//Command Enables
        ancient_commands_enable = configuration.getBoolean("ancient_commands_enable", CAT_ENABLE,
            ancient_commands_enable,"Enable really old commands (/iron, /wood, /solid)");
        loot_enable = configuration.getBoolean("loot_enable", CAT_ENABLE,
            loot_enable, "enable /loot command");
        rotate_enable = configuration.getBoolean("rotate_enable", CAT_ENABLE,
            rotate_enable, "enable /rotate command");
        killother_enable = configuration.getBoolean("killother_enable", CAT_ENABLE,
            killother_enable, "enable /killother command");
        fill_clone_enable = configuration.getBoolean("fill_clone_enable", CAT_ENABLE,
            fill_clone_enable, "enable /fill and /clone commands");
        warp_enable = configuration.getBoolean("warp_enable", CAT_ENABLE,
            warp_enable, "enable /warp command");
//Aliases
        killself_aliases = configuration.getBoolean("killself_aliases", CAT_ALIAS,
            killself_aliases, "Add aliases /die and /killself to vanilla /kill");
        killother_alias_kill = configuration.getBoolean("killother_alias_kill", CAT_ALIAS,
            killother_alias_kill, "Make /killother invoked on /kill instead of vanilla");

//Permission levels
        iron_permission_level = configuration.getInt("iron_permission_level", CAT_PERMISSIONS,
            iron_permission_level,0,5,
            "Permission level for /iron [drop four saplings] (5 prevents use)");
        wood_permission_level = configuration.getInt("wood_permission_level", CAT_PERMISSIONS,
            wood_permission_level,0,5,
            "Permission level for /wood [drop four saplings] (5 prevents use)");

        loot_permission_level = configuration.getInt("loot_permission_level", CAT_PERMISSIONS,
            loot_permission_level,0,5,"Permission level for /loot (5 prevents use)");
        rotate_permission_level = configuration.getInt("rotate_permission_level", CAT_PERMISSIONS,
            rotate_permission_level,0,5,"Permission level for /rotate (5 prevents use)");

        killother_permission_level = configuration.getInt("killother_permission_level", CAT_PERMISSIONS,
            killother_permission_level,0,5,"Permission level for /killother (5 prevents use)");
        rotateself_permission_level = configuration.getInt("rotateself_permission_level", CAT_PERMISSIONS,
            rotateself_permission_level,0,5,"Permission level for /rotateself (5 prevents use)");
        die_permission_level = configuration.getInt("die_permission_level", CAT_PERMISSIONS,
            die_permission_level,0,5,"Permission level for /die, /killself (5 prevents use). Note that this will not override vanilla /kill permission level.");

        clone_permission_level = configuration.getInt("clone_permission_level", CAT_PERMISSIONS,
            clone_permission_level,0,5,"Permission level for /clone (5 prevents use)");
        fill_permission_level = configuration.getInt("fill_permission_level", CAT_PERMISSIONS,
            fill_permission_level,0,5,"Permission level for /fill (5 prevents use)");

        coordtest_permission_level = configuration.getInt("coordtest_permission_level", CAT_PERMISSIONS,
            coordtest_permission_level,0,5,"Permission level for /coordtest (5 prevents use)");

        warp_permission_level = configuration.getInt("warp_permission_level", CAT_PERMISSIONS,
            warp_permission_level, 0, 5, "Permission level for /warp (5 prevents use)");

//command options
        ancient_drops_delay = configuration.getInt("ancient_drops_delay","ancient", ancient_drops_delay,
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

        modid_dimension_dictionary = configuration.getBoolean("modid_dimension_dictionary", "warp",
            modid_dimension_dictionary, "Whether to also allow modid:dimension_name for /warp");

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }
}
