package xyz.oribuin.lilori.manager;

import xyz.oribuin.lilori.LilOri;
import xyz.oribuin.lilori.database.DatabaseConnector;
import xyz.oribuin.lilori.database.SQLiteConnector;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class DataManager extends Manager {

    private final Map<String, String> faqMap = new HashMap<>();
    private DatabaseConnector connector;

    public DataManager(LilOri bot) {
        super(bot);
    }

    @Override
    public void enable() {
        final File file = new File("database.db");
        try {
            if (!file.exists()) {
                file.createNewFile();
                System.out.println(" * Creating database file.");
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        this.connector = new SQLiteConnector(file);
        this.connector.cleanup();

        System.out.println(" * Connected to the database successfully.");
        System.out.println(" * Creating tables if they don't exist.");
        this.createTables();

        System.out.println(" * Loading Database Values.");
        this.loadValues();
    }

    /**
     * Create the tables if they don't exist
     */
    public void createTables() {
        this.connector.connect(connection -> {
            String createFaqs = "CREATE TABLE IF NOT EXISTS lilori_faqs (id TEXT PRIMARY KEY, answer TEXT);";
            try (PreparedStatement statement = connection.prepareStatement(createFaqs)) {
                statement.executeUpdate();
            }
        });
    }

    /**
     * Load the FAQ's from the database
     */
    public void loadValues() {
        this.connector.connect(connection -> {
            String loadFaqs = "SELECT * FROM lilori_faqs;";
            try (PreparedStatement statement = connection.prepareStatement(loadFaqs)) {
                ResultSet result = statement.executeQuery();
                while (result.next()) {
                    this.faqMap.put(result.getString("id"), result.getString("answer"));
                }
            }
        });
    }

    /**
     * Save the FAQ's to the database
     *
     * @param id     The FAQ ID
     * @param answer The FAQ Answer
     */
    public void saveFAQ(String id, String answer) {
        this.connector.connect(connection -> {
            String saveFAQ = "REPLACE INTO lilori_faqs (id, answer) VALUES (?, ?);";
            try (PreparedStatement statement = connection.prepareStatement(saveFAQ)) {
                statement.setString(1, id);
                statement.setString(2, answer);
                statement.executeUpdate();
            }
        });
    }

    /**
     * Delete a FAQ from the database
     *
     * @param id The FAQ ID
     */
    public void deleteFAQ(String id) {
        this.connector.connect(connection -> {
            String deleteFAQ = "DELETE FROM lilori_faqs WHERE id = ?;";
            try (PreparedStatement statement = connection.prepareStatement(deleteFAQ)) {
                statement.setString(1, id);
                statement.executeUpdate();
            }
        });
    }

    public Map<String, String> getFaqMap() {
        return faqMap;
    }

}
