package xyz.oribuin.lilori.command;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.oribuin.lilori.LilOri;
import xyz.oribuin.lilori.handler.Category;
import xyz.oribuin.lilori.handler.OptionBuilder;
import xyz.oribuin.lilori.handler.type.SlashCommand;
import xyz.oribuin.lilori.manager.DataManager;
import xyz.oribuin.lilori.manager.TicketManager;
import xyz.oribuin.lilori.util.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ArchiveCommand extends SlashCommand {

    private final LilOri bot = LilOri.getInstance();

    @Override
    public void execute(SlashCommandInteractionEvent event) {

        if (event.getGuild() == null)
            return;

        if (!event.getGuild().getId().equals(Constants.SUPPORT_SERVER.getValue())) {
            event.reply("❌ This command can only be used in the support server.").setEphemeral(true).queue();
            return;
        }

        if (!(event.getChannel() instanceof TextChannel textChannel)) {
            event.reply("❌ This command can only be used in a text channel.").setEphemeral(true).queue();
            return;
        }

        if (textChannel.getParentCategoryIdLong() != Constants.TICKETS_CATEGORY.getLong()) {
            event.reply("❌ This command can only be used in a ticket channel.").setEphemeral(true).queue();
            return;
        }

        File archive = LilOri.getInstance().getManager(TicketManager.class)
                .createFileArchive(event.getChannel().asTextChannel());

        this.bot.getManager(DataManager.class).createArchiveLog(archive.getName(), integer -> {
            this.bot.getManager(TicketManager.class).getTicketFiles().put(integer, archive);

            event.reply("We have archived your ticket and will be closing it shortly. Press the button below within 30 seconds to get a copy of the archive.")
                    .addActionRow(Button.success("request-archive:" + integer, "Request Archive").withEmoji(Emoji.fromUnicode("📦")))
                    .queue(hook -> textChannel.delete().queueAfter(30, TimeUnit.SECONDS));
        });
    }

    @NotNull
    @Override
    public String getName() {
        return "archive";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Archive a channel into a html file.";
    }

    @Nullable
    @Override
    public String getUsage() {
        return "/archive";
    }

    @NotNull
    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @NotNull
    @Override
    public Category getCategory() {
        return Category.OWNER;
    }

    @Override
    public int getCooldown() {
        return 0;
    }

    @NotNull
    @Override
    public List<Long> getRequiredRoles() {
        return new ArrayList<>();
    }

    @Nullable
    @Override
    public Long getRequiredGuild() {
        return 731659405958971413L;
    }

    @Override
    public boolean isNSFW() {
        return false;
    }

    @Override
    public boolean isOwnerOnly() {
        return true;
    }

    @NotNull
    @Override
    public List<OptionBuilder> getOptionBuilders() {
        return new ArrayList<>();
    }


}
