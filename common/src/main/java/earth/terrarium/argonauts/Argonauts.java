package earth.terrarium.argonauts;

import dev.architectury.event.events.common.PlayerEvent;
import earth.terrarium.argonauts.api.guild.Guild;
import earth.terrarium.argonauts.api.guild.GuildApi;
import earth.terrarium.argonauts.common.constants.ConstantComponents;
import earth.terrarium.argonauts.common.network.NetworkHandler;
import earth.terrarium.argonauts.common.network.messages.ClientboundSyncGuildsPacket;
import earth.terrarium.argonauts.common.utils.ModUtils;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.player.Player;

import java.util.HashSet;
import java.util.Set;

public class Argonauts {
    public static final String MOD_ID = "argonauts";

    public static void init() {
        NetworkHandler.init();

        PlayerEvent.PLAYER_ADVANCEMENT.register(((player, advancement) -> {
            Guild guild = GuildApi.API.get(player);
            if (guild == null) return;
            if (!guild.syncAdvancements()) return;
            PlayerList players = player.server.getPlayerList();

            guild.members().forEach(member -> {
                ServerPlayer otherPlayer = players.getPlayer(member.profile().getId());
                if (otherPlayer == null) return;

                AdvancementProgress advancementprogress = otherPlayer.getAdvancements().getOrStartProgress(advancement);
                if (!advancementprogress.isDone()) {
                    for(String s : advancementprogress.getRemainingCriteria()) {
                        otherPlayer.getAdvancements().award(advancement, s);
                    }
                }
            });
        }));
    }

    public static void onPlayerJoin(Player player) {
        if (player.level().isClientSide()) return;
        NetworkHandler.CHANNEL.sendToPlayer(new ClientboundSyncGuildsPacket(new HashSet<>(GuildApi.API.getAll(player.getServer())), Set.of()), player);
        Guild guild = GuildApi.API.get((ServerPlayer) player);
        if (guild == null) return;

        Component motd = guild.settings().motd();
        if (!motd.getString().isEmpty()) {
            player.displayClientMessage(ConstantComponents.MOTD_HEADER, false);
            player.displayClientMessage(ModUtils.getParsedComponent(motd, (ServerPlayer) player), false);
            player.displayClientMessage(ConstantComponents.LINE, false);
        }

        if (guild.syncAdvancements()) {
            PlayerList players = ((ServerPlayer) player).server.getPlayerList();

            guild.members().forEach(member -> {
                ServerPlayer otherPlayer = players.getPlayer(member.profile().getId());
                if (otherPlayer == null) return;

                for (Advancement advancement : player.getServer().getAdvancements().getAllAdvancements()) {
                    // sync player advancements to otherPlayer
                    AdvancementProgress advancementProgress = ((ServerPlayer) player).getAdvancements().getOrStartProgress(advancement);
                    if (advancementProgress.isDone()) {
                        AdvancementProgress otherAdvancementProgress = ((ServerPlayer) player).getAdvancements().getOrStartProgress(advancement);
                        if (!otherAdvancementProgress.isDone()) {
                            for (String s : otherAdvancementProgress.getRemainingCriteria()) {
                                otherPlayer.getAdvancements().award(advancement, s);
                            }
                        }
                    }

                    // sync otherPlayer advancements to player
                    AdvancementProgress otherAdvancementProgress = otherPlayer.getAdvancements().getOrStartProgress(advancement);
                    if (otherAdvancementProgress.isDone()) {
                        AdvancementProgress advancementProgress_ = otherPlayer.getAdvancements().getOrStartProgress(advancement);
                        if (!advancementProgress_.isDone()) {
                            for(String s : advancementProgress_.getRemainingCriteria()) {
                                ((ServerPlayer) player).getAdvancements().award(advancement, s);
                            }
                        }
                    }
                }
            });
        }
    }
}