package com.rizzqt.gbank.runnables;

import com.rizzqt.gbank.GBank;
import com.rizzqt.gbank.domain.Currency;
import com.rizzqt.gbank.utils.Language;
import com.rizzqt.gbank.utils.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class MoneyPayRunnable extends BukkitRunnable {

    private final GBank plugin;

    public MoneyPayRunnable(GBank plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        double moneyPayAmount = plugin.getConfig().getDouble("money-pay-amount");
        List<Currency> currencies = plugin.getCurrencies();
        Bukkit.getOnlinePlayers().forEach(player -> {
            currencies.forEach(currency -> {
                plugin.getStoreBankData().addBalance(player.getUniqueId(), currency.getId(), moneyPayAmount);
            });

            Placeholder amountPlaceholder = new Placeholder("%amount%", moneyPayAmount + "");
            plugin.getMessages().sendLanguageMessageToPlayer(player, Language.SCHEDULED_PAYMENT, amountPlaceholder);
        });
    }
}
