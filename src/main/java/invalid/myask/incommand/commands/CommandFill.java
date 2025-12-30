package invalid.myask.incommand.commands;

import invalid.myask.incommand.Config;
import net.minecraft.block.Block;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class CommandFill extends InCommandBase {
    public static final CommandFill instance = new CommandFill(Config.fill_permission_level);
    public static final List<String> FILLMODESTRS = new ArrayList<>(6);
    public static final List<String> REPLACEMODESTRS = new ArrayList<>(4);
    public static final String META_REGEX = "\\d+|\\*";
    public static final ItemStack DIAMOND_SHOVEL = new ItemStack(Items.diamond_shovel);
    public static final ItemStack DIAMOND_PICK = new ItemStack(Items.diamond_pickaxe);


    static {
        for (String s : new String[] {"outline", "hollow", "strict", "destroy"}) {
            FILLMODESTRS.add(s); //can't Arrays.toList without J17 stdlib
            REPLACEMODESTRS.add(s);
        }
        FILLMODESTRS.add("replace");
        FILLMODESTRS.add("keep");

        NBTTagCompound b = new NBTTagCompound();
        b.setBoolean("Unbreakable", true);
        DIAMOND_PICK.setTagCompound(b);
        DIAMOND_SHOVEL.setTagCompound(b);
    }
    public enum ComMode {
        OUTLINE("outline"),
        HOLLOW("hollow"),
        STRICT("strict", 2), //"no usages" but the flags are how it works out
        REPLACE("replace"),
        DESTROY("destroy"),
        KEEP("keep"),
        REPLACEANY("catchfire");

        public final String name;
        public final int flags;

        ComMode(String nam) {name = nam; flags = 3;}
        ComMode(String nam, int flags) {name = nam; this.flags = flags;}
    }

    protected CommandFill(int permissionLevel) {
        super(permissionLevel);
    }

    @Override
    public String getCommandName() {
        return "fill";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length < 7) throw new CommandException("commands.fill.failure.args.few");
        Vec3 from = parseCoordinatesCaretOrTilde(sender, args, 0),
               to = parseCoordinatesCaretOrTilde(sender, args, 3);
        Block result = getBlockByText(sender, args[6]), filterBlock = null, innerBlock = result;
        int changedBlocks = 0, maxChanges = Integer.parseInt(sender.getEntityWorld().getWorldInfo()
            .getGameRulesInstance().getGameRuleStringValue("max_block_modifications")),
            maxMeta = Integer.parseInt(sender.getEntityWorld().getWorldInfo()
                .getGameRulesInstance().getGameRuleStringValue("max_block_meta")),
            x0, x1, y0, y1, z0, z1, resultMeta = 0, filterMeta = OreDictionary.WILDCARD_VALUE,
            parsedArg = 7, argAdjust = 0;
        ComMode fillMode = ComMode.REPLACEANY;
        boolean replaceFiltered = false;

        if (from.xCoord < to.xCoord) { x0 = (int) from.xCoord; x1 = (int) to.xCoord; }
        else                         { x0 = (int) to.xCoord; x1 = (int) from.xCoord; }
        if (from.yCoord < to.yCoord) { y0 = (int) from.yCoord; y1 = (int) to.yCoord; }
        else                         { y0 = (int) to.yCoord; y1 = (int) from.yCoord; }
        if (from.zCoord < to.zCoord) { z0 = (int) from.zCoord; z1 = (int) to.zCoord; }
        else                         { z0 = (int) to.zCoord; z1 = (int) from.zCoord; }

        int volume = (x1 - x0 + 1) * (y1 - y0 + 1) * (z1 - z0 + 1);
        if (volume > maxChanges) throw new CommandException("commands.fill.failure.toomany", volume, maxChanges);

        if (args.length > 7) {
            if (args[parsedArg].matches("-?\\d")) {
                resultMeta = parseInt(sender, args[parsedArg]);
                if (resultMeta > maxMeta) throw new CommandException("commands.fill.failure.meta.oob");
                else if (resultMeta < 0) throw new  CommandException("commands.fill.failure.meta.negative");
                parsedArg++;
            } else argAdjust++;
        }
        if (args.length > 8 - argAdjust) {
            if (FILLMODESTRS.contains(args[parsedArg - argAdjust])) {
                for (ComMode c : ComMode.values()) {
                    if (c.name.equalsIgnoreCase(args[parsedArg - argAdjust])) {
                        parsedArg++;
                        if (c != ComMode.REPLACE || args.length > 9 - argAdjust)
                            if (c != ComMode.REPLACE) fillMode = c;
                        break;
                    }
                }
                if (fillMode == ComMode.REPLACEANY) throw new CommandException("commands.fill.failure.fillmode");
            } else throw new CommandException("commands.fill.failure.fillmode");
        }

        if (args.length > 9 - argAdjust) {
            //if (fillMode != ComMode.REPLACE) throw new CommandException("commands.fill.failure.args.paststrat");
            filterBlock = getBlockByText(sender, args[parsedArg++ - argAdjust]);
            replaceFiltered = true;
            if (args.length > parsedArg - argAdjust) {
                if (args[parsedArg - argAdjust].matches(META_REGEX)) {
                    if ("*".equals(args[parsedArg - argAdjust])) ;//filterMeta = OreDictionary.WILDCARD_VALUE; //for clarity
                    else {
                        filterMeta = parseInt(sender, args[parsedArg - argAdjust]);
                        if (filterMeta > maxMeta) throw new CommandException("commands.fill.failure.meta.oob");
                        else if (filterMeta < 0) throw new  CommandException("commands.fill.failure.meta.negative");
                    }
                    parsedArg++;
                } else argAdjust++;
            }
            if (args.length > parsedArg - argAdjust) {
                if (REPLACEMODESTRS.contains(args[parsedArg - argAdjust])) {
                    for (ComMode c : ComMode.values()) {
                        if (c.name.equalsIgnoreCase(args[parsedArg - argAdjust])) {
                            parsedArg++;
                            fillMode = c;
                            break;
                        }
                    }
                } else throw new CommandException("commands.fill.failure.args.replacemode");
            }
        }

        int innerMeta = resultMeta;
        if (fillMode == ComMode.HOLLOW) { innerBlock = Blocks.air;  innerMeta = 0; }

        //Parsing done, Now to perform it!
        boolean isInner, isInX = false, isInY; //always start x == x0
        World w = sender.getEntityWorld();
        Block b;
        int bMeta, flags = fillMode.flags; //accounts for Strict option not sending block updates

        for (int x = x0; x <= x1; x++, isInX = x != x1) {
            isInY = false; //always start y == y0;
            for (int y = y0; y <= y1; y++, isInY = y != y1) {
                for (int z = z0; z <= z1; z++) {
                    isInner = (isInX && isInY && z != z0 && z != z1);
                    if (isInner && fillMode == ComMode.OUTLINE) continue;
                    b = w.getBlock(x, y, z);
                    bMeta = w.getBlockMetadata(x, y, z);
                    if (replaceFiltered)
                        if (filterBlock != b
                            || (filterMeta != OreDictionary.WILDCARD_VALUE && filterMeta != bMeta))
                            continue;
                    if (fillMode == ComMode.KEEP) {
                        if (!b.isAir(w, x, y, z)) continue;
                    }
                    else if (fillMode == ComMode.DESTROY) {
                        String harvestTool = b.getHarvestTool(bMeta);
                        if (b.getHarvestLevel(bMeta) == -1 || harvestTool == null || harvestTool.equalsIgnoreCase("shovel") ||
                            harvestTool.equalsIgnoreCase("pickaxe"))
                            b.dropBlockAsItem(w, x, y, z, bMeta, 0);
                    }
                    if ((b != (isInner ? innerBlock : result))
                        || (bMeta != (isInner ? innerMeta : resultMeta))) {
                        w.setBlock(x, y, z, isInner ? innerBlock : result,
                            isInner ? innerMeta : resultMeta, flags);
                        changedBlocks++;
                    }
                }
            }
        }
        if (changedBlocks > 0) func_152373_a(sender, this, "commands.fill.success", changedBlocks);
        else func_152373_a(sender, this, "commands.fill.failure.nochanges");
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        List<String> result = new ArrayList<>(3);
        int i = args.length, argAdjust = 0; //xyzxyzBrBMS
        if (i > 7 && args[7].matches("\\d")) argAdjust++;
        if (i > 9 + argAdjust && args[9 + argAdjust].matches(META_REGEX)) argAdjust++; //allow metas being optional
        switch(i - argAdjust) {
            case 0, 3: result.add("~ ~ ~"); break;
            case 1, 4: result.add("~ ~"); break;
            case 2, 5: result.add("~"); break;
            case 6, 8: return getListOfStringsFromIterableMatchingLastWord(args, Block.blockRegistry.getKeys());
            case 7: return getListOfStringsFromIterableMatchingLastWord(args, FILLMODESTRS);
            case 9: return getListOfStringsFromIterableMatchingLastWord(args, REPLACEMODESTRS);
        }
        return result;
    }
}
