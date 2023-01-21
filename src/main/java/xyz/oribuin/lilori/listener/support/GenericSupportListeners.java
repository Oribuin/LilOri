package xyz.oribuin.lilori.listener.support;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import xyz.oribuin.lilori.util.Constants;

import java.awt.*;

public class GenericSupportListeners extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        if (!event.getGuild().getId().equals(Constants.SUPPORT_SERVER.getValue()))
            return;

        TextChannel channel = event.getGuild().getTextChannelById("733059354328170629"); // #join-logs channel
        if (channel == null)
            return;

        // give the member role
        Role role = event.getGuild().getRoleById("731661458680578069");
        if (role != null)
            event.getGuild().addRoleToMember(event.getMember(), role).queue();

        // Send embed to the channel
        MessageEmbed embed = new EmbedBuilder()
                .setAuthor("Member Joined: " + event.getUser().getAsTag())
                .setDescription("ID: [" + event.getUser().getId() + "] | Mention: " + event.getUser().getAsMention())
                .setColor(Color.decode("#a6b2fc"))
                .build();

        channel.sendMessageEmbeds(embed).queue();
    }

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        if (!event.getGuild().getId().equals(Constants.SUPPORT_SERVER.getValue()))
            return;

        TextChannel channel = event.getGuild().getTextChannelById("733059354328170629"); // #join-logs channel
        if (channel == null)
            return;

        MessageEmbed embed = new EmbedBuilder()
                .setAuthor("Member Left: " + event.getUser().getAsTag())
                .setDescription("ID: [" + event.getUser().getId() + "]")
                .setColor(Color.decode("#f75454"))
                .build();

        channel.sendMessageEmbeds(embed).queue();
    }

}
