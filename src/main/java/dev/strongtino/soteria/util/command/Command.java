package dev.strongtino.soteria.util.command;

import dev.strongtino.soteria.util.Base;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public abstract class Command extends ListenerAdapter implements Base {

    public final static char PREFIX = '!';

    private final String command;
    private final List<String> aliases;
    private final Permission permission;
    private final CommandType commandType;

    @Setter
    private boolean async;

    public Command(CommandType commandType) {
        this(null, new ArrayList<>(), null, commandType);
    }

    public Command(Permission permission, CommandType commandType) {
        this(null, new ArrayList<>(), permission, commandType);
    }

    public Command(String command, CommandType commandType) {
        this(command, new ArrayList<>(), null, commandType);
    }

    public Command(String command, List<String> aliases, CommandType commandType) {
        this(command, aliases, null, commandType);
    }

    public Command(String command, Permission permission, CommandType commandType) {
        this(command, new ArrayList<>(), permission, commandType);
    }

    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        if (commandType == CommandType.PRIVATE) {
            prepare(event.getMessage(), null);
        }
    }

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (commandType == CommandType.GUILD) {
            prepare(event.getMessage(), null);
        }
    }

    private void prepare(Message message, TextChannel channel) {
        Member member = message.getMember();

        if (member == null || member.getUser().isBot()) return;

        String[] originalArray = message.getContentRaw().split(" ");
        String[] newArray = Arrays.copyOfRange(originalArray, command == null ? 0 : 1, originalArray.length);

        if (permission != null && !member.hasPermission(permission)) return;

        if (command == null || originalArray[0].equalsIgnoreCase(PREFIX + command) || (!aliases.isEmpty() && aliases.stream().anyMatch(alias -> originalArray[0].equalsIgnoreCase(PREFIX + alias)))) {
            if (async) runAsync(() -> execute(member, channel, newArray));
            else execute(member, channel, newArray);
        }
    }

    public abstract void execute(Member member, TextChannel channel, String[] args);
}
