package xyz.oribuin.lilori.manager;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.entities.emoji.EmojiUnion;
import net.dv8tion.jda.api.entities.sticker.Sticker;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.apache.commons.lang3.StringUtils;
import xyz.oribuin.lilori.LilOri;
import xyz.oribuin.lilori.ticket.Ticket;
import xyz.oribuin.lilori.util.Constants;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class TicketManager extends Manager {

    private final Map<Integer, File> ticketFiles = new HashMap<>();

    public TicketManager(LilOri bot) {
        super(bot);
    }

    @Override
    public void enable() {
        this.bot.getManager(DataManager.class).getArchiveMap()
                .forEach((integer, s) -> this.ticketFiles.put(integer, new File("/archives/" + s)));
    }

    public TextChannel createTicket(Ticket ticket) {
        Guild guild = this.bot.getJDAInstance().getGuildById(Constants.SUPPORT_SERVER.getString());
        if (guild == null)
            return null;

        Role supportRole = guild.getRoleById(Constants.SUPPORT_ROLE.getString());
        if (supportRole == null)
            return null;

        TextChannel channel = guild.createTextChannel(ticket.getOwner().getUser().getName() + "-ticket")
                .setTopic("\uD83C\uDFAB Welcome to your ticket channel, Our staff will be here shortly.")
                .setParent(guild.getCategoryById("733086484470694018"))
                .addRolePermissionOverride(supportRole.getIdLong(), List.of(Permission.VIEW_CHANNEL), List.of())
                .addMemberPermissionOverride(ticket.getOwner().getIdLong(), List.of(Permission.VIEW_CHANNEL), List.of())
                .addRolePermissionOverride(Constants.MEMBER_ROLE.getLong(), List.of(), List.of(Permission.VIEW_CHANNEL))
                .complete();

        final StringBuilder builder = new StringBuilder("Please wait while we get a staff member to help you.\n\n");
        builder.append("**Ticket Information**\n");
        builder.append("| **Owner**: ").append(ticket.getOwner().getAsMention()).append("\n");
        builder.append("| **Subject**: ").append(ticket.getSubject()).append("\n");
        builder.append("| **Created**: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH))).append("\n");

        if (ticket.getPlugin() != null)
            builder.append("| **Plugin**: ").append(ticket.getPlugin()).append("\n");

        if (ticket.getVersion() != null)
            builder.append("| **Version**: ").append(ticket.getVersion()).append("\n");

        builder.append("\n");
        builder.append("If you would like to close this ticket, please press the button below.");


        Message message = channel.sendMessage(ticket.getOwner().getAsMention())
                .addEmbeds(new EmbedBuilder()
                        .setAuthor("❓ Ticket Type: " + StringUtils.capitalize(ticket.getType().name().toLowerCase()))
                        .setDescription(builder.toString())
                        .setColor(Color.decode(Constants.DEFAULT_COLOR.getString()))
                        .build())
                .addActionRow(Button.danger("close-ticket", "Close Ticket").withEmoji(Emoji.fromUnicode("\uD83C\uDFAB")))
                .complete();

        channel.pinMessageById(message.getId()).queue();

        // TODO: Reimplement this when the ticket system is finished to prevent spam pinging RansomNGaming
//        channel.sendMessage(supportRole.getAsMention()).queue();
        return channel;
    }

    /**
     * Close a ticket channel with the archive option
     *
     * @param event The event that triggered the close
     */
    public void closeTicket(ButtonInteractionEvent event) {
        if (!(event.getChannel() instanceof TextChannel textChannel))
            return;

        Guild guild = this.bot.getJDAInstance().getGuildById(Constants.SUPPORT_SERVER.getString());
        if (guild == null)
            return;

        File archive = this.createFileArchive(textChannel);
        if (archive != null) {

            this.bot.getManager(DataManager.class).createArchiveLog(archive.getName(), integer -> {

                this.ticketFiles.put(integer, archive);
                event.reply("We have archived your ticket and will be closing it shortly. Press the button below within 30 seconds to get a copy of the archive.")
                        .addActionRow(Button.success("request-archive:" + integer, "Request Archive").withEmoji(Emoji.fromUnicode("📦")))
                        .queue(hook -> textChannel.delete().queueAfter(30, TimeUnit.SECONDS));
            });


            return;
        }

        textChannel.delete().queue();
    }

    public File getArchiveFile(int id) {
        return this.ticketFiles.get(id);
    }

    /**
     * Recreate the ticket channel as a html file
     *
     * @param channel The channel to recreate
     * @return The new text file
     */
    public File createFileArchive(TextChannel channel) {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MMM/yyyy HH:mm:ss"));

        System.out.println(" * Creating archive for " + channel.getName() + "(" + channel.getId() + ") + " + date);
        MessageHistory action = channel.getHistoryBefore(channel.getLatestMessageId(), 100).complete();

        // Start to build the html file
        StringBuilder builder = new StringBuilder("<!DOCTYPE html>")
                .append("<html>")
                .append("<head>")
                .append("<title>Archive for ").append(channel.getName()).append("</title>")
                .append("<meta charset=\"UTF-8\">")
                .append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">")
                .append("<style>body {background-color: #36393f; color: #dcddde; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; font-size: 13px;}</style>")
                .append("</head>");

        // Add styling to the html file
        builder.append("<body>");

        // Add a header to the html file
        builder.append("<div style=\"margin-bottom: 40px; background-color: #2f3136; padding: 10px; text-align: center;\">")
                .append("Recreation of the channel <b>").append(channel.getName()).append("</b> created on <b>")
                .append(channel.getTimeCreated().format(DateTimeFormatter.ofPattern("dd/MMM/yyyy HH:mm:ss")))
                .append("</b>")
                .append("</div>");

        // Make sure the tickets are from oldest to newest
        List<Message> messages = new ArrayList<>(action.getRetrievedHistory());
        messages.add(channel.retrieveMessageById(channel.getLatestMessageId()).complete());
        messages.sort(Comparator.comparing(ISnowflake::getTimeCreated));

        User lastUser = null;
        for (Message message : messages) {

            if (lastUser == null || !lastUser.equals(message.getAuthor())) {
                builder.append("<div style=\"margin-top: 20px; display: flex; align-items: center; margin-bottom: 5px; margin-left: 20px; \">");

                // add the avatar
                builder.append("<img src=\"").append(message.getAuthor().getEffectiveAvatarUrl()).append("\" style=\"width: 48px; height: 48px; border-radius: 50%; margin-right: 10px;\">");

                // add the username and tag with the date and time the message was sent
                builder.append("<div style=\"display: flex; flex-direction: column;\">");
                builder.append("<span style=\"font-weight: bold;\">").append(message.getAuthor().getAsTag()).append("</span>")
                        .append("<span style=\"font-size: 12px; color: #b9bbbe;\">").append(message.getTimeCreated().format(DateTimeFormatter.ofPattern("dd/MMM/yyyy HH:mm:ss"))).append("</span>");
                builder.append("</div></div>");
            }

            String messageContent = message.getContentRaw();

            // TODO: Add discord emojis
            for (User user : message.getMentions().getUsers()) {
                messageContent = messageContent.replace(user.getAsMention(), "<span style=\"color: #c1c5f2; background-color: #3c406f\"> @" + user.getName() + "</span>");
            }

            // replace any mentioned channels with their name
            for (TextChannel textChannel : message.getMentions().getChannels(TextChannel.class)) {
                messageContent = messageContent.replace(textChannel.getAsMention(), "<span style=\"color: #c1c5f2; background-color: #3c406f\"> #" + textChannel.getName() + "</span>");
            }

            // replace any mentioned roles with their name
            for (Role role : message.getMentions().getRoles()) {
                Color defaultColor = role.getColor() == null ? Color.decode("#c1c5f2") : role.getColor();
                int colorRaw = defaultColor.getRGB();
                messageContent = messageContent.replace(role.getAsMention(), "<span style=\"color: " + String.format("#%06X", (0xFFFFFF & colorRaw)) + "; background-color: " + String.format("#%06X", (0xFFFFFF & colorRaw)) + "1a\"> @" + role.getName() + "</span>");
            }

            builder.append("<div style=\"margin-left: 30px; margin-bottom: 3px;\">").append(this.markdownFormat(messageContent)).append("</div>");

            // Add stickers
            for (Sticker sticker : message.getStickers()) {
                builder.append("<img src=\"").append(sticker.getIconUrl()).append("\"style=\"margin-left: 30px; width: 128px; height: 128px; max-height: 128px; max-width: 128px;\" title=\"").append(sticker.getName()).append("\">");
            }

            // Add reactions
            if (!message.getReactions().isEmpty()) {
                builder.append("<div style=\"margin-top: 5px; margin-left: 30px;\">");
                for (MessageReaction reaction : message.getReactions()) {
                    EmojiUnion emoji = reaction.getEmoji();

                    builder.append("<div style=\"display: inline-block; width: 42px; height: 32px; background-color: #3b405a; border-radius: 10%; border: 1px solid #5865f2; text-align: center; vertical-align: middle; padding: 3px; margin: 3px;\">"); //  margin-right: 5px;

                    // add the emoji to the box
                    if (emoji.getType() == Emoji.Type.CUSTOM) {
                        builder.append("<img src=\"").append(emoji.asCustom().getImageUrl()).append("\" style=\"width: 16px; height: 16px; margin-top: 8px; margin-left: -8px\">");
                        builder.append("<span style=\"position: absolute; font-size: 12px; color: #dcddde; margin-top: 8px; margin-left: 8px;\">").append(reaction.getCount()).append("</span>");
                    } else {
                        builder.append("<span style=\"font-size: 12px; color: #dcddde; margin-top: 12px;  margin-left: -8px\">").append(emoji.asUnicode().getFormatted()).append("</span>");
                        builder.append("<span style=\"position: absolute; font-size: 12px; color: #dcddde; margin-top: 8px; margin-left: 8px;\">").append(reaction.getCount()).append("</span>");
                    }

                    builder.append("</div>");
                }

                builder.append("</div>");
            }

            builder.append("</div>");

            // add embeds here if they are supported
            if (!message.getEmbeds().isEmpty()) {

                for (MessageEmbed embed : message.getEmbeds()) {
                    Color color = embed.getColor();
                    if (color == null)
                        color = Color.WHITE;

                    builder.append("<div style=\"margin-left: 30px; margin-bottom: 5px; background-color: #2f3136; border-radius: 5px; border: 1px solid rgb(")
                            .append(color.getRed()).append(",")
                            .append(color.getGreen())
                            .append(",")
                            .append(color.getBlue())
                            .append("); padding: 10px; max-width: 500px;\">");

                    if (embed.getAuthor() != null) {
                        builder.append("<div style=\"margin-bottom: 10px;\"><b>").append(embed.getAuthor().getName()).append("</b></div>");
                    }

                    if (embed.getTitle() != null)
                        builder.append("<b>").append(embed.getTitle()).append("</b><br>");

                    if (embed.getDescription() != null) {
                        String description = embed.getDescription();
                        // add support for \n
                        builder.append(this.markdownFormat(description)).append("<br>");
                    }

                    if (embed.getFooter() != null)
                        builder.append("<div style=\"font-size: 12px; margin-top: 10px;\">").append(embed.getFooter().getText()).append("</div>");

                    builder.append("</div>");
                }
            }

            lastUser = message.getAuthor();

            if (!message.getAttachments().isEmpty()) {
                builder.append("<div style=\"margin-left: 30px;\">");
                for (Message.Attachment attachment : message.getAttachments()) {

                    // gross else if ladders and nested if statements
                    if (attachment.isImage()) {
                        int width = Math.min(600, attachment.getWidth());
                        int height = Math.min(600, attachment.getHeight());

                        // Spoiler Images
//                        if (attachment.isSpoiler()) {
//                            builder.append("<div style=\"margin-top: 5px; margin-bottom: 5px; background-color: #2f3136; border-radius: 5px; border: 1px solid #5865f2; padding: 1px; max-width: 500px;\">");
//                            builder.append("<div style=\"margin-bottom: 10px;\"><b>Spoiler</b></div>");
//                            builder.append("<img src=\"").append(attachment.getUrl()).append("\" style=\"width: 100%; max-width: 500px; filter: blur(50px);\" onclick=\"window.open('").append(attachment.getUrl()).append("', '_blank')\">");
//                            builder.append("</div>");
//                        } else {
                        builder.append("<img src=\"").append(attachment.getUrl()).append("\" style=\"max-width: ").append(width).append("px; max-height: ").append(height).append("px;\">");
//                        }

                    } else if (attachment.isVideo()) {
                        builder.append("<video controls style=\"max-width: 600px; max-height: 600px;\"><source src=\"").append(attachment.getUrl()).append("\" type=\"video/mp4\"></video>");
                    } else {
                        builder.append("<a href=\"").append(attachment.getUrl()).append("\">").append(attachment.getFileName()).append("</a>");
                    }
                }
                builder.append("</div>");
            }
        }

        // Finish the html file
        builder.append("</body></html>");

        // Create the file
        try {
            File archiveFolder = new File("archives");
            if (!archiveFolder.exists())
                archiveFolder.mkdir();

            File file = new File(archiveFolder, UUID.randomUUID() + ".html");
            if (!file.exists())
                file.createNewFile();

            // Write the html file to the file
            Files.write(file.toPath(), builder.toString().getBytes());
            System.out.println("* Wrote archive to " + file.getAbsolutePath());
            return file;
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * Formats the markdown to html
     *
     * @param text The text to format
     * @return The formatted text
     */
    private String markdownFormat(String text) {
        String newText = text;
        // general markdown formatting
        newText = newText.replaceAll("\n", "<br>");
        newText = newText.replaceAll("\\*\\*(.*?)\\*\\*", "<b>$1</b>");
        newText = newText.replaceAll("\\*(.*?)\\*", "<i>$1</i>");
        newText = newText.replaceAll("\\*\\*\\*(.*?)\\*\\*\\*", "<b><i>$1</i></b>");
        newText = newText.replaceAll("~~(.*?)~~", "<s>$1</s>");
        newText = newText.replaceAll("__(.*?)__", "<u>$1</u>");

        // make links starting with http:// or https:// clickable
        newText = newText.replaceAll("(https?://\\S+)", "<a href=\"$1\" style=\"color: #23a4ee;\">$1</a>");

        // Code blocks
        newText = newText.replaceAll("```(.*?)```", "<div style=\"background-color: #2f3136; padding: 5px;\">$1</div>");
        newText = newText.replaceAll("`(.*?)```", "<span style=\"background-color: #2f3136; padding: 5px;\">$1</span>");

        return newText;
    }

    public Map<Integer, File> getTicketFiles() {
        return ticketFiles;
    }

}
