package dev.strongtino.soteria.util;

import dev.strongtino.soteria.Soteria;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by StrongTino on 28.12.2020.
 */

public interface Base {

    // -- Constants --
    Soteria SOTERIA = Soteria.getInstance();

    String BOT_TOKEN = "NzkzMjM1NDY5MjM5ODQ0OTA1.X-pUGQ.IbSah8r7TEfcSDr2SbeLjBQCYkc";
    String BOT_PRESENCE = "for license keys.";
    String GUILD_ID = "777942242399748098";

    String TABLE_LICENSES = "licenses";
    String TABLE_LICENSE_USAGES = "license_usages";

    // -- Methods --
    default void alert(String text) {
        System.out.println("[Critical] " + text);
    }

    default void warning(String text) {
        System.out.println("[Soteria] [WARNING] " + text);
    }

    default boolean equalsIgnoreCase(String input, String... possibilities) {
        return Arrays.stream(possibilities).anyMatch(possibility -> possibility.equalsIgnoreCase(input));
    }

    default boolean notInteger(String input) {
        try {
            Integer.parseInt(input);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }
    default String convertToString(Collection<String> collection, boolean boldValues) {
        StringBuilder builder = new StringBuilder(collection.size());

        collection.forEach(element -> builder
                .append(boldValues ? "**" : "")
                .append(element)
                .append(boldValues ? "**" : "")
                .append(", "));

        int substringIndex = builder.toString().length() > 2 ? 2 : 0;
        return builder.toString().substring(0, builder.toString().length() - substringIndex);
    }

    // -- Simplified Discord API Methods --
    default JDA getJDA() {
        return SOTERIA.getJda();
    }

    default Guild getGuild() {
        return getJDA().getGuildById(GUILD_ID);
    }

    default User getUser(String id) {
        return getMember(id).getUser();
    }

    default Member getMember(String id) {
        return getGuild().getMemberById(id);
    }

    default Member getMember(User user) {
        return getGuild().getMember(user);
    }

    default Role getRole(String role) {
        return getGuild().getRolesByName(role, true).get(0);
    }

    default boolean existsRole(String role) {
        return !getGuild().getRolesByName(role, true).isEmpty();
    }

    default boolean hasRole(Member member, String role) {
        return member.getRoles().contains(getRole(role));
    }

    default void addRoles(Member member, String... roles) {
        Arrays.asList(roles).forEach(role -> member.getGuild().getController().addSingleRoleToMember(member, getRole(role)).queue());
    }

    default void removeRoles(Member member, String... roles) {
        Arrays.asList(roles).forEach(role -> member.getGuild().getController().removeSingleRoleFromMember(member, getRole(role)).queue());
    }

    default boolean hasPermission(User user, Permission permission) {
        return user != null && getGuild().getMember(user).hasPermission(permission);
    }

    default void sendPrivateMessage(User user, MessageEmbed message) {
        user.openPrivateChannel().complete().sendMessage(message).queue();
    }

    default MessageEmbed embed(Color color, String title, String field) {
        EmbedBuilder embed = new EmbedBuilder();

        embed.setColor(color);
        embed.addField(title, field, false);
        embed.setFooter(RandomUtil.getCurrentDate(), getJDA().getSelfUser().getAvatarUrl());

        return embed.build();
    }

    default MessageEmbed embed(Color color, String title, String... fields) {
        EmbedBuilder embed = new EmbedBuilder();
        StringBuilder builder = new StringBuilder();

        Arrays.asList(fields).forEach(field -> builder.append(field).append("\n"));

        embed.setColor(color);
        embed.addField(title, builder.toString(), false);
        embed.setFooter(RandomUtil.getCurrentDate(), getJDA().getSelfUser().getAvatarUrl());

        return embed.build();
    }

    default MessageEmbed embed(Color color, String title, MessageEmbed.Field... fields) {
        EmbedBuilder embed = new EmbedBuilder();
        Arrays.stream(fields).forEach(embed::addField);

        embed.setColor(color);
        embed.setTitle(title);
        embed.setFooter(RandomUtil.getCurrentDate(), getJDA().getSelfUser().getAvatarUrl());

        return embed.build();
    }
}
