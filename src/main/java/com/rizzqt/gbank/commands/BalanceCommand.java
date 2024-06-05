package com.rizzqt.gbank.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Syntax;
import com.rizzqt.gbank.GBank;
import com.rizzqt.gbank.domain.Currency;
import com.rizzqt.gbank.gui.BalanceGui;
import com.rizzqt.gbank.utils.Language;
import com.rizzqt.gbank.utils.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Optional;

public class BalanceCommand extends BaseCommand {

    private final GBank plugin;

    public BalanceCommand(GBank plugin) {
        this.plugin = plugin;
    }

    @CommandAlias("balance")
    @Description("Open ui to view currencies")
    public void balance(Player sender) {
        new BalanceGui(plugin, sender);
    }

    @CommandAlias("balance")
    @Syntax("<currency>")
    @Description("View the balance for the specified currency")
    public void currencyBalance(Player sender, String currency) {
        Optional<Currency> currencyOptional = plugin.findCurrency(currency);
        if (!currencyOptional.isPresent()) {
            plugin.getMessages().sendLanguageMessageToPlayer(sender, Language.INVALID_CURRENCY);
            return;
        }

        double balance = plugin.getStoreBankData().getBalance(sender.getUniqueId(), currencyOptional.get().getId());
        plugin.getMessages().sendLanguageMessageToPlayer(sender, Language.BALANCE,
                new Placeholder("%balance%", currencyOptional.get().getPrefix() + balance));
    }

    @CommandAlias("balance")
    @Syntax("<currency> <player>")
    @Description("View a specific player's balance for a specified currency")
    public void currencyBalanceOther(Player sender, String currency, String playerName) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
        if (!offlinePlayer.hasPlayedBefore()) {
            plugin.getMessages().sendLanguageMessageToPlayer(sender, Language.INVALID_PLAYER);
            return;
        }
        Optional<Currency> currencyOptional = plugin.findCurrency(currency);
        if (!currencyOptional.isPresent()) {
            plugin.getMessages().sendLanguageMessageToPlayer(sender, Language.INVALID_CURRENCY);
            return;
        }

        double balance = plugin.getStoreBankData().getBalance(offlinePlayer.getUniqueId(), currencyOptional.get().getId());
        plugin.getMessages().sendLanguageMessageToPlayer(sender, Language.BALANCE,
                new Placeholder("%balance%", currencyOptional.get().getPrefix() + balance));
    }

    @CommandAlias("pay")
    @Syntax("<player> <currency> <amount>")
    @Description("Pay another player")
    public void pay(Player sender, String targetName, String currency, double amount) {
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

        double balance = plugin.getStoreBankData().getBalance(sender.getUniqueId(), currencyOptional.get().getId());
        if (balance < amount) {
            plugin.getMessages().sendLanguageMessageToPlayer(sender, Language.INSUFFICIENT_BALANCE);
            return;
        }

        plugin.getStoreBankData().takeBalance(sender.getUniqueId(), currencyOptional.get().getId(), amount);
        plugin.getStoreBankData().addBalance(offlinePlayer.getUniqueId(), currencyOptional.get().getId(), amount);
        plugin.getMessages().sendLanguageMessageToPlayer(sender, Language.SUCCESSFULLY_PAID);

        Placeholder amountPlaceholder = new Placeholder("%amount%", String.valueOf(amount));
        Placeholder senderNamePlaceholder = new Placeholder("%sender%", sender.getName());
        if (offlinePlayer.isOnline()) {
            plugin.getMessages().sendLanguageMessageToPlayer(offlinePlayer.getPlayer(), Language.MONEY_RECEIVED, amountPlaceholder, senderNamePlaceholder);
            return;
        }

        plugin.getMessages().sendLanguageMessageToOfflinePlayer(offlinePlayer.getUniqueId(), Language.MONEY_RECEIVED, amountPlaceholder, senderNamePlaceholder);
    }

}
