package xyz.oribuin.lilori.handler;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import xyz.oribuin.lilori.LilOri;
import xyz.oribuin.lilori.handler.type.MessageCommand;
import xyz.oribuin.lilori.handler.type.SlashCommand;
import xyz.oribuin.lilori.handler.type.UserCommand;
import xyz.oribuin.lilori.manager.CommandManager;
import xyz.oribuin.lilori.util.Constants;

import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;

public class CommandExecutor extends ListenerAdapter {

    private final Table<Long, String, Long> cooldowns = HashBasedTable.create();
    private final CommandManager manager;

    public CommandExecutor(LilOri bot) {
        this.manager = bot.getManager(CommandManager.class);
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        BotCommand command = this.manager.getCommands().get(event.getName().toLowerCase());
        if ((!(command instanceof SlashCommand slashCommand)))
            return;

        // check if command is owner only
        if (command.getCategory() == Category.OWNER && !event.getUser().getId().equals(Constants.OWNER_ID.getValue()))
            return;

        // check if user has the required roles to use the command
        Member member = event.getMember();
        if (member != null && !command.getRequiredRoles().isEmpty()) {
            List<Long> roles = member.getRoles().stream().map(Role::getIdLong).toList();

            if (!new HashSet<>(roles).containsAll(command.getRequiredRoles())) {
                event.reply("❌ You do not have the required roles to use this command.").setEphemeral(true).queue();
                return;
            }
        }

        // check if the command is on cooldown
        if (this.isCooldown(event.getUser().getIdLong(), command.getName())) {
            Long cooldown = this.cooldowns.get(event.getUser().getIdLong(), command.getName());
            if (cooldown != null) {
                event.reply("❌ This command is on cooldown for another " + (cooldown - System.currentTimeMillis()) / 1000 + " seconds.").setEphemeral(true).queue();
                return;
            }
        }

        this.cooldowns.put(event.getUser().getIdLong(), command.getName(), System.currentTimeMillis() + command.getCooldown());

        slashCommand.execute(event);
    }

    @Override
    public void onMessageContextInteraction(@NotNull MessageContextInteractionEvent event) {
        BotCommand command = this.manager.getCommands().get(event.getName().toLowerCase());
        if ((!(command instanceof MessageCommand messageCommand)))
            return;

        // check if command is owner only
        if (command.getCategory() == Category.OWNER && !event.getUser().getId().equals(Constants.OWNER_ID.getValue()))
            return;

        // check if user has the required roles to use the command
        Member member = event.getMember();
        if (member != null && !command.getRequiredRoles().isEmpty()) {
            List<Long> roles = member.getRoles().stream().map(Role::getIdLong).toList();

            if (!new HashSet<>(roles).containsAll(command.getRequiredRoles())) {
                event.reply("❌ You do not have the required roles to use this command.").setEphemeral(true).queue();
                return;
            }
        }

        // check if the command is on cooldown
        if (this.isCooldown(event.getUser().getIdLong(), command.getName())) {
            Long cooldown = this.cooldowns.get(event.getUser().getIdLong(), command.getName());
            if (cooldown != null) {
                event.reply("❌ This command is on cooldown for another " + (cooldown - System.currentTimeMillis()) / 1000 + " seconds.").setEphemeral(true).queue();
                return;
            }
        }

        this.cooldowns.put(event.getUser().getIdLong(), command.getName(), System.currentTimeMillis() + command.getCooldown());

        messageCommand.execute(event);
    }

    @Override
    public void onUserContextInteraction(@NotNull UserContextInteractionEvent event) {
        BotCommand command = this.manager.getCommands().get(event.getName().toLowerCase());
        if ((!(command instanceof UserCommand userCommand)))
            return;

        // check if command is owner only
        if (command.getCategory() == Category.OWNER && !event.getUser().getId().equals(Constants.OWNER_ID.getValue()))
            return;

        // check if user has the required roles to use the command
        Member member = event.getMember();
        if (member != null && !command.getRequiredRoles().isEmpty()) {
            List<Long> roles = member.getRoles().stream().map(Role::getIdLong).toList();

            if (!new HashSet<>(roles).containsAll(command.getRequiredRoles())) {
                event.reply("❌ You do not have the required roles to use this command.").setEphemeral(true).queue();
                return;
            }
        }

        // check if the command is on cooldown
        if (this.isCooldown(event.getUser().getIdLong(), command.getName())) {
            Long cooldown = this.cooldowns.get(event.getUser().getIdLong(), command.getName());
            if (cooldown != null) {
                event.reply("❌ This command is on cooldown for another " + (cooldown - System.currentTimeMillis()) / 1000 + " seconds.").setEphemeral(true).queue();
                return;
            }
        }

        this.cooldowns.put(event.getUser().getIdLong(), command.getName(), System.currentTimeMillis() + command.getCooldown());
        userCommand.execute(event);
    }

    /**
     * Execute a command from a generic command event
     *
     * @param command  The command to execute
     * @param event    The event to execute the command from
     * @param consumer The consumer to execute after all checks have been passed
     */
    @SuppressWarnings("unchecked")
    private void executeCommand(BotCommand command, Member member, Consumer<BotCommand> consumer) {

        // execute the command
        consumer.accept(command);
    }

    /**
     * Check if a command is on cooldown for user
     *
     * @param userId      The user id
     * @param commandName The command name
     * @return true if on cooldown
     */
    private boolean isCooldown(long userId, String commandName) {
        Long lastUsed = this.cooldowns.get(userId, commandName);
        if (lastUsed == null) {
            return false;
        }

        long timeLeft = (lastUsed + 1000L * 5) - System.currentTimeMillis();
        return timeLeft > 0;
    }

}
