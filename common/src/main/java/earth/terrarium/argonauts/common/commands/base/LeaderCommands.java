package earth.terrarium.argonauts.common.commands.base;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.teamresourceful.resourcefullib.common.utils.CommonUtils;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import earth.terrarium.argonauts.common.handlers.base.members.Group;
import earth.terrarium.argonauts.common.handlers.base.members.Member;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public final class LeaderCommands {
    public static <M extends Member, T extends Group<M, ?>> ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> disband(CommandHelper.GetGroupAction<M, T> getGroupAction, RemoveAction<M, T> removeAction, MemberException youAreNotTheLeaderOfGroup) {
        return Commands.literal("disband")
            .executes(context -> {
                ServerPlayer player = context.getSource().getPlayerOrException();
                var group = getGroupAction.getGroup(player, false);
                CommandHelper.runAction(() -> {
                    if (group.members().isLeader(player.getUUID())) {
                        removeAction.remove(group, player.server);
                    } else {
                        throw youAreNotTheLeaderOfGroup;
                    }
                    group.members().forEach(p -> {
                        ServerPlayer groupMember = player.server.getPlayerList().getPlayer(p.profile().getId());
                        if (groupMember != null) {
                            groupMember.displayClientMessage(CommonUtils.serverTranslatable("text.argonauts.member.disband", group.displayName()), false);
                        }
                    });
                });
                return 1;
            });
    }

    public static <M extends Member, T extends Group<M, ?>> ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> transfer(CommandHelper.GetGroupAction<M, T> getGroupAction, MemberException youAreNotTheLeaderOfGroup) {
        return Commands.literal("transfer").then(Commands.argument("player", EntityArgument.player())
            .executes(context -> {
                ServerPlayer player = context.getSource().getPlayerOrException();
                ServerPlayer target = EntityArgument.getPlayer(context, "player");
                var group = getGroupAction.getGroup(player, false);
                CommandHelper.runAction(() -> {
                    if (group.members().isLeader(player.getUUID())) {
                        CommandHelper.runAction(() -> group.members().setLeader(target.getGameProfile()));
                        player.displayClientMessage(CommonUtils.serverTranslatable("text.argonauts.leader.transfer", group.displayName(), target.getGameProfile().getName()), false);
                        target.displayClientMessage(CommonUtils.serverTranslatable("text.argonauts.leader.transfer_receive", target.getGameProfile().getName(), group.displayName()), false);
                    } else {
                        throw youAreNotTheLeaderOfGroup;
                    }
                });
                return 1;
            }));
    }

    @FunctionalInterface
    public interface RemoveAction<M extends Member, T extends Group<M, ?>> {
        void remove(T group, MinecraftServer server) throws MemberException;
    }
}
