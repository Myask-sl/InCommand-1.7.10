package invalid.myask.incommand.mixins;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gtnewhorizon.gtnhmixins.IEarlyMixinLoader;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

import invalid.myask.incommand.Tags;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.common.config.Configuration;

//TODO: not used yet
//@IFMLLoadingPlugin.Name("InCommandEarlyMixinPlugin")
//@IFMLLoadingPlugin.MCVersion("1.7.10")
public class InCommandEarlyMixinPlugin implements IEarlyMixinLoader, IFMLLoadingPlugin {
    @Override
    public String getMixinConfig() {
        return "mixins." + Tags.MODID + ".early.json";
    }

    @Override
    public List<String> getMixins(Set<String> loadedCoreMods) {
        File file = new File(Launch.minecraftHome, "config" + File.separator + "incommand.cfg");
        Configuration configuration = new Configuration(file); //todo

        ArrayList<String> theList = new ArrayList<>(1);
        //theList.add("MixinEntityLivingBase_hideParticles");
        return theList;
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
