package com.rizzqt.gbank;

import co.aikar.commands.BukkitCommandManager;
import com.rizzqt.gbank.api.GBankAPI;
import com.rizzqt.gbank.commands.BalanceCommand;
import com.rizzqt.gbank.commands.BankCommand;
import com.rizzqt.gbank.domain.Currency;
import com.rizzqt.gbank.domain.StoreBankData;
import com.rizzqt.gbank.domain.impl.StoreBankDataMySQL;
import com.rizzqt.gbank.domain.impl.StoreBankDataYAML;
import com.rizzqt.gbank.listeners.PlayerJoinListener;
import com.rizzqt.gbank.runnables.MoneyPayRunnable;
import com.rizzqt.gbank.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public final class GBank extends JavaPlugin {

    private final List<Currency> currencies = new ArrayList<>();
    private final HashMap<UUID, String> playersToSendMessage = new HashMap<>();
    private StoreBankData storeBankData;
    private YamlConfiguration languageConfig;
    private File languageFile;
    private Messages messages;

    @Override
    public void onEnable() {
        this.getConfig().options().parseComments(true);
        this.getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        languageFile = new File(getDataFolder(), "language.yml");
        if (!languageFile.exists()) {
            languageFile.getParentFile().mkdirs();
            saveResource("language.yml", false);
        }

        languageConfig = YamlConfiguration.loadConfiguration(languageFile);
        languageConfig.options().copyDefaults(true);

        loadCurrenciesFromConfig();

        String storageType = this.getConfig().getString("storage-type");
        if (storageType.equalsIgnoreCase("mysql")) {
            storeBankData = new StoreBankDataMySQL();
        } else if (storageType.equalsIgnoreCase("file")) {
            storeBankData = new StoreBankDataYAML();
        } else {
            storeBankData = new StoreBankDataYAML();
        }

        messages = new Messages(this);

        BukkitCommandManager aikarCommandRegistrator = new BukkitCommandManager(this);
        aikarCommandRegistrator.registerCommand(new BalanceCommand(this));
        aikarCommandRegistrator.registerCommand(new BankCommand(this));

        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(this), this);

        GBankAPI.setPlugin(this);

        MoneyPayRunnable moneyPayRunnable = new MoneyPayRunnable(this);
        int moneyPayIntervalInSeconds = this.getConfig().getInt("money-pay-interval");
        moneyPayRunnable.runTaskTimerAsynchronously(this,
                moneyPayIntervalInSeconds * 20L, moneyPayIntervalInSeconds * 20L);
    }

    @Override
    public void onDisable() {
        storeBankData.shutdown();
    }

    public void loadCurrenciesFromConfig() {
        this.currencies.clear();
        for (String currency : this.getConfig().getConfigurationSection("currencies").getKeys(false)) {
            String name = this.getConfig().get("currencies." + currency + ".name").toString();
            String prefix = this.getConfig().get("currencies." + currency + ".prefix").toString();
            this.currencies.add(new Currency(currency, name, prefix));
        }
    }

    public List<Currency> getCurrencies() {
        return this.currencies;
    }

    public HashMap<UUID, String> getPlayersToSendMessage() {
        return playersToSendMessage;
    }

    public StoreBankData getStoreBankData() {
        return this.storeBankData;
    }

    public YamlConfiguration getLanguageConfig() {
        return languageConfig;
    }

    public void setLanguageConfig(YamlConfiguration languageConfig) {
        this.languageConfig = languageConfig;
    }

    public File getLanguageFile() {
        return languageFile;
    }

    public Messages getMessages() {
        return messages;
    }

    public Optional<Currency> findCurrency(String currency) {
        Optional<Currency> currencyByName = this.currencies.stream()
                .filter(c -> c.getName().equalsIgnoreCase(currency)).findFirst();
        if (currencyByName.isPresent()) return currencyByName;

        return this.currencies.stream()
                .filter(c -> c.getId().equalsIgnoreCase(currency)).findFirst();
    }


}
