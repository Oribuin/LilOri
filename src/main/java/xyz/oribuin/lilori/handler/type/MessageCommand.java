package xyz.oribuin.lilori.handler.type;

import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import xyz.oribuin.lilori.handler.BotCommand;

public abstract class MessageCommand implements BotCommand {

    protected final MessageContextInteractionEvent event;

    public MessageCommand(MessageContextInteractionEvent event) {
        this.event = event;
    }

    /**
     * Execute the slash command event
     */
    public abstract void execute();

}