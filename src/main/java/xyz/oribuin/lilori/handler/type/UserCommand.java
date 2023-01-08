package xyz.oribuin.lilori.handler.type;

import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import xyz.oribuin.lilori.handler.BotCommand;

public abstract class UserCommand implements BotCommand {

    protected final UserContextInteractionEvent event;

    public UserCommand(UserContextInteractionEvent event) {
        this.event = event;
    }

    /**
     * Execute the slash command event
     */
    public abstract void execute();

}