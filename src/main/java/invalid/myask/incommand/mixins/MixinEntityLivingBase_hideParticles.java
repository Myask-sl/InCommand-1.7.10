package invalid.myask.incommand.mixins;

import java.util.HashMap;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import invalid.myask.incommand.potion.HiddenPotionEffect;

@Mixin(value = EntityLivingBase.class, priority = 1002)
public class MixinEntityLivingBase_hideParticles { //FIXME
    @Shadow
    private final HashMap activePotionsMap = new HashMap();

    @WrapWithCondition(method = "updatePotionEffects()V",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnParticle(Ljava/lang/String;DDDDDD)V"))
    private boolean inCommand$onlyNoParticles(World instance, String s, double particleName, double x, double y, double z, double velocityX, double velocityY) {
        boolean hiddenEffectsOnly = true;
        for (Object e: activePotionsMap.values()) {
            if (e instanceof PotionEffect) {
                if (!(e instanceof HiddenPotionEffect)) { //TODO: S1DPacketEntityEffect make these
                    hiddenEffectsOnly = false;
                    break;
                }
            }
        }
        return !hiddenEffectsOnly;
    }
}
