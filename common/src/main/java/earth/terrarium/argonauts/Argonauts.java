package earth.terrarium.argonauts;

import earth.terrarium.argonauts.api.guild.Guild;
import earth.terrarium.argonauts.api.guild.GuildApi;
import earth.terrarium.argonauts.common.constants.ConstantComponents;
import earth.terrarium.argonauts.common.network.NetworkHandler;
import earth.terrarium.argonauts.common.network.messages.ClientboundSyncGuildsPacket;
import earth.terrarium.argonauts.common.utils.ModUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.HashSet;
import java.util.Set;

public class Argonauts {
    public static final String MOD_ID = "argonauts";

    public static void init() {
        NetworkHandler.init();
    }

    public static void onPlayerJoin(Player player) {
        if (player.level().isClientSide()) return;
        NetworkHandler.CHANNEL.sendToPlayer(new ClientboundSyncGuildsPacket(new HashSet<>(GuildApi.API.getAll(player.getServer())), Set.of()), player);
        Guild guild = GuildApi.API.get((ServerPlayer) player);
        if (guild == null) return;
        Component motd = guild.settings().motd();
        if (motd.getString().isEmpty()) return;

        player.displayClientMessage(ConstantComponents.MOTD_HEADER, false);
        player.displayClientMessage(ModUtils.getParsedComponent(motd, (ServerPlayer) player), false);
        player.displayClientMessage(ConstantComponents.LINE, false);
    }
}