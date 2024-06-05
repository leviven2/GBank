package com.rizzqt.gbank.api;

import com.rizzqt.gbank.GBank;
import com.rizzqt.gbank.domain.Currency;

import java.util.Optional;
import java.util.UUID;

public class GBankAPI {

    private GBankAPI() {
    }

    private static GBank plugin;

    public static double getBalance(UUID playerUuid, String currency) {
        Optional<Currency> currencyOptional = plugin.findCurrency(currency);
        return currencyOptional.map(value ->
                plugin.getStoreBankData().getBalance(playerUuid, value.getId())).orElse(0.0);
    }

    public static void setPlugin(GBank plugin) {
        GBankAPI.plugin = plugin;
    }

}