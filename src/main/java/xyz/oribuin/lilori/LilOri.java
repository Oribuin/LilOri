package xyz.oribuin.lilori;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.NotNull;
import xyz.oribuin.lilori.handler.CommandExecutor;
import xyz.oribuin.lilori.listener.support.FAQListeners;
import xyz.oribuin.lilori.listener.support.GenericSupportListeners;
import xyz.oribuin.lilori.listener.support.TicketListeners;
import xyz.oribuin.lilori.manager.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class LilOri extends ListenerAdapter {

    private final Map<Class<? extends Manager>, Manager> managers = new LinkedHashMap<>();

    private static LilOri instance;
    private JDA jdaInstance;

    public static LilOri getInstance() {
        return instance;
    }

    /**
     * Load the main instance of the bot.
     *
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        new LilOri();
    }

    public LilOri() {
        final String token = this.loadToken();
        if (token == null) {
            System.out.println(" * Failed to load token. Please check the token.txt file.");
            return;
        }

        // Set the instance of the bot.
        instance = this;


        JDABuilder builder = JDABuilder.createDefault(token, List.of(GatewayIntent.values()));

        builder.addEventListeners(this,
                // Support Server Listeners
                new FAQListeners(this), // FAQ System
                new GenericSupportListeners(), // Generic Support Listeners
                new TicketListeners(), // Ticket System

                // Command Handler
                new CommandExecutor(this)
        );

        builder.setEnabledIntents(Arrays.asList(GatewayIntent.values()));
        this.jdaInstance = builder.build();


        // Load the managers.
        this.getManager(CommandManager.class);
        this.getManager(DataManager.class);
        this.getManager(TicketManager.class);

    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        this.getManager(CommandManager.class).loadCommands();
    }

    /**
     * @return The bot token from the token.txt file.
     */
    private String loadToken() {
        try {
            return Files.readString(Paths.get("token.txt"));
        } catch (IOException ex) {
            return null;
        }
    }

    /**
     * Gets a manager instance
     *
     * @param managerClass The class of the manager to get
     * @param <T> extends Manager
     * @return A new or existing instance of the given manager class
     */
    @SuppressWarnings("unchecked")
    public final <T extends Manager> T getManager(Class<T> managerClass) {
        if (this.managers.containsKey(managerClass))
            return (T) this.managers.get(managerClass);

        try {
            T manager = managerClass.getConstructor(LilOri.class).newInstance(this);
            this.managers.put(managerClass, manager);
            manager.enable();
            return manager;
        } catch (Exception ex) {
            throw new ManagerNotFoundException(managerClass, ex);
        }
    }

    public JDA getJDAInstance() {
        return jdaInstance;
    }

    /**
     * An exception thrown when a Manager fails to load
     */
    private static class ManagerNotFoundException extends RuntimeException {

        public ManagerNotFoundException(Class<? extends Manager> managerClass, Throwable cause) {
            super("Failed to load " + managerClass.getSimpleName(), cause);
        }

    }

}
