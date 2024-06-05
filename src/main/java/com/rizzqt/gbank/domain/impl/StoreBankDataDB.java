package com.rizzqt.gbank.domain.impl;

import com.rizzqt.gbank.GBank;
import com.rizzqt.gbank.domain.StoreBankData;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class StoreBankDataDB implements StoreBankData {

    private final GBank gBank;
    private final HikariDataSource dataSource;

    public StoreBankDataDB() {
        gBank = JavaPlugin.getPlugin(GBank.class);
        this.dataSource = new HikariDataSource(getHikariConfig());
        createTable();
    }

    public abstract HikariConfig getHikariConfig();

    @Override
    public void setBalance(UUID player, String currencyId, double amount) {
        String updateColumn = currencyId + "_balance";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO bank (USER, " + updateColumn + ") " +
                             "VALUES (?, ?) " +
                             "ON DUPLICATE KEY UPDATE " + updateColumn + " = ?")) {

            statement.setString(1, player.toString());
            statement.setDouble(2, amount);

            statement.setDouble(3, amount);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addBalance(UUID player, String currencyId, double amount) {
        double currentBalance = getBalance(player, currencyId);
        double newBalance = currentBalance + amount;
        setBalance(player, currencyId, newBalance);
    }

    @Override
    public boolean takeBalance(UUID player, String currencyId, double amount) {
        double currentBalance = getBalance(player, currencyId);
        if (currentBalance >= amount) {
            double newBalance = currentBalance - amount;
            setBalance(player, currencyId, newBalance);
            return true;
        }

        return false;
    }

    @Override
    public double getBalance(UUID player, String currencyId) {
        String balanceColumn = currencyId + "_balance";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT " + balanceColumn + " FROM bank WHERE USER = ?")) {
            statement.setString(1, player.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble(balanceColumn);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public void shutdown() {
        dataSource.close();
    }

    public void createTable() {
        List<String> currencyIdList = gBank.getCurrencies().stream()
                .map(currency -> currency.getId() + "_balance")
                .collect(Collectors.toList());
        StringBuilder currencyIdBuilder = new StringBuilder();
        currencyIdList.forEach(currencyId -> currencyIdBuilder.append(", ").append(currencyId).append(" DECIMAL(10, 2)"));

        createTable("bank", "USER VARCHAR(36) NOT NULL PRIMARY KEY" + currencyIdBuilder);
    }

    public String getHost() {
        return gBank.getConfig().getString("db-host");
    }

    public String getPort() {
        return gBank.getConfig().getString("db-port");
    }

    public String getDatabaseName() {
        return gBank.getConfig().getString("database-name");
    }

    public String getUserName() {
        return gBank.getConfig().getString("db-username");
    }

    public String getPassword() {
        return gBank.getConfig().getString("db-password");
    }

    private void createTable(String databaseName, String columns) {
        try (Connection connection = this.dataSource.getConnection(); PreparedStatement statement =
                connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + databaseName + "(" + columns + ");")) {
            statement.execute();
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage("An error occurred while creating database table " + databaseName + ".");
            e.printStackTrace();
        }
    }

}