package invalid.myask.incommand.mixins;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandEffect;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.sugar.Local;

import invalid.myask.incommand.IDDictionary;

@Mixin(CommandEffect.class)
public abstract class MixinCommandEffect extends CommandBase {

    @Inject(
        method = "processCommand(Lnet/minecraft/command/ICommandSender;[Ljava/lang/String;)V",
        at = @At("HEAD"))
//           , target = "Lnet/minecraft/command/CommandBase;getPlayer(Lnet/minecraft/command/ICommandSender;Ljava/lang/String;)Lnet/minecraft/entity/player/EntityPlayerMP;"))
    private void fixArgs(CallbackInfo nameAndNumber, @Local(argsOnly = true) String[] args) {
        if (args.length >= 2)
            args[1] = String.valueOf(IDDictionary.lookUpEffect(args[1]));
    }

    @Inject(
        method = "addTabCompletionOptions(Lnet/minecraft/command/ICommandSender;[Ljava/lang/String;)Ljava/util/List;",
        at = @At("HEAD"), cancellable = true)
    private void tabNames(CallbackInfoReturnable<List<String>> nameAndNumber, @Local(argsOnly = true) String[] args) {
        if (IDDictionary.effDict.isEmpty()) {
            IDDictionary.initEffDict();
        }
        if (args.length == 2) {
            nameAndNumber.setReturnValue(getListOfStringsFromIterableMatchingLastWord(args, IDDictionary.effDict.keySet()));
            nameAndNumber.cancel();
        }
    }
}
