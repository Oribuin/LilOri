package xyz.oribuin.lilori.command;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import net.dv8tion.jda.api.utils.FileUpload;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.oribuin.lilori.LilOri;
import xyz.oribuin.lilori.handler.Category;
import xyz.oribuin.lilori.handler.OptionBuilder;
import xyz.oribuin.lilori.handler.type.SlashCommand;
import xyz.oribuin.lilori.manager.TicketManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ArchiveCommand extends SlashCommand {

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        File file = LilOri.getInstance().getManager(TicketManager.class)
                .createFileArchive(event.getChannel().asTextChannel());

        ReplyCallbackAction action = event.reply("Here is the archive of this channel.")
                .addFiles(FileUpload.fromData(file));

        action.queue(interactionHook -> interactionHook.deleteOriginal().queueAfter(30, TimeUnit.SECONDS));
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
