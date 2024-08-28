package earth.terrarium.argonauts.common.handlers.guild.settings;

import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import com.teamresourceful.resourcefullib.common.bytecodecs.ExtraByteCodecs;
import net.minecraft.ChatFormatting;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class GuildSettings {

    @Nullable
    private GlobalPos hq;
    private Component displayName = CommonComponents.EMPTY;
    private Component motd = CommonComponents.EMPTY;
    private ChatFormatting color = ChatFormatting.AQUA;
    private boolean allowFakePlayers;
    private boolean syncAdvancements;

    public GuildSettings() {}

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public GuildSettings(Component displayName, Component motd, ChatFormatting color, boolean allowFakePlayers, boolean syncAdvancements) {
        this.displayName = displayName;
        this.motd = motd;
        this.color = color;
        this.allowFakePlayers = allowFakePlayers;
        this.syncAdvancements = syncAdvancements;
    }

    public void setDisplayName(Component displayName) {
        this.displayName = displayName;
    }

    public Component displayName() {
        return displayName;
    }

    public void setMotd(Component motd) {
        this.motd = motd;
    }

    public Component motd() {
        return motd;
    }

    public void setColor(ChatFormatting color) {
        this.color = color;
    }

    public ChatFormatting color() {
        return color;
    }

    public void setAllowFakePlayers(boolean allowFakePlayers) {
        this.allowFakePlayers = allowFakePlayers;
    }

    public boolean allowFakePlayers() {
        return allowFakePlayers;
    }

    public void setSyncAdvancements(boolean syncAdvancements) { this.syncAdvancements = syncAdvancements; }

    public boolean syncAdvancements() { return syncAdvancements; }

    public static final ByteCodec<GuildSettings> BYTE_CODEC = ObjectByteCodec.create(
        ExtraByteCodecs.COMPONENT.fieldOf(GuildSettings::displayName),
        ExtraByteCodecs.COMPONENT.fieldOf(GuildSettings::motd),
        ByteCodec.ofEnum(ChatFormatting.class).fieldOf(GuildSettings::color),
        ByteCodec.BOOLEAN.fieldOf(GuildSettings::allowFakePlayers),
        ByteCodec.BOOLEAN.fieldOf(GuildSettings::syncAdvancements),
        GuildSettings::new
    );
}
