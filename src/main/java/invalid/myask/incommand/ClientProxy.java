package invalid.myask.incommand;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.MinecraftForge;

import invalid.myask.incommand.commands.CommandCoordTest;
import invalid.myask.incommand.commands.fake.CommandFakeEffect;
import invalid.myask.incommand.commands.fake.CommandFakeEnchant;

public class ClientProxy extends CommonProxy {

    // Override CommonProxy methods here, if you want a different behaviour on the client (e.g. registering renders).
    // Don't forget to call the super methods as well.


    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);

        net.minecraftforge.client.ClientCommandHandler.instance.registerCommand(CommandCoordTest.instance);

        net.minecraftforge.client.ClientCommandHandler.instance.registerCommand(CommandFakeEnchant.instance);
        net.minecraftforge.client.ClientCommandHandler.instance.registerCommand(CommandFakeEffect.instance);
        MinecraftForge.EVENT_BUS.register(FakeCommandInterceptor.instance);
    }
}
