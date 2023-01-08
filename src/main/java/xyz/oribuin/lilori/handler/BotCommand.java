package xyz.oribuin.lilori.handler;

import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public interface BotCommand {

    /**
     * @return The name of the command.
     */
    @NotNull
    String getName();

    /**
     * @return The description of the command.
     */
    @NotNull
    String getDescription();

    /**
     * @return The command type.
     */
    @NotNull
    CommandType getType();

    /**
     * @return The command usage.
     */
    @Nullable
    String getUsage();

    /**
     * @return The command aliases.
     */
    @NotNull
    String[] getAliases();

    /**
     * @return The command category.
     */
    @NotNull
    Category getCategory();

    /**
     * @return The command cooldown (in seconds).
     */
    int getCooldown();

    /**
     * @return The command permission.
     */
    @Nullable
    Long getRequiredRole();

    /**
     * @return The command guild.
     */
    @Nullable
    Long getRequiredGuild();

    /**
     * @return If the command is NSFW.
     */
    boolean isNSFW();

    /**
     * @return If the command is owner only.
     */
    boolean isOwnerOnly();

    /**
     * @return Create the command options.
     */
    @NotNull
    List<OptionBuilder> getOptionBuilders();

}
