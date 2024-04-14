package earth.terrarium.argonauts.client.compat.rei;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.Lifecycle;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.client.screens.members.MembersScreen;
import me.shedaniel.rei.api.client.favorites.FavoriteEntryType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("UnstableApiUsage")
public class PartyMembersFavoriteEntry extends BaseFavoriteEntry {

    public static final ResourceLocation ID = new ResourceLocation(Argonauts.MOD_ID, "party_members");
    private static final ResourceLocation TEXTURE = new ResourceLocation(Argonauts.MOD_ID, "textures/gui/icons/chat.png");

    public PartyMembersFavoriteEntry() {
        super(ID, TEXTURE, MembersScreen::openParty);
    }

    public enum Type implements FavoriteEntryType<PartyMembersFavoriteEntry> {
        INSTANCE;

        @Override
        public DataResult<PartyMembersFavoriteEntry> read(CompoundTag object) {
            return DataResult.success(new PartyMembersFavoriteEntry(), Lifecycle.stable());
        }

        @Override
        public DataResult<PartyMembersFavoriteEntry> fromArgs(Object... args) {
            return DataResult.success(new PartyMembersFavoriteEntry(), Lifecycle.stable());
        }

        @Override
        public CompoundTag save(PartyMembersFavoriteEntry entry, CompoundTag tag) {
            return tag;
        }
    }
}