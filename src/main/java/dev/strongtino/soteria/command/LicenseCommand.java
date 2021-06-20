package dev.strongtino.soteria.command;

import dev.strongtino.soteria.Soteria;
import dev.strongtino.soteria.license.License;
import dev.strongtino.soteria.util.JDAUtil;
import dev.strongtino.soteria.util.StringUtil;
import dev.strongtino.soteria.util.command.Command;
import dev.strongtino.soteria.util.command.CommandType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LicenseCommand extends Command {

    private final MessageEmbed usage = JDAUtil.createEmbed(Color.RED, "Available commands",
            "/license create <user> <software> [duration]",
            "/license revoke <license> or <user> <software>",
            "/license check <license> or <user> or <software>",
            "/license list [page]");

    public LicenseCommand() {
        super("license", Arrays.asList("licence", "licenses", "licences"), Permission.ADMINISTRATOR, CommandType.GUILD);
    }

    @Override
    public void execute(Member member, TextChannel channel, Message message, String[] args) {
        if (args.length == 0 || args.length > 3) {
            channel.sendMessage(usage).queue();
            return;
        }
        switch (args[0].toLowerCase()) {
            case "create":
                Soteria.INSTANCE.getLicenseService().createLicense(args[1], args[2]);
                channel.sendMessage("created a license").queue();
                break;
            case "revoke":
                break;
            case "list":
                int page = args.length == 1 || !StringUtil.isInteger(args[1]) ? 1 : Integer.parseInt(args[1]);
                int elementsPerPage = 5;

                List<List<License>> pages = StringUtil.sliceList(new ArrayList<>(Soteria.INSTANCE.getLicenseService().getLicenses()), elementsPerPage);

                if (page < 1 || page > pages.size()) {
                    channel.sendMessage(JDAUtil.createEmbed(Color.RED, "Invalid page", pages.isEmpty() ? "There are no licenses created yet." : "That page is not found.")).queue();
                    return;
                }
                List<License> elements = pages.get(page - 1);

                EmbedBuilder embed = JDAUtil.embedBuilder(Color.ORANGE, "Licenses (Page " + page + '/' + pages.size() + ')');

                for (int i = 0; i < elements.size(); i++) {
                    License license = elements.get(i);

                    embed.addField((i + 1 + ((page - 1) * elementsPerPage)) + ". license",
                            "Key: **" + license.getKey() + "**"
                                    + "\nUser: **" + license.getUser() + "**"
                                    + "\nSoftware: **" + license.getSoftware() + "**",
                            false
                    );
                }
                channel.sendMessage(embed.build()).queue();
                break;
            default:
                channel.sendMessage(usage).queue();
        }
    }
}
