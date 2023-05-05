package earth.terrarium.argonauts.common.commands.party;

import earth.terrarium.argonauts.common.handlers.party.Party;
import earth.terrarium.argonauts.common.handlers.party.PartyException;
import earth.terrarium.argonauts.common.handlers.party.PartyHandler;
import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.world.entity.player.Player;

public final class PartyCommandHelper {

    public static Party getPartyOrThrow(Player player, boolean otherPlayer) throws CommandRuntimeException {
        Party party = PartyHandler.get(player);
        if (party == null) {
            throw new CommandRuntimeException(otherPlayer ? PartyException.PLAYER_IS_NOT_IN_PARTY.message() : PartyException.YOU_ARE_NOT_IN_PARTY.message());
        }
        return party;
    }
}
