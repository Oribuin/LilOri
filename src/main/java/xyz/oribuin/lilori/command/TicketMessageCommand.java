package xyz.oribuin.lilori.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.oribuin.lilori.handler.Category;
import xyz.oribuin.lilori.handler.OptionBuilder;
import xyz.oribuin.lilori.handler.type.SlashCommand;
import xyz.oribuin.lilori.util.Constants;

import java.awt.*;
import java.util.List;

public class TicketMessageCommand extends SlashCommand {

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        MessageEmbed builder = new EmbedBuilder()
                .setAuthor("\uD83C\uDFAB Create A Ticket")
                .setColor(Color.decode(Constants.DEFAULT_COLOR.getString()))
                .setDescription("Create a private support channel to have direct communication with our support team.\n" +
                        "\n" +
                        "Please select the type of ticket you would like to create.\n" +
                        "\uD83D\uDC1B **Issue** - For plugin bug reports or errors.\n" +
                        "❓ **Question** - For plugin questions.\n" +
                        "\uD83D\uDCD8 **Other** - General Tickets.\n" +
                        "\n" +
                        "If you are creating a ticket for a plugin, please include the plugin name, version and the subject.\n" +
                        "__Latest is not a version, use `/version <plugin>` to get the version.__"
                )
                .build();

        event.getChannel().sendMessageEmbeds(builder)
                .addActionRow(
                        Button.primary("create-ticket:issue", "Issue"),
                        Button.primary("create-ticket:question", "Question"),
                        Button.secondary("create-ticket:other", "Other")
                ).queue();


    }

    @NotNull
    @Override
    public String getName() {
        return "ticketmessage";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Send the message to create a ticket.";
    }

    @Nullable
    @Override
    public String getUsage() {
        return "/ticketmessage";
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
        return List.of();
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
        return List.of();
    }

}
