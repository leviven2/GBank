package com.rizzqt.gbank.domain.impl;

import com.rizzqt.gbank.GBank;
import com.rizzqt.gbank.domain.StoreBankData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class StoreBankDataYAML implements StoreBankData {

    private final File dataFile;
    private final FileConfiguration config;

    public StoreBankDataYAML() {
        dataFile = new File(JavaPlugin.getProvidingPlugin(GBank.class).getDataFolder(), "data.yml");
        this.config = YamlConfiguration.loadConfiguration(dataFile);
        if (this.config.getConfigurationSection("balances") == null) {
            this.config.createSection("balances");
        }
    }

    @Override
    public void setBalance(UUID player, String currencyId, double amount) {
        createPlayerIfNotExists(player);

        config.set("balances." + player + "." + currencyId + ".amount", amount);
        saveData();
    }

    @Override
    public void addBalance(UUID player, String currencyId, double amount) {
        createPlayerIfNotExists(player);

        double balance = getBalance(player, currencyId);
        config.set("balances." + player + "." + currencyId + ".amount", balance + amount);
        saveData();
    }

    @Override
    public boolean takeBalance(UUID player, String currencyId, double amount) {
        createPlayerIfNotExists(player);

        double balance = getBalance(player, currencyId);
        if (balance < amount) return false;

        config.set("balances." + player + "." + currencyId + ".amount", balance - amount);
        saveData();
        return true;
    }

    @Override
    public double getBalance(UUID player, String currencyId) {
        createPlayerIfNotExists(player);

        return config.getDouble("balances." + player + "." + currencyId + ".amount");
    }

    @Override
    public void shutdown() {
        saveData();
    }

    private void createPlayerIfNotExists(UUID player) {
        if (playerExists(player)) return;

        JavaPlugin.getPlugin(GBank.class).getCurrencies().forEach(currency -> {
            config.set("balances." + player + "." + currency.getId() + ".amount", 0.0);
            saveData();
        });
    }

    private boolean playerExists(UUID player) {
        for (final String uuid : config.getConfigurationSection("balances").getKeys(false)) {
            if (UUID.fromString(uuid).equals(player)) {
                return true;
            }
        }

        return false;
    }

    private void saveData() {
        try {
            config.save(dataFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
