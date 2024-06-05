package com.rizzqt.gbank.utils;

import com.rizzqt.gbank.GBank;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Messages {

    private final GBank gBank;

    public Messages(GBank gBank) {
        this.gBank = gBank;
    }

    public void sendMessageToPlayer(Player player, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public void sendLanguageMessageToPlayer(Player player, Language language) {
        String message = gBank.getLanguageConfig().getString(language.getPath());
        if (message == null) return;

        sendMessageToPlayer(player, message);
    }

    public void sendLanguageMessageToPlayer(Player player, Language language, Placeholder... placeholders) {
        String message = gBank.getLanguageConfig().getString(language.getPath());
        if (message == null) return;

        message = replacePlaceholders(placeholders, message);

        sendMessageToPlayer(player, message);
    }

    public void sendLanguageMessageToOfflinePlayer(UUID offlinePlayer, Language language) {
        String message = gBank.getLanguageConfig().getString(language.getPath());
        if (message == null) return;

        gBank.getPlayersToSendMessage().put(offlinePlayer, message);
    }

    public void sendLanguageMessageToOfflinePlayer(UUID offlinePlayer, Language language, Placeholder... placeholders) {
        String message = gBank.getLanguageConfig().getString(language.getPath());
        if (message == null) return;

        message = replacePlaceholders(placeholders, message);

        gBank.getPlayersToSendMessage().put(offlinePlayer, message);
    }

    private String replacePlaceholders(Placeholder[] placeholders, String message) {
        for (Placeholder placeholder : placeholders) {
            message = message.replace(placeholder.getPlaceholder(), placeholder.getReplacement());
        }
        return message;
    }

}
