package xyz.oribuin.lilori.manager;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;
import xyz.oribuin.lilori.LilOri;
import xyz.oribuin.lilori.command.ArchiveCommand;
import xyz.oribuin.lilori.command.TicketMessageCommand;
import xyz.oribuin.lilori.handler.BotCommand;
import xyz.oribuin.lilori.handler.CommandType;
import xyz.oribuin.lilori.handler.OptionBuilder;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommandManager extends Manager {

    private final Map<String, BotCommand> commands = new HashMap<>();

    public CommandManager(LilOri bot) {
        super(bot);
    }

    @Override
    public void enable() {

    }

    public void loadCommands() {
        this.commands.clear();

        // Register all commands
        this.commands.put("archive", new ArchiveCommand());
//        this.commands.put("ticketmessage", new TicketMessageCommand());


        System.out.println("Loaded " + this.commands.size() + " commands. Registering them now.");

        // Register non legacy commands.
        final List<Guild> guilds = this.bot.getJDAInstance().getGuilds();

        for (BotCommand command : this.commands.values()) {
            if (command.getType() == CommandType.LEGACY)
                return;

            CommandData commandData = null;

            switch (command.getType()) {
                case SLASH -> commandData = Commands.slash(command.getName(), command.getDescription())
                        .setNSFW(command.isNSFW())
                        .addOptions(command.getOptionBuilders().stream().map(OptionBuilder::build).collect(Collectors.toList()));

                case USER -> commandData = Commands.user(command.getName())
                        .setGuildOnly(true)
                        .setNSFW(command.isNSFW());

                case MESSAGE -> commandData = Commands.message(command.getName())
                        .setGuildOnly(true)
                        .setNSFW(command.isNSFW());
            }

            CommandData finalCommandData = commandData;
            if (finalCommandData == null)
                return;

            guilds.forEach(guild -> {
                if (command.getRequiredGuild() != null && !command.getRequiredGuild().equals(guild.getIdLong()))
                    return;

                guild.upsertCommand(finalCommandData).queue();
            });
        }
    }

    /**
     * Delete a command from a guild or all guilds.
     *
     * @param name    The name of the command.
     * @param guildId The guild id to delete the command from.
     */
    public void disableCommand(@NotNull String name, @Nullable Long guildId) {
        if (guildId != null) {
            Guild guild = this.bot.getJDAInstance().getGuildById(guildId);
            if (guild == null)
                return;

            guild.deleteCommandById(name).queue();
            return;
        }

        final List<Guild> guilds = this.bot.getJDAInstance().getGuilds();
        guilds.forEach(guild -> guild.deleteCommandById(name).queue());
    }

    public Map<String, BotCommand> getCommands() {
        return commands;
    }

}
