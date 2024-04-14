package earth.terrarium.argonauts.api.teams.settings.types;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import earth.terrarium.argonauts.api.teams.settings.Setting;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ColorArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.Locale;

public record ChatFormattingSetting(String id, ChatFormatting value) implements Setting<ChatFormatting> {

    @Override
    public ArgumentBuilder<CommandSourceStack, ?> createArgument(String argument) {
        return Commands.argument(argument, ColorArgument.color());
    }

    @Override
    public Setting<ChatFormatting> getFromArgument(String argument, CommandContext<CommandSourceStack> context) {
        return new ChatFormattingSetting(id, ColorArgument.getColor(context, argument));
    }

    @Override
    public void serialize(CompoundTag tag) {
        tag.putString(id, value.getName().toLowerCase(Locale.ROOT));
    }

    @Override
    public Setting<ChatFormatting> deserialize(CompoundTag tag) {
        return new ChatFormattingSetting(id, ChatFormatting.getByName(tag.getString(id)));
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeEnum(value);
    }

    @Override
    public Setting<ChatFormatting> decode(FriendlyByteBuf buf) {
        return new ChatFormattingSetting(id, buf.readEnum(ChatFormatting.class));
    }

    @Override
    public String toString() {
        return value.getName();
    }
}
