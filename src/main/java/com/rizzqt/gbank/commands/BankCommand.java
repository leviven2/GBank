package com.rizzqt.gbank.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import com.rizzqt.gbank.GBank;
import com.rizzqt.gbank.domain.Currency;
import com.rizzqt.gbank.utils.Language;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@CommandAlias("bank")
public class BankCommand extends BaseCommand {

    private final GBank plugin;

    public BankCommand(GBank plugin) {
        this.plugin = plugin;
    }

    @Subcommand("give")
    @Syntax("<player> <currency> <amount>")
    @Description("Give money to a player")
    public void give(Player sender, String targetName, String currency, double amount) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(targetName);
        if (!offlinePlayer.hasPlayedBefore()) {
            plugin.getMessages().sendLanguageMessageToPlayer(sender, Language.INVALID_PLAYER);
            return;
        }
        Optional<Currency> currencyOptional = plugin.findCurrency(currency);
        if (!currencyOptional.isPresent()) {
            plugin.getMessages().sendLanguageMessageToPlayer(sender, Language.INVALID_CURRENCY);
            return;
        }

        plugin.getStoreBankData().addBalance(offlinePlayer.getUniqueId(), currencyOptional.get().getId(), amount);
        plugin.getMessages().sendLanguageMessageToPlayer(sender, Language.SUCCESSFULLY_GIVEN);
    }

    @Subcommand("set")
    @Syntax("<player> <currency> <amount>")
    @Description("Set money for a player")
    public void set(Player sender, String targetName, String currency, double amount) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(targetName);
        if (!offlinePlayer.hasPlayedBefore()) {
            plugin.getMessages().sendLanguageMessageToPlayer(sender, Language.INVALID_PLAYER);
            return;
        }
        Optional<Currency> currencyOptional = plugin.findCurrency(currency);
        if (!currencyOptional.isPresent()) {
            plugin.getMessages().sendLanguageMessageToPlayer(sender, Language.INVALID_CURRENCY);
            return;
        }

        plugin.getStoreBankData().setBalance(offlinePlayer.getUniqueId(), currencyOptional.get().getId(), amount);
        plugin.getMessages().sendLanguageMessageToPlayer(sender, Language.SUCCESSFULLY_SET);
    }

    @Subcommand("take")
    @Syntax("<player> <currency> <amount>")
    @Description("Take money from a player")
    public void take(Player sender, String targetName, String currency, double amount) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(targetName);
        if (!offlinePlayer.hasPlayedBefore()) {
            plugin.getMessages().sendLanguageMessageToPlayer(sender, Language.INVALID_PLAYER);
            return;
        }
        Optional<Currency> currencyOptional = plugin.findCurrency(currency);
        if (!currencyOptional.isPresent()) {
            plugin.getMessages().sendLanguageMessageToPlayer(sender, Language.INVALID_CURRENCY);
            return;
        }

        boolean success = plugin.getStoreBankData().takeBalance(offlinePlayer.getUniqueId(), currencyOptional.get().getId(), amount);
        if (!success) {
            plugin.getMessages().sendLanguageMessageToPlayer(sender, Language.INSUFFICIENT_BALANCE);
            return;
        }
        
        plugin.getMessages().sendLanguageMessageToPlayer(sender, Language.SUCCESSFULLY_TAKEN);
    }

    @Subcommand("reload")
    @Description("Reload the config file")
    public void reload(Player sender) {
        plugin.reloadConfig();
        plugin.saveConfig();
        plugin.setLanguageConfig(YamlConfiguration.loadConfiguration(plugin.getLanguageFile()));
        plugin.loadCurrenciesFromConfig();
        plugin.getMessages().sendLanguageMessageToPlayer(sender, Language.SUCCESSFULLY_RELOADED);
    }

}
