package com.rizzqt.gbank.utils;

public class Placeholder {

    private final String placeholder;
    private final String replacement;

    public Placeholder(String placeholder, String replacement) {
        this.placeholder = placeholder;
        this.replacement = replacement;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public String getReplacement() {
        return replacement;
    }
}
