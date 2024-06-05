package com.rizzqt.gbank.domain;

import java.util.UUID;

public interface StoreBankData {

    void setBalance(UUID player, String currencyId, double amount);
    void addBalance(UUID player, String currencyId, double amount);
    boolean takeBalance(UUID player, String currencyId, double amount);
    double getBalance(UUID player, String currencyId);
    void shutdown();

}
