package com.rizzqt.gbank.domain;

public class Currency {

    private final String id;
    private final String name;
    private final String prefix;

    public Currency(String id, String name, String prefix) {
        this.id = id;
        this.name = name;
        this.prefix = prefix;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

}
