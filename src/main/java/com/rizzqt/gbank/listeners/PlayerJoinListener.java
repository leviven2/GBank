package com.rizzqt.gbank.listeners;

import com.rizzqt.gbank.GBank;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final GBank plugin;

    public PlayerJoinListener(GBank plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!plugin.getPlayersToSendMessage().containsKey(event.getPlayer().getUniqueId())) return;

        plugin.getMessages().sendMessageToPlayer(event.getPlayer(), plugin.getPlayersToSendMessage().get(event.getPlayer().getUniqueId()));
        plugin.getPlayersToSendMessage().remove(event.getPlayer().getUniqueId());
    }

}
