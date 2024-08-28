package earth.terrarium.argonauts.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import earth.terrarium.argonauts.common.commands.guild.*;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class ModCommands {

    @SuppressWarnings("unused")
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext ctx, Commands.CommandSelection environment) {
        GuildCommand.register(dispatcher);
        GuildCommands.register(dispatcher);
        GuildLeaderCommands.register(dispatcher);
        GuildManageCommands.register(dispatcher);
        GuildHqCommands.register(dispatcher);
        GuildMemberCommands.register(dispatcher);
        GuildSettingsCommands.register(dispatcher);
        GuildChatCommands.register(dispatcher);
        GuildFakePlayerCommands.register(dispatcher);
        GuildsCommand.register(dispatcher);
        GuildAdminCommands.register(dispatcher);
    }
}
