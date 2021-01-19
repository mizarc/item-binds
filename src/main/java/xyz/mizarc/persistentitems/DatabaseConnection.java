package xyz.mizarc.persistentitems;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseConnection {
    private PersistentItems plugin;
    private Connection connection = null;

    public DatabaseConnection(PersistentItems plugin) {
        this.plugin = plugin;
        openConnection();
        createTable();
    }

    public void openConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + plugin.getDataFolder() + "/data.db");
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }

    private void createTable() {
        // Create table if it doesn't exit
        String sqlCreate = "CREATE TABLE IF NOT EXISTS active_items (player TEXT NOT NULL, item TEXT NOT NULL, " +
                "world TEXT NOT NULL);";

        try {
            PreparedStatement statement = connection.prepareStatement(sqlCreate);
            statement.executeUpdate();
            statement.close();

        } catch (SQLException error) {
            error.printStackTrace();
        }

    }
}
