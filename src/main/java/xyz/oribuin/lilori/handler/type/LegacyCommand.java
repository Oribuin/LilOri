package xyz.oribuin.lilori.handler.type;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import xyz.oribuin.lilori.handler.BotCommand;

public abstract class LegacyCommand implements BotCommand {

    protected final MessageReceivedEvent event;

    public LegacyCommand(MessageReceivedEvent event) {
        this.event = event;
    }

    /**
     * Execute the slash command event
     */
    public abstract void execute();

}