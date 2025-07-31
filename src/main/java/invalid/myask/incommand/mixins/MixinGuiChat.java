package invalid.myask.incommand.mixins;

import invalid.myask.incommand.commands.fake.CommandTabOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.command.ICommand;
import net.minecraftforge.client.ClientCommandHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(GuiChat.class)
public class MixinGuiChat {
    @ModifyArg(method = "func_146403_a(Ljava/lang/String;)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityClientPlayerMP;sendChatMessage(Ljava/lang/String;)V"))
    private String preProcessCommand (String original) {
        if (!original.startsWith("/")) return original;
        String[] tokens = original.split(" ");
        if (tokens[0].startsWith("/")) {
            ICommand ic = ClientCommandHandler.instance.getCommands().get(tokens[0].substring(1));
            if (ic instanceof CommandTabOnly theCommand) {
                theCommand.processArguments(Minecraft.getMinecraft().thePlayer, tokens);
                original = String.join(" ", tokens);
            }
        }
        return original;
    }
}
