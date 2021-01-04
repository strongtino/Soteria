package dev.strongtino.soteria.util.command;

import dev.strongtino.soteria.util.Base;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by StrongTino on 28.12.2020.
 */

public abstract class Command extends ListenerAdapter implements Base {

    public final char PREFIX = '/';

    private final String command;
    private final List<String> aliases;
    private final Permission permission;
    private final CommandType commandType;

    // Main constructor that will initialize every variable
    public Command(String command, List<String> aliases, Permission permission, CommandType commandType) {
        this.command = command;
        this.aliases = aliases;
        this.permission = permission;
        this.commandType = commandType;
    }

    /***
     * Constructors used if we don't need all parameters.
     * Unused variables are set as null or default value and will not make an impact on the command.
     *
     * Example: If you use the no args constructor you can then have multiple commands in single class by checking the first argument
     */
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
            prepare(event.getAuthor(), event.getMessage(), null);
        }
    }

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (commandType == CommandType.GUILD) {
            prepare(event.getAuthor(), event.getMessage(), event.getChannel());
        }
    }

    private void prepare(User user, Message message, TextChannel channel) {
        // We usually don't want bots being able to execute commands.
        if (user.isBot()) return;

        // Getting the message content and splitting it into the array
        String[] arguments = message.getContentRaw().split(" ");

        /*
         *  Making a new array without the first element (command)
         *
         *  Example Command: /ban <player> <reason>
         *  args[0] -> <player>
         *  args[1] -> <reason>
         *  etc...
         */
        String[] args = Arrays.copyOfRange(arguments, command == null ? 0 : 1, arguments.length);

        // If permission is set and user doesn't have that permission, command won't be executed.
        if (permission != null && !hasPermission(user, permission)) return;

        // If all parameters are valid -> execute the command
        if (command == null || arguments[0].equalsIgnoreCase(PREFIX + command) || (!aliases.isEmpty() && aliases.stream().anyMatch(alias -> arguments[0].equalsIgnoreCase(PREFIX + alias)))) {
            execute(user, channel, args);
        }
    }

    // Basic abstract method, you can add more parameters or remove some if you want (personal preference)
    public abstract void execute(User user, TextChannel channel, String[] args);
}
