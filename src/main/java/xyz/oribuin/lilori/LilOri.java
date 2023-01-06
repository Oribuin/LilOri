package xyz.oribuin.lilori;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import xyz.oribuin.lilori.manager.Manager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LilOri extends ListenerAdapter {

    private final Map<Class<? extends Manager>, Manager> managers = new HashMap<>();

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

        instance = this;

        JDABuilder builder = JDABuilder.createDefault(token, List.of(
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_WEBHOOKS,
                GatewayIntent.GUILD_INVITES,
                GatewayIntent.GUILD_PRESENCES,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_MESSAGE_REACTIONS,
                GatewayIntent.DIRECT_MESSAGES,
                GatewayIntent.DIRECT_MESSAGE_REACTIONS,
                GatewayIntent.MESSAGE_CONTENT
        ));

        builder.setEnabledIntents(Arrays.asList(GatewayIntent.values()));
        this.jdaInstance = builder.build();
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

    public JDA getJDAInstance() {
        return jdaInstance;
    }

}
