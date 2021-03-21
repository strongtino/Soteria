package dev.strongtino.soteria.command;

import dev.strongtino.soteria.service.license.License;
import dev.strongtino.soteria.service.license.product.Product;
import dev.strongtino.soteria.util.command.Command;
import dev.strongtino.soteria.util.command.CommandType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class LicenseCommand extends Command {

    public LicenseCommand() {
        super("license", Collections.singletonList("licence"), CommandType.GUILD);
    }

    @Override
    public void execute(Member member, TextChannel channel, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
            StringBuilder builder = new StringBuilder();
            if (SOTERIA.getLicenseService().getLicenses().isEmpty()) {
                builder.append("No licenses found.");
            } else {
                SOTERIA.getLicenseService().getLicenses().forEach(license -> {
                    builder.append(license.getKey()).append(", ").append(license.getUser()).append(", ").append(license.getProduct().getName()).append("\n");
                });
            }
            channel.sendMessage(builder.toString()).queue();
        }
        if (args.length != 3) return;

        if (args[0].equalsIgnoreCase("create")) {
            String licenseUser = args[1];
            Product product = Product.getByName(args[2]);

            if (product == null) {
                channel.sendMessage(createEmbed(Color.RED, "License Error", "Usage: /license create <user> <product>\n\nAvailable products: "
                        + convertToString(Arrays.stream(Product.values()).map(Product::name).collect(Collectors.toList()), true))).queue();
                return;
            }
            runAsync(() -> {
                License license = SOTERIA.getLicenseService().createLicense(licenseUser, product);

                channel.sendMessage("License: " + license.getKey() + "\nUser: " + license.getUser() + "\nProduct: " + license.getProduct().getName()).queue();
            });
        }
    }
}
