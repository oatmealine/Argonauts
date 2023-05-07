package earth.terrarium.argonauts.forge;

import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.client.ArgonautsClient;
import earth.terrarium.argonauts.common.commands.ModCommands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(Argonauts.MOD_ID)
public class ArgonautsForge {
    public ArgonautsForge() {
        Argonauts.init();

        var bus = MinecraftForge.EVENT_BUS;
        bus.addListener(ArgonautsForge::onClientSetup);
        bus.addListener(ArgonautsForge::onPlayerLoggedIn);
        bus.addListener(ArgonautsForge::registerCommands);
    }

    private static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            Argonauts.onPlayerJoin(player);
        }
    }

    public static void onClientSetup(FMLClientSetupEvent event) {
        ArgonautsClient.init();
    }

    private static void registerCommands(RegisterCommandsEvent event) {
        ModCommands.register(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
    }
}