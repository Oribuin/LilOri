package xyz.oribuin.lilori.handler;

import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import xyz.oribuin.lilori.LilOri;
import xyz.oribuin.lilori.handler.type.SlashCommand;
import xyz.oribuin.lilori.manager.CommandManager;

public class CommandExecutor extends ListenerAdapter {

    private final CommandManager manager;

    public CommandExecutor(LilOri bot) {
        this.manager = bot.getManager(CommandManager.class);
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        this.manager.getCommands().values().stream()
                .filter(command -> command.getType() == CommandType.SLASH && command.getName().equalsIgnoreCase(event.getName()))
                .findFirst()
                .ifPresent(command -> ((SlashCommand) command).execute());
    }

    @Override
    public void onMessageContextInteraction(@NotNull MessageContextInteractionEvent event) {
        this.manager.getCommands().values().stream()
                .filter(command -> command.getType() == CommandType.MESSAGE && command.getName().equalsIgnoreCase(event.getName()))
                .findFirst()
                .ifPresent(command -> ((SlashCommand) command).execute());
    }

    @Override
    public void onUserContextInteraction(@NotNull UserContextInteractionEvent event) {
        this.manager.getCommands().values().stream()
                .filter(command -> command.getType() == CommandType.USER && command.getName().equalsIgnoreCase(event.getName()))
                .findFirst()
                .ifPresent(command -> ((SlashCommand) command).execute());
    }

}
