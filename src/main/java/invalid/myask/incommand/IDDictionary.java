package invalid.myask.incommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.potion.Potion;
import net.minecraft.world.biome.BiomeGenBase;

public class IDDictionary {

    public static final Map<String, Integer> enchDict = new HashMap<>();
    public static final Map<String, Integer> effDict = new HashMap<>();
    public static final Map<String, Integer> romanDict = new HashMap<>();
    public static final Map<String, Integer> biomeDict = new HashMap<>();
    public static final HashMap<String, Integer> biomeNameMap = new HashMap<>();
    public static ArrayList<String> biomeNameList;

    public static void initEnchDict() {
        enchDict.clear();
        for (int i = 0; i < Enchantment.enchantmentsList.length; i++) {
            if (Enchantment.enchantmentsList[i] != null) {
                String[] key = Enchantment.enchantmentsList[i].getName()
                    .replace(' ', '_').split("\\.", 2);
                int j = (key.length < 2) ? 0 : 1;
                enchDict.put(key[j], i);
            }
        }
    }

    public static void initEffDict() {
        effDict.clear();
        for (int i = 0; i < Potion.potionTypes.length; i++) {
            if (Potion.potionTypes[i] != null) {
                String[] key = Potion.potionTypes[i].getName()
                    .replace(' ', '_').split("\\.", 2);
                int j = (key.length < 2) ? 0 : 1;
                effDict.put(key[j], i);
            }
        }
    }
    public static void initRomanDict() {
        romanDict.clear();
        romanDict.put("I", 1);
        romanDict.put("II", 2);
        romanDict.put("III", 3);
        romanDict.put("IIII", 4);
        romanDict.put("IV", 4);
        romanDict.put("V", 5);
        romanDict.put("VI", 6);
        romanDict.put("VII", 7);
        romanDict.put("VIII", 8);
        romanDict.put("IX", 9);
        romanDict.put("X", 10);
    }
    public static void initBiomeDict() {
        biomeNameMap.clear();
        for (BiomeGenBase biome : BiomeGenBase.getBiomeGenArray()) {
            if (biome != null && !biome.biomeName.isEmpty()) {
                String underscored = biome.biomeName.replace(' ', '_');
                biomeNameMap.put(biome.biomeName, biome.biomeID);
            }
        }
        biomeNameList = new ArrayList<>(biomeNameMap.keySet());
    }

    public static void refreshAllDicts() {
        initEnchDict();
        initEffDict();
        initRomanDict();
        initBiomeDict();
    }

    public static String lookUpEnchant(String arg) {
        if (enchDict == null || enchDict.isEmpty())
            initEnchDict();
        if (enchDict.containsKey(arg)) return String.valueOf(enchDict.get(arg));
        else return arg;
    }

    public static String lookUpEffect(String arg) {
        if (effDict == null || effDict.isEmpty())
            initEffDict();
        if (effDict.containsKey(arg)) return String.valueOf(effDict.get(arg));
        else return arg;
    }

    public static String lookupRoman(String arg) {
        if (romanDict == null || romanDict.isEmpty()) initRomanDict();
        if (romanDict.containsKey(arg)) return String.valueOf(romanDict.get(arg));
        else return arg;
    }

    public static String lookupBiome(String arg) {
        if (biomeDict == null || biomeDict.isEmpty())
            initBiomeDict();
        if (biomeNameMap.containsKey(arg)) return String.valueOf(biomeDict.get(arg));
        else return arg;
    }

    public static List<String> getBiomeList() {
        if (biomeDict == null || biomeDict.isEmpty())
            initBiomeDict();
        return biomeNameList;
    }
}
