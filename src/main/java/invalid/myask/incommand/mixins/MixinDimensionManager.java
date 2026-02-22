package invalid.myask.incommand.mixins;

import invalid.myask.incommand.IDDictionary;
import net.minecraftforge.common.DimensionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = DimensionManager.class, remap = false)
public class MixinDimensionManager {
    @Inject(method = "registerDimension(II)V", at = @At("HEAD"))
    private static void noteDimension(int id, int providerType, CallbackInfo ci) {
        switch (id) {
            case 0: case -1: case 1: break;
            default: IDDictionary.noteDim(id, providerType);
        }
    }
}
