package xyz.oribuin.lilori.handler.type;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import xyz.oribuin.lilori.handler.BotCommand;

public abstract class SlashCommand implements BotCommand {

    protected final SlashCommandInteractionEvent event;

    public SlashCommand(SlashCommandInteractionEvent event) {
        this.event = event;
    }

    /**
     * Execute the slash command event
     */
    public abstract void execute();

}