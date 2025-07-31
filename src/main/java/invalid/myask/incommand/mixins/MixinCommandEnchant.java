package invalid.myask.incommand.mixins;

import java.util.List;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandEnchant;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StatCollector;

import invalid.myask.incommand.Config;
import invalid.myask.incommand.IDDictionary;

@Mixin(CommandEnchant.class)
public abstract class MixinCommandEnchant extends CommandBase {

    @Inject(
        method = "processCommand(Lnet/minecraft/command/ICommandSender;[Ljava/lang/String;)V",
        at = @At("HEAD"),
        cancellable = true)
//            target = "Lnet/minecraft/command/CommandBase;getPlayer(Lnet/minecraft/command/ICommandSender;Ljava/lang/String;)Lnet/minecraft/entity/player/EntityPlayerMP;"))
    private void fixArgs(CallbackInfo nameAndNumber, @Local(argsOnly = true)ICommandSender sender,
                         @Local(argsOnly = true) String[] args) {
        if (args.length >= 2) {
            if (Config.enchant_clear_enable && "clear".equalsIgnoreCase(args[1]))
                args[1] = "-0";
            else args[1] = IDDictionary.lookUpEnchant(args[1]);

            if (args.length >= 3)
                args[2] = IDDictionary.lookupRoman(args[2]);

            ItemStack itemstack = getCommandSenderAsPlayer(sender).getCurrentEquippedItem();
            if (itemstack == null) return;
            NBTTagList enchantList = itemstack.getEnchantmentTagList();
            int targetID = parseInt(sender, args[1]);
            if (Config.enchant_clear_enable && "-0".equalsIgnoreCase(args[1])) {
                if (enchantList != null) {
                    /*
                    for (int k = enchantList.tagCount() - 1; k >= 0; k--) {
                        enchantList.removeTag(k);
                    }
                     */
                    itemstack.getTagCompound().removeTag("ench");
                    func_152373_a(sender, this, "commands.enchant.clear.success");
                }
                nameAndNumber.cancel();
            } else if (EnchantmentHelper.getEnchantmentLevel(targetID, itemstack) > 0) {
                for (int i = 0; i < enchantList.tagCount(); i++) {
                    if (enchantList.getCompoundTagAt(i).getShort("id") == targetID) { //TODO: check endlessIDs for issue.
                        enchantList.removeTag(i);
                    }
                }
                if (enchantList.tagCount() == 0)
                    itemstack.getTagCompound().removeTag("ench");
            }
            if (args.length >= 3 && parseInt(sender, args[2]) == 0) {
                func_152373_a(sender, this, "commands.enchant.zero.success");
                nameAndNumber.cancel();
            }
        }
    }

    @WrapOperation(method = "processCommand",
        at= @At(value = "INVOKE",
        target = "Lnet/minecraft/command/CommandEnchant;func_152373_a(Lnet/minecraft/command/ICommandSender;Lnet/minecraft/command/ICommand;Ljava/lang/String;[Ljava/lang/Object;)V")
    )
    private void improveMessage (ICommandSender iCommandSender, ICommand iCommand, String s, Object[] objects, Operation<Void> original,
                                 @Local ItemStack itemstack, @Local Enchantment enchantment, @Local(ordinal = 1) int level) {
        original.call(iCommandSender, iCommand, "commands.enchant.enchant.verbose", new Object[] {
            iCommandSender.getCommandSenderName(),
            StatCollector.translateToLocal(itemstack.getUnlocalizedName()),
            enchantment.getTranslatedName(level)});
        //Enchanting by %1$s of %2$s with %3$s succeeded
    }


    @Inject(
        method = "addTabCompletionOptions(Lnet/minecraft/command/ICommandSender;[Ljava/lang/String;)Ljava/util/List;",
        at = @At("HEAD"), cancellable = true)
    private void tabNames(CallbackInfoReturnable<List<String>> nameAndNumber, @Local(argsOnly = true) String[] args) {
        if (IDDictionary.enchDict.isEmpty()) {
            IDDictionary.initEnchDict();
        }
        if (args.length == 2) {
            nameAndNumber
            .setReturnValue(getListOfStringsFromIterableMatchingLastWord(args, IDDictionary.enchDict.keySet()));
            nameAndNumber.cancel();
        }
    }
}
