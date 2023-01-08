package xyz.oribuin.lilori.ticket;


import net.dv8tion.jda.api.entities.Member;

public class Ticket {

    private final Member owner; // The owner of the ticket
    private final TicketType type; // The type of ticket
    private final String subject; // The subject of the ticket
    private String plugin; // The plugin that created the ticket
    private String version; // The version of the plugin

    public Ticket(Member owner, TicketType type, String subject) {
        this.owner = owner;
        this.type = type;
        this.subject = subject;
        this.plugin = null;
        this.version = null;
    }

    public Member getOwner() {
        return owner;
    }

    public TicketType getType() {
        return type;
    }

    public String getSubject() {
        return subject;
    }

    public String getPlugin() {
        return plugin;
    }

    public void setPlugin(String plugin) {
        this.plugin = plugin;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
