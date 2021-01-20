package xyz.mizarc.persistentitems;

import java.sql.*;

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

    public boolean isHidden(String playerUUID, String itemID, String world) {
        String query = "SELECT * FROM hidden_items WHERE player=? AND item=? AND world=?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, playerUUID);
            statement.setString(2, itemID);
            statement.setString(3, world);
            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                return false;
            }
            return true;

        } catch (SQLException error) {
            error.printStackTrace();
        }
        return false;
    }

    public boolean addHidden(String playerUUID, String itemID, String world) {
        String query = "INSERT INTO hidden_items (player, item, world) VALUES (?, ?, ?)";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, playerUUID);
            statement.setString(2, itemID);
            statement.setString(3, world);
            statement.executeUpdate();
            statement.close();

        } catch (SQLException error) {
            error.printStackTrace();
        }
    }

    public boolean removeHidden(String playerUUID, String itemID, String world) {
        String query = "DELETE FROM hidden_items WHERE player=? AND item=? AND world=?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, playerUUID);
            statement.setString(2, itemID);
            statement.setString(3, world);
            statement.executeUpdate();
            statement.close();

        } catch (SQLException error) {
            error.printStackTrace();
        }
    }

    private void createTable() {
        // Create table if it doesn't exit
        String sqlCreate = "CREATE TABLE IF NOT EXISTS hidden_items (player TEXT NOT NULL, item TEXT NOT NULL, " +
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
