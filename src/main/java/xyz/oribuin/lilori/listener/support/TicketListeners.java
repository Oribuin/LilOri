package xyz.oribuin.lilori.listener.support;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;
import net.dv8tion.jda.api.utils.FileUpload;
import org.jetbrains.annotations.NotNull;
import xyz.oribuin.lilori.LilOri;
import xyz.oribuin.lilori.manager.TicketManager;
import xyz.oribuin.lilori.ticket.Ticket;
import xyz.oribuin.lilori.ticket.TicketType;
import xyz.oribuin.lilori.util.Constants;

import java.io.File;

public class TicketListeners extends ListenerAdapter {

    private final TicketManager manager;

    public TicketListeners() {
        this.manager = LilOri.getInstance().getManager(TicketManager.class);
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if (event.getGuild() == null || !event.getGuild().getId().equals(Constants.SUPPORT_SERVER.getValue()))
            return;

        final String id = event.getComponentId();

        // Create a new ticket
        if (id.startsWith("create-ticket:")) {
            TicketType type = TicketType.valueOf(id.split(":")[1].toUpperCase());

            Modal.Builder modal = Modal.create("ticket-modal:" + type.name(), "Create a new ticket")
                    .addActionRow(TextInput.create("ticket-subject", "Ticket Subject", TextInputStyle.PARAGRAPH).setRequired(true).build());

            if (type != TicketType.OTHER) {
                modal.addActionRow(TextInput.create("ticket-plugin", "Plugin Name", TextInputStyle.SHORT).build());
                modal.addActionRow(TextInput.create("ticket-version", "Plugin Version", TextInputStyle.SHORT).build());
            }

            event.replyModal(modal.build()).queue();
            return;
        }

        // Delete a ticket
        if (id.equalsIgnoreCase("close-ticket")) {
            this.manager.closeTicket(event);
            return;
        }

        // Retrieve the archive
        if (id.startsWith("request-archive:")) {
            File file = this.manager.getArchiveFile(Integer.parseInt(id.split(":")[1]));
            if (file == null) {
                event.reply("There was an error creating the archive.").setEphemeral(true).queue();
                return;
            }

            event.getMessage().delete().queue();
            event.reply("\uD83D\uDCDC\n Here is the archive of this channel.")
                    .addFiles(FileUpload.fromData(file))
                    .queue();
        }
    }

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        if (!event.getGuild().getId().equals(Constants.SUPPORT_SERVER.getValue()))
            return;

        if (!event.getInteraction().getModalId().startsWith("ticket-modal:"))
            return;

        TicketType type = TicketType.valueOf(event.getInteraction().getModalId().split(":")[1].toUpperCase());

        ModalMapping subject = event.getInteraction().getValue("ticket-subject");
        if (subject == null) {
            event.reply("You must provide a subject for your ticket.").setEphemeral(true).queue();
            return;
        }


        Ticket ticket = new Ticket(event.getMember(), type, subject.getAsString());

        // get the plugin name
        ModalMapping plugin = event.getInteraction().getValue("ticket-plugin");
        if (plugin != null) {
            ticket.setPlugin(plugin.getAsString());
        }

        // get the plugin version
        ModalMapping version = event.getInteraction().getValue("ticket-version");
        if (version != null) {
            ticket.setVersion(version.getAsString());
        }

        TextChannel channel = this.manager.createTicket(ticket);
        event.deferReply().setContent("\uD83C\uDFAB Your ticket has been created. You can view it here: " + channel.getAsMention()).setEphemeral(true).queue();
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        // If the message was sent in a ticket channel
        if (!event.getGuild().getId().equals(Constants.SUPPORT_SERVER.getValue()))
            return;

        if (!(event.getChannel() instanceof TextChannel textChannel))
            return;

        if (textChannel.getParentCategoryIdLong() != Constants.TICKETS_CATEGORY.getLong())
            return;

        if (event.getAuthor().isBot())
            return;

        event.getMessage().getMentions().getMembers().forEach(member -> {
            if (textChannel.canTalk(member))
                return;

            textChannel.upsertPermissionOverride(member).setAllowed(Permission.VIEW_CHANNEL).queue();
        });
    }

}
