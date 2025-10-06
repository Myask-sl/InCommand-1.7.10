package invalid.myask.incommand.commands;

import com.google.common.collect.ImmutableList;
import invalid.myask.incommand.Config;
import invalid.myask.incommand.IDDictionary;
import invalid.myask.incommand.InCommand;
import invalid.myask.incommand.ducks.ILootTableGetter;
import invalid.myask.incommand.ducks.IMobLooter;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.FishingHooks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.minecraft.util.MathHelper.floor_double;

public class CommandLoot extends InCommandBase {
    public static CommandLoot instance = new CommandLoot(Config.loot_permission_level);
    private static final ArrayList<String> leadKeywords = new ArrayList<String>(Arrays.asList("times", "give", "replaceBlock", "spawn"));

    public enum Destination {
        DUMP,
        GIVE,
        REPLACEBLOCK,
        SPAWN
    }
    public enum Target {
        FISH,
        KILL,
        LOOT,
        MINE
    }
    protected CommandLoot(int permissionLevel) {
        super(permissionLevel);
    }

    @Override
    public String getCommandName() {
        return "loot";
    }

    private void throwIfArgsTooShort (int parseIndex, String[] args) {
        if (parseIndex >= args.length) throw new CommandException("commands.loot.failure.arguments.few");
    }
    private boolean notValidTargetString(String targetCandidate) {
        return !"fish".equalsIgnoreCase(targetCandidate) &&
            !"kill".equalsIgnoreCase(targetCandidate) &&
            !"loot".equalsIgnoreCase(targetCandidate) &&
            !"mine".equalsIgnoreCase(targetCandidate);
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        int parseIndex = 0,
            times = 1,
            fishLuck = 0,
            fishLure = 0,
            looting = 0,
            fortune = 0; //defaults
        Destination destination = Destination.GIVE;
        EntityPlayer destinationPlayer = getCommandSenderAsPlayer(sender),
            senderPlayer = destinationPlayer;
        World world = destinationPlayer.getEntityWorld();

        Target targetType = Target.LOOT;
        boolean istargetFishBiome = false;
        BiomeGenBase targetFishBiome = null;
        ChestGenHooks targetLootTable = null;
        String mobName = null, lootTableName = null;
        Entity mobTarget = null;
        double destinationX = destinationPlayer.posX,
            destinationY = destinationPlayer.posY,
            destinationZ = destinationPlayer.posZ,
            targetX = destinationPlayer.posX,
            targetY = destinationPlayer.posY,
            targetZ = destinationPlayer.posZ;

        ArrayList<ItemStack> loot = new ArrayList<>();

        throwIfArgsTooShort(parseIndex, args);
//optionally pick iteration count
        if ("times".equalsIgnoreCase(args[parseIndex])) {
            throwIfArgsTooShort(++parseIndex, args);
            times = parseIntWithMin(sender, args[parseIndex], 0);
            if (times == 0) {
                func_152373_a(
                    sender,
                    this,
                "commands.loot.success.nothing");
                return;
            }
            throwIfArgsTooShort(++parseIndex, args);
        }
//pick destination
        if ("give".equalsIgnoreCase(args[parseIndex])) {
//            destination = Destination.GIVE; //already is, but.
            throwIfArgsTooShort(++parseIndex, args);
            if (notValidTargetString(args[parseIndex])) {
                if ("@s".equalsIgnoreCase(args[parseIndex])) {
                    destinationPlayer = getCommandSenderAsPlayer(sender);
                } else {
                    destinationPlayer = getPlayer(sender, args[parseIndex]);
                }
                throwIfArgsTooShort(++parseIndex, args);
                destinationX = destinationPlayer.posX;
                destinationY = destinationPlayer.posY;
                destinationZ = destinationPlayer.posZ;
            }
        } else if ("replaceBlock".equalsIgnoreCase(args[parseIndex])) {
            destination = Destination.REPLACEBLOCK;
            throwIfArgsTooShort(++parseIndex, args);
            destinationX = func_110666_a(sender, senderPlayer.posX, args[parseIndex]);
            throwIfArgsTooShort(++parseIndex, args);
            destinationY = func_110666_a(sender, senderPlayer.posY, args[parseIndex]);
            throwIfArgsTooShort(++parseIndex, args);
            destinationZ = func_110666_a(sender, senderPlayer.posZ, args[parseIndex]);
            throwIfArgsTooShort(++parseIndex, args);
        } else if ("spawn".equalsIgnoreCase(args[parseIndex])) {
            destination = Destination.SPAWN;
            throwIfArgsTooShort(++parseIndex, args);
            if (notValidTargetString(args[parseIndex])) {
                destinationX = func_110666_a(sender, senderPlayer.posX, args[parseIndex]);
                throwIfArgsTooShort(++parseIndex, args);
                destinationY = func_110666_a(sender, senderPlayer.posY, args[parseIndex]);
                throwIfArgsTooShort(++parseIndex, args);
                destinationZ = func_110666_a(sender, senderPlayer.posZ, args[parseIndex]);
                throwIfArgsTooShort(++parseIndex, args);
            } //else, default to sender's pos
        } else if ("dump".equalsIgnoreCase(args[parseIndex])) {
            destination = Destination.DUMP;
            throwIfArgsTooShort(++parseIndex, args);
        }
        //Destination decided or defaulted, now let's move on to target loot option.
        if (notValidTargetString(args[parseIndex])) throw new CommandException("commands.loot.failure.args.target");
        //else
        if ("fish".equalsIgnoreCase(args[parseIndex])) {
            targetType = Target.FISH;
            if (++parseIndex < args.length) {
                istargetFishBiome = (IDDictionary.getBiomeList().contains(args[parseIndex]));
                if (istargetFishBiome) {
                    targetFishBiome = BiomeGenBase.getBiomeGenArray()[
                        Integer.valueOf(IDDictionary.lookupBiome(args[parseIndex]))];
                }
                else {
                    targetX = func_110666_a(sender, senderPlayer.posX, args[parseIndex]);
                    throwIfArgsTooShort(++parseIndex, args);
                    targetY = func_110666_a(sender, senderPlayer.posY, args[parseIndex]);
                    throwIfArgsTooShort(++parseIndex, args);
                    targetZ = func_110666_a(sender, senderPlayer.posZ, args[parseIndex]);
                    if (!world.blockExists((int)targetX, (int)targetY, (int)targetZ)) {
                        func_152373_a(sender, this, "commands.loot.failure.outside.world", args[parseIndex]);
                        return;
                    }
                    targetFishBiome = world.getBiomeGenForCoords((int) targetX, (int) targetZ);
                }
                if (++parseIndex < args.length) {
                    //TODO: parseTool
                }
            }
        } else if ("kill".equalsIgnoreCase(args[parseIndex])) {
            targetType = Target.KILL;
            throwIfArgsTooShort(++parseIndex, args);
            NBTTagCompound nbt = new NBTTagCompound();
            mobName = args[parseIndex];
            nbt.setString("id", mobName);
            mobTarget = EntityList.createEntityFromNBT(nbt, world);
            if (!(mobTarget instanceof EntityLiving)) {
                func_152373_a(sender, this, "commands.loot.success.nothing.mobloot.nonliving", mobName);
                return;
            }
            else if (!world.getGameRules().getGameRuleBooleanValue("doMobLoot")) {
                func_152373_a(sender, this, "commands.loot.success.nothing.mobloot.gameruled", mobName);
                return;
            }
            if (++parseIndex < args.length) {
                //TODO: parseTool
            }
            mobTarget.captureDrops = true;
        } else if ("loot".equalsIgnoreCase(args[parseIndex])) {
            targetType = Target.LOOT;
            throwIfArgsTooShort(++parseIndex, args);
            if (!((ILootTableGetter) ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST)).getLootTableNames().contains(args[parseIndex])) {
                func_152373_a(sender, this, "commands.loot.failure.args.loottable", args[parseIndex]);
            }
            lootTableName = args[parseIndex];
            targetLootTable = ChestGenHooks.getInfo(args[parseIndex]);
        } else if ("mine".equalsIgnoreCase(args[parseIndex])) {
            targetType = Target.MINE;
            if (++parseIndex < args.length) {
                targetX = parseDouble(sender, args[parseIndex]);
                throwIfArgsTooShort(++parseIndex, args);
                targetY = parseDouble(sender, args[parseIndex]);
                throwIfArgsTooShort(++parseIndex, args);
                targetZ = parseDouble(sender, args[parseIndex]);
                if (++parseIndex < args.length) {
                    //TODO: tool
                }
            }
            if (!world.blockExists(floor_double(targetX), floor_double(targetY), floor_double(targetZ))) {
                func_152373_a(sender, this, "commands.loot.failure.outside.world", args[parseIndex]);
                return;
            }
            //TODO: mineTarget?
        }

