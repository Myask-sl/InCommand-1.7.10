package invalid.myask.incommand.world;

import net.minecraft.command.CommandException;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class TeleporterNoPortal extends Teleporter {
    private static TeleporterNoPortal toOverworld;
    private static TeleporterNoPortal toNether;

    private TeleporterNoPortal(WorldServer worldIn) {
        super(worldIn);
    }

    public static Teleporter to(int id) {
        return switch (id) {
            case 0 -> toLand();
            case 1 -> toHell();
            default -> null;
        };
    }

    private static Teleporter toHell() {
        MinecraftServer server = MinecraftServer.getServer();
        if (!server.getAllowNether()) throw new CommandException("commands.warp.failure.nether.forbidden");
        if (toNether == null)
            toNether = new TeleporterNoPortal(server.worldServerForDimension(-1));
        return toNether;
    }

    private static Teleporter toLand() {
        if (toOverworld == null)
            toOverworld = new TeleporterNoPortal(MinecraftServer.getServer().worldServerForDimension(0));
        return toOverworld;
    }

    @Override
    public void placeInPortal(Entity entity, double x, double y, double z, float yaw) {
        placeInExistingPortal(entity, x, y, z, yaw);
    }

    @Override
    public boolean makePortal(Entity vic) {
        return false;
    }

    @Override
    public boolean placeInExistingPortal(Entity entity, double x, double y, double z, float yaw) {
        entity.setLocationAndAngles(x, y, z, yaw, 0);
        entity.motionZ = entity.motionY = entity.motionX = 0;
        return true;
    }
}
