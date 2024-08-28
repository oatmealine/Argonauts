package earth.terrarium.argonauts.client.fabric;

import earth.terrarium.argonauts.client.ArgonautsClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

public class ArgonautsClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ArgonautsClient.init();
        ClientTickEvents.START_CLIENT_TICK.register(client -> ArgonautsClient.clientTick());
        KeyBindingHelper.registerKeyBinding(ArgonautsClient.KEY_OPEN_GUILD_CHAT);
    }
}