        //Now gen the loot.

        for (int iteration = 0; iteration < times; iteration++) {
            switch (targetType) {
                case FISH : loot.add(FishingHooks.getRandomFishable(world.rand, world.rand.nextFloat(), fishLuck, fishLure));
                    break;
                case KILL :  if (mobTarget instanceof EntityLiving mob) {
                    IMobLooter mobLooter = (IMobLooter) mob;
                    mob.captureDrops = true;
                    mobLooter.commandLoot$clearEquipment();
                    mobLooter.commandLoot$armorUp();
                    mobLooter.commandLoot$enchantUp();
                    mobLooter.commandLoot$fewDrop(true, looting);
                    mobLooter.commandLoot$equipDrop(true, looting);

                    int roll = world.rand.nextInt(200) - looting;
                    if (roll < 5) {
                        mobLooter.commandLoot$rareDrop(roll <= 0 ? 1 : 0);
                    }
                    for (EntityItem eItem : mob.capturedDrops) {
                        loot.add(eItem.getEntityItem());
                    }
                    mob.capturedDrops.clear();
                    }
                    break;
                case LOOT:
                    InventoryBasic billOfGoods = new InventoryBasic("bill", false, 27);
                    WeightedRandomChestContent.generateChestContents(world.rand, targetLootTable.getItems(world.rand),
                        billOfGoods, targetLootTable.getCount(world.rand));
                    for (int i = 0; i < 27; i++) {
                        loot.add(billOfGoods.getStackInSlotOnClosing(i));
                    } break;
                case MINE:
                    //TODO
            }
        }

