package earth.terrarium.argonauts.common.handlers.party;

import com.teamresourceful.resourcefullib.common.utils.CommonUtils;
import earth.terrarium.argonauts.api.party.Party;
import earth.terrarium.argonauts.api.party.PartyApi;
import earth.terrarium.argonauts.common.constants.ConstantComponents;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import earth.terrarium.argonauts.common.handlers.base.members.Member;
import earth.terrarium.argonauts.common.handlers.chat.ChatHandler;
import earth.terrarium.argonauts.common.handlers.chat.ChatMessageType;
import earth.terrarium.argonauts.common.utils.ModUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

public class PartyHandler implements PartyApi {

    private static final Map<UUID, Party> PARTIES = new HashMap<>();
    private static final Map<UUID, UUID> PLAYER_PARTIES = new HashMap<>();

    @Override
    public void createParty(ServerPlayer player) throws MemberException {
        if (PLAYER_PARTIES.containsKey(player.getUUID())) {
            throw MemberException.ALREADY_IN_PARTY;
        }
        UUID id = CommonUtils.generate(Predicate.not(PARTIES::containsKey), UUID::randomUUID);
        Party party = new Party(id, player);
        PARTIES.put(id, party);
        PLAYER_PARTIES.put(player.getUUID(), id);
        //ModUtils.sendToAllClientPlayers(new ClientboundSyncPartiesPacket(Set.of(party), Set.of()), player.server);
        player.displayClientMessage(ConstantComponents.PARTY_CREATE, false);
    }

    @Nullable
    @Override
    public Party get(UUID id) {
        return PARTIES.get(id);
    }

    @Nullable
    @Override
    public Party get(Player player) {
        return getPlayerParty(player.getUUID());
    }

    @Nullable
    @Override
    public Party getPlayerParty(UUID player) {
        return get(PLAYER_PARTIES.get(player));
    }

    @Override
    public void join(Party party, ServerPlayer player) throws MemberException {
        if (PLAYER_PARTIES.containsKey(player.getUUID())) {
            throw MemberException.ALREADY_IN_PARTY;
        } else if (party.ignored().has(player.getUUID())) {
            throw MemberException.NOT_ALLOWED_TO_JOIN_PARTY;
        } else if (party.isPublic() || party.members().isInvited(player.getUUID())) {
            for (Member member : party.members()) {
                ServerPlayer serverPlayer = player.server.getPlayerList().getPlayer(member.profile().getId());
                if (serverPlayer == null) continue;
                serverPlayer.displayClientMessage(CommonUtils.serverTranslatable("text.argonauts.member.party_perspective_join", player.getName().getString(), party.members().getLeader().profile().getName()), false);
            }

            party.members().add(player.getGameProfile());
            PLAYER_PARTIES.put(player.getUUID(), party.id());
            //ModUtils.sendToAllClientPlayers(new ClientboundSyncPartiesPacket(Set.of(party), Set.of()), player.server);
            player.displayClientMessage(CommonUtils.serverTranslatable("text.argonauts.member.party_join", party.members().getLeader().profile().getName()), false);
        } else {
            throw MemberException.NOT_ALLOWED_TO_JOIN_PARTY;
        }
    }

    @Override
    public void leave(UUID id, ServerPlayer player) throws MemberException {
        Party party = get(id);
        if (party == null) {
            throw MemberException.PARTY_DOES_NOT_EXIST;
        } else if (PLAYER_PARTIES.get(player.getUUID()) != id) {
            throw MemberException.PLAYER_IS_NOT_IN_PARTY;
        } else if (party.members().isLeader(player.getUUID())) {
            throw MemberException.YOU_CANT_REMOVE_PARTY_LEADER;
        }
        PLAYER_PARTIES.remove(player.getUUID());
        party.members().remove(player.getUUID());
        //ModUtils.sendToAllClientPlayers(new ClientboundSyncPartiesPacket(Set.of(party), Set.of()), player.server);

        player.displayClientMessage(CommonUtils.serverTranslatable("text.argonauts.member.party_leave", party.members().getLeader().profile().getName()), false);

        for (Member member : party.members()) {
            ServerPlayer serverPlayer = player.server.getPlayerList().getPlayer(member.profile().getId());
            if (serverPlayer == null) continue;
            serverPlayer.displayClientMessage(CommonUtils.serverTranslatable("text.argonauts.member.party_perspective_leave", player.getName().getString(), party.members().getLeader().profile().getName()), false);
        }
    }

    @Override
    public void disband(Party party, MinecraftServer server) {
        PARTIES.remove(party.id());
        //ModUtils.sendToAllClientPlayers(new ClientboundSyncPartiesPacket(Set.of(), Set.of(party.id())), server);
        party.members().forEach(member -> {
            if (PLAYER_PARTIES.get(member.profile().getId()) == party.id()) {
                PLAYER_PARTIES.remove(member.profile().getId());
            }
        });
        ChatHandler.remove(party, ChatMessageType.PARTY);
        ServerPlayer player = server.getPlayerList().getPlayer(party.members().getLeader().profile().getId());
        if (player == null) return;
        player.displayClientMessage(CommonUtils.serverTranslatable("text.argonauts.member.party_disband", player.getName().getString()), false);
    }

    @Override
    public Collection<Party> getAll() {
        return PARTIES.values();
    }
}
