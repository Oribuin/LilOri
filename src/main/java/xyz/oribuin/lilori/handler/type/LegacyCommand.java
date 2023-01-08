package xyz.oribuin.lilori.handler.type;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import xyz.oribuin.lilori.handler.BotCommand;
import xyz.oribuin.lilori.handler.CommandType;

public abstract class LegacyCommand implements BotCommand {

    /**
     * Execute the slash command event
     */
    public abstract void execute(MessageReceivedEvent event);

    @NotNull
    @Override
    public CommandType getType() {
        return CommandType.LEGACY;
    }

}