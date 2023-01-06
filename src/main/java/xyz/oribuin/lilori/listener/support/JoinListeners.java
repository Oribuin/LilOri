package xyz.oribuin.lilori.listener.support;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class JoinListeners extends ListenerAdapter {

    private final String GUILD_ID = "731659405958971413"; // Ori Support Server ID

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        if (!event.getGuild().getId().equals(GUILD_ID))
            return;

        TextChannel channel = event.getGuild().getTextChannelById("733059354328170629"); // #join-logs channel
        if (channel == null)
            return;

        MessageEmbed embed = new EmbedBuilder()
                .setAuthor("Member Joined: " + event.getUser().getAsTag())
                .setDescription("ID: [" + event.getUser().getId() + "] | Mention: " + event.getUser().getAsMention())
                .setColor(Color.decode("#a6b2fc"))
                .build();

        channel.sendMessageEmbeds(embed).queue();
    }

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        if (event.getGuild().getId().equals(GUILD_ID))
            return;

        TextChannel channel = event.getGuild().getTextChannelById("733059354328170629"); // #join-logs channel
        if (channel == null)
            return;

        MessageEmbed embed = new EmbedBuilder()
                .setAuthor("Member Left: " + event.getUser().getAsTag())
                .setDescription("ID: [" + event.getUser().getId() + "]")
                .setColor(Color.decode("#f75454"))
                .build();
    }

}
