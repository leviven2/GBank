package com.rizzqt.gbank.utils;

public enum Language {

    INVALID_CURRENCY("lang.invalid-currency"),
    INVALID_PLAYER("lang.invalid-player"),
    INSUFFICIENT_BALANCE("lang.insufficient-balance"),
    BALANCE("lang.balance"),
    SUCCESSFULLY_GIVEN("lang.successfully-given"),
    SUCCESSFULLY_TAKEN("lang.successfully-taken"),
    SUCCESSFULLY_SET("lang.successfully-set"),
    SUCCESSFULLY_RELOADED("lang.successfully-reloaded"),
    SUCCESSFULLY_PAID("lang.successfully-paid"),
    MONEY_RECEIVED("lang.money-received"),
    SCHEDULED_PAYMENT("lang.scheduled-payment");

    private final String path;

    Language(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

}
