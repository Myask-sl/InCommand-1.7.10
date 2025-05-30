package invalid.myask.incommand.mixins;

import invalid.myask.incommand.ducks.ILootTableGetter;
import net.minecraftforge.common.ChestGenHooks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Mixin(value = ChestGenHooks.class, remap = false)
public class MixinChestGenHooks implements ILootTableGetter {
    @Shadow
    @Final
    private static HashMap<String, ChestGenHooks> chestInfo;

    public List<String> getLootTableNames() {
        List<String> list = new ArrayList<>();
        list.addAll(chestInfo.keySet());
        return list;
    }
}
