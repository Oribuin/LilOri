package xyz.oribuin.lilori.handler.type;

import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import org.jetbrains.annotations.NotNull;
import xyz.oribuin.lilori.handler.BotCommand;
import xyz.oribuin.lilori.handler.CommandType;

public abstract class UserCommand implements BotCommand {

    /**
     * Execute the slash command event
     */
    public abstract void execute(UserContextInteractionEvent event);

    @NotNull
    @Override
    public CommandType getType() {
        return CommandType.USER;
    }

}