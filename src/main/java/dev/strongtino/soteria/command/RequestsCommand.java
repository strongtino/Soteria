package dev.strongtino.soteria.command;

import dev.strongtino.soteria.Soteria;
import dev.strongtino.soteria.license.License;
import dev.strongtino.soteria.license.request.Request;
import dev.strongtino.soteria.util.JDAUtil;
import dev.strongtino.soteria.util.StringUtil;
import dev.strongtino.soteria.util.command.Command;
import dev.strongtino.soteria.util.command.CommandType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.Color;
import java.util.List;
import java.util.stream.Collectors;

public class RequestsCommand extends Command {

    public RequestsCommand() {
        super("requests", Permission.ADMINISTRATOR, CommandType.GUILD);
    }

    @Override
    public void execute(Member member, TextChannel channel, Message message, String[] args) {
        if (args.length == 0 || args.length > 2) {
            sendUsage(channel);
            return;
        }
        EmbedBuilder embed = JDAUtil.embedBuilder(Color.ORANGE, "Requests Lookup");

        if (args.length == 1) {
            License license = Soteria.INSTANCE.getLicenseService().getLicenseByKey(args[0]);

            if (license != null) {
                return;
            }
            List<Request> requests = Soteria.INSTANCE.getRequestService().getRequestsByAddress(args[0]);

            if (requests.isEmpty()) {
                channel.sendMessage(JDAUtil.createEmbed(Color.RED, "Requests Error", "There are no recorded GET requests on the received IP address.")).queue();
                return;
            }
            embed.addField("IP address", args[0], false);
            embed.addField("Total requests", String.valueOf(requests.size()), true);
            embed.addField("Recent requests", String.valueOf(requests.stream().filter(Request::isRecent).count()), true);

            embed.addField("Requests per software", Soteria.INSTANCE.getSoftwareService().getSoftware().stream().map(software -> {
                List<Request> softwareRequests = Soteria.INSTANCE.getRequestService().getRequestsBySoftware(software);

                return '`' + software.getName() + "` - " + StringUtil.formatInteger(softwareRequests.size()) + " total, " + softwareRequests.stream().filter(Request::isRecent).count() + " recent\n";
            }).collect(Collectors.joining()), false);
            channel.sendMessage(embed.build()).queue();
            return;
        }
    }

    private void sendUsage(TextChannel channel) {
        channel.sendMessage(JDAUtil.createEmbed(Color.RED, "Invalid usage", "Usage: /requests <address> or <key> or <user> <software>")).queue();
    }
}
