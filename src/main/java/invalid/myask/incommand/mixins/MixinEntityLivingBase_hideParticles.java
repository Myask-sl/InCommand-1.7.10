package invalid.myask.incommand.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import invalid.myask.incommand.potion.HiddenPotionEffect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.HashMap;

@Mixin(value = EntityLivingBase.class, priority = 404)
public class MixinEntityLivingBase_hideParticles {
    @Shadow
    private final HashMap activePotionsMap = new HashMap();

    @ModifyExpressionValue(method = "updatePotionEffects()V",
    at = @At(value = "INVOKE", target = "Ljava/util/HashMap;isEmpty()Z"))
    private boolean inCommand$onlyNoParticles(boolean original) {
        boolean hiddenEffectsOnly = true;
        for (Object e: activePotionsMap.values()) {
            if (e instanceof PotionEffect) {
                if (!(e instanceof HiddenPotionEffect)) {
                    hiddenEffectsOnly = false;
                }
            }
        }
        return original || hiddenEffectsOnly;
    }
}
