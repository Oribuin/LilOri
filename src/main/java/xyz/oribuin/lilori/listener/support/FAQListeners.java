package xyz.oribuin.lilori.listener.support;

import jdk.jfr.SettingControl;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import xyz.oribuin.lilori.LilOri;
import xyz.oribuin.lilori.manager.DataManager;
import xyz.oribuin.lilori.util.Constants;

import java.awt.*;
import java.util.Map;

public class FAQListeners extends ListenerAdapter {

    private final DataManager dataManager;

    public FAQListeners(LilOri bot) {
        this.dataManager = bot.getManager(DataManager.class);
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.isWebhookMessage())
            return;

        if (!event.getGuild().getId().equals(Constants.SUPPORT_SERVER.getValue()))
            return;

        final String message = event.getMessage().getContentRaw();
        if (!message.startsWith("?"))
            return;

        String key = message.substring(1).toLowerCase();

        String answer = this.dataManager.getFaqMap().get(key);
        if (answer == null)
            return;

        event.getMessage().replyEmbeds(
                new EmbedBuilder()
                        .setAuthor("Found FAQ for: " + key)
                        .setDescription(answer)
                        .setFooter("Requested by: " + event.getAuthor().getAsTag())
                        .setColor(Color.decode(Constants.DEFAULT_COLOR.getValue()))
                        .build()
        ).queue();
    }

}
