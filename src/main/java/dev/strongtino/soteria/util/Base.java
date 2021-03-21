package dev.strongtino.soteria.util;

import com.google.gson.Gson;
import dev.strongtino.soteria.Soteria;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public interface Base {

    Soteria SOTERIA = Soteria.getInstance();
    ExecutorService EXECUTOR = Executors.newFixedThreadPool(3);
    Gson GSON = new Gson();

    // Replacement for config file because I'm too lazy, might change later
    String BOT_TOKEN = "token";
    String BOT_PRESENCE = "for licenses.";

    String MONGO_ADDRESS = "localhost";
    String MONGO_DATABASE = "soteria";
    int MONGO_PORT = 27017;

    String COLLECTION_LICENSES = "licenses";

    default void runAsync(Callback callback) {
        EXECUTOR.execute(callback::run);
    }

    default JDA getJDA() {
        return SOTERIA.getJda();
    }

    default MessageEmbed createEmbed(Color color, String title, String... fields) {
        EmbedBuilder embed = new EmbedBuilder();
        StringBuilder builder = new StringBuilder();

        Arrays.asList(fields).forEach(field -> builder.append(field).append('\n'));

        embed.setColor(color);
        embed.addField(title, builder.toString(), false);
        embed.setFooter("Created by strongtino", getJDA().getSelfUser().getAvatarUrl());

        return embed.build();
    }

    default void warning(String text) {
        System.out.println("[Soteria] [WARNING] " + text);
    }

    default String convertToString(Collection<String> collection, boolean boldValues) {
        StringBuilder builder = new StringBuilder();
        String wrap = boldValues ? "**" : "";

        collection.forEach(element -> builder
                .append(wrap)
                .append(element)
                .append(wrap)
                .append(", "));

        int stringLength = builder.toString().length();

        return builder.substring(0, stringLength - (stringLength > 2 ? 2 : 0));
    }
}
