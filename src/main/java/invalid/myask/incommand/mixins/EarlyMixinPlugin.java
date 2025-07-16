package invalid.myask.incommand.mixins;

import com.gtnewhorizon.gtnhmixins.IEarlyMixinLoader;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EarlyMixinPlugin implements IEarlyMixinLoader, IFMLLoadingPlugin {
    @Override
    public String getMixinConfig() {
        return "";
    }

    @Override
    public List<String> getMixins(Set<String> loadedCoreMods) {
        ArrayList<String> theList = new ArrayList<>(1);
        theList.add("MixinEntityLivingBase_hideParticles");
        return theList;
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return "";
    }

    @Override
    public String getSetupClass() {
        return "";
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return "";
    }
}
