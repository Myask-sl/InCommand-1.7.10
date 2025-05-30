package invalid.myask.incommand.mixins;

import invalid.myask.incommand.ducks.IMobLooter;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityLiving.class)
public abstract class MixinEntityLiving_lootpeek extends EntityLivingBase implements IMobLooter {
    public MixinEntityLiving_lootpeek(World w) {
        super(w);
    }

    @Shadow
    protected abstract void dropEquipment(boolean p_82160_1_, int p_82160_2_);
    @Override
    public void commandLoot$equipDrop(boolean pK, int looting) {
        dropEquipment(pK, looting);
    }

    @Shadow
    private ItemStack[] equipment;
    @Override
    public void commandLoot$clearEquipment() {
        for (int i = 0; i < equipment.length; i++) {
            equipment[i] = null;
        }
    }

    @Shadow
    protected abstract void addRandomArmor();
    @Override
    public void commandLoot$armorUp() {
        addRandomArmor();
    }

    @Shadow
    protected abstract void enchantEquipment();
    @Override
    public void commandLoot$enchantUp() {
        enchantEquipment();
    }

    @Shadow
    protected abstract void dropFewItems(boolean p_70628_1_, int p_70628_2_);
    @Override
    public void commandLoot$fewDrop(boolean pK, int looting) {
        dropFewItems(pK, looting);
    }

    @Override
    public void commandLoot$rareDrop(int bonus) {
        dropRareDrop(bonus);
    }
}