        switch (targetType) {
            case FISH -> func_152373_a(sender, this, "commands.loot.success.fish", times, targetFishBiome);
            case KILL -> func_152373_a(sender, this, "commands.loot.success.kill", times, mobName);
            case LOOT -> func_152373_a(sender, this, "commands.loot.success.loot", times, lootTableName);
            case MINE -> func_152373_a(sender, this, "commands.loot.success.mine", times, ""); //TODO: blockName
        }

        IInventory destinationInventory = null;
        if (destination == Destination.REPLACEBLOCK) {
            TileEntity te = world.getTileEntity(floor_double(destinationX), floor_double(destinationY), floor_double(destinationZ));
            if (te instanceof IInventory inv) destinationInventory = inv;
            else {
                func_152373_a(sender, this, "commands.loot.failure.insert.noninventory", floor_double(destinationX), floor_double(destinationY), floor_double(destinationZ));
                return;
            }
        }

        //Now deliver the loot.
        int i = 0;
        for (ItemStack stack : loot) {
            switch (destination) {
                case GIVE, SPAWN: //give is just spawning inside a player.
                    if (stack != null) {
                        EntityItem entityItem = new EntityItem(world, destinationX, destinationY, destinationZ, stack);
                        if (destination == Destination.GIVE) entityItem.delayBeforeCanPickup = 0;
                        world.spawnEntityInWorld(entityItem);
                    }
                    break;
                case REPLACEBLOCK:
                    destinationInventory.setInventorySlotContents(i++, stack);
                    //TODO: packed version.
                    break;
                case DUMP:
                    if (stack != null || Config.loot_dump_empties)
                        InCommand.LOG.info(String.format("/loot item: %s with tags %s", stack == null ? "(empty)" : stack.toString(),
                            stack == null || stack.getTagCompound() == null ? "(none)" : stack.getTagCompound().toString()));
            }
        }

        //Now report the delivery.


        switch(destination) {
            case DUMP -> func_152373_a(sender, this, "commands.loot.success.dump");
            case GIVE -> func_152373_a(sender, this, "commands.loot.success.give", destinationPlayer.getCommandSenderName(), destinationX, destinationY, destinationZ);
            case REPLACEBLOCK -> func_152373_a(sender, this, "commands.loot.success.insert", destinationInventory.getInventoryName(), destinationX, destinationY, destinationZ);
            case SPAWN -> func_152373_a(sender, this, "commands.loot.success.spawn", destinationX, destinationY, destinationZ);
        }
    }

    protected boolean wantsCoordinates (String arg) {
        return ("replaceBlock".equalsIgnoreCase(arg) ||
            "spawn".equalsIgnoreCase(arg) ||
            "mine".equalsIgnoreCase(arg) ||
            "fish".equalsIgnoreCase(arg));
    }

    protected void addIterable (List<String> list, Iterable<String> in) {
        for (String s: in) {
            list.add(s);
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        List<String> theList = new ArrayList<String>(5);
        if (args.length < 1) return theList;
        else if (args.length == 1) {
            return getListOfStringsFromIterableMatchingLastWord(args, leadKeywords);
        }
        if (args.length == 3 && "times".equalsIgnoreCase(args[0])) {
            theList = getListOfStringsFromIterableMatchingLastWord(args, leadKeywords);
            theList.remove("times");
            return theList;
        }


        if ("fish".equalsIgnoreCase(args[args.length - 2]))
            theList.addAll(getListOfStringsFromIterableMatchingLastWord(args,
                IDDictionary.getBiomeList()));
        else if ("kill".equalsIgnoreCase(args[args.length - 2]))
            theList.addAll(getListOfStringsFromIterableMatchingLastWord(args,
                EntityList.func_151515_b()));
        else if ("loot".equalsIgnoreCase(args[args.length - 2]))
            theList.addAll(getListOfStringsFromIterableMatchingLastWord(args,
                ((ILootTableGetter) ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST)).getLootTableNames()));
//        else if ("mine".equalsIgnoreCase(args[args.length - 2]))
//            return ImmutableList.of("mainHand");
        else if ("give".equalsIgnoreCase(args[args.length - 2]))
            theList.addAll(getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()));
        else if (wantsCoordinates(args[args.length - 2]))
            theList.addAll(ImmutableList.of("~ ", "~ ~ ~ "));
        if (args.length < 3) return theList;
        if (wantsCoordinates(args[args.length - 3]))
            theList.addAll(ImmutableList.of("~ ", "~ ~ "));
        //if (validTargetString(args[args.length - 3]))
        //  return ImmutableList.of("mainHand");
        if (args.length < 4) return theList;
        if (wantsCoordinates(args[args.length - 3]))
            theList.addAll(ImmutableList.of("~ "));
        return theList;
    }
}
