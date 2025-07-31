package invalid.myask.incommand;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import invalid.myask.incommand.commands.fake.CommandTabOnly;
import net.minecraftforge.event.CommandEvent;

public class FakeCommandInterceptor {
    static FakeCommandInterceptor instance = new FakeCommandInterceptor();
    @SubscribeEvent
    public void axe (CommandEvent event) {
        if (event.command instanceof CommandTabOnly) event.setCanceled(true);
    }
}
