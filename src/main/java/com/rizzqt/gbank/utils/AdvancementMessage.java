package com.rizzqt.gbank.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

public class AdvancementMessage {

    private NamespacedKey id;
    private String icon;
    private String title, description;
    private String frame = "goal";
    private boolean announce = false, toast = true;
    private JavaPlugin plugin;

    public AdvancementMessage(String title, String description, String icon, JavaPlugin plugin)	{
        this(new NamespacedKey(plugin, UUID.randomUUID().toString()), title, description, icon, plugin);
    }

    public AdvancementMessage(NamespacedKey id, String title, String description, String icon, JavaPlugin plugin) {
        this.id = id;
        this.title = title;
        this.description = ChatColor.translateAlternateColorCodes('&', description);
        this.icon = icon;
        this.plugin = plugin;
    }

    public void showTo(Player player)	{
        showTo(Arrays.asList(player));
    }

    public void showTo(Collection<? extends Player> players)	{
        add();
        grant(players);
        new BukkitRunnable() {

            @Override
            public void run() {
                revoke(players);
                remove();
            }
        }.runTaskLater(plugin, 20);
    }

    @SuppressWarnings("deprecation")
    private void add()	{
        try {
            Bukkit.getUnsafe().loadAdvancement(id, getJson());
            Bukkit.getLogger().info("Advancement " + id + " saved");
        } catch (IllegalArgumentException e){
            Bukkit.getLogger().info("Error while saving, Advancement " + id + " seems to already exist");
        }
    }

    @SuppressWarnings("deprecation")
    private void remove()	{
        Bukkit.getUnsafe().removeAdvancement(id);
    }

    private void grant(Collection<? extends Player> players) {
        Advancement advancement = Bukkit.getAdvancement(id);
        AdvancementProgress progress;
        for (Player player : players)	{

            progress = player.getAdvancementProgress(advancement);
            if (!progress.isDone())	{
                for (String criteria : progress.getRemainingCriteria())	{
                    progress.awardCriteria(criteria);
                }
            }
        }
    }

    private void revoke(Collection<? extends Player> players)	{
        Advancement advancement = Bukkit.getAdvancement(id);
        AdvancementProgress progress;
        for (Player player : players)	{

            progress = player.getAdvancementProgress(advancement);
            if (progress.isDone())	{
                for (String criteria : progress.getAwardedCriteria())	{
                    progress.revokeCriteria(criteria);
                }
            }
        }
    }

    public String getJson()	{

        JsonObject json = new JsonObject();

        JsonObject icon = new JsonObject();
        icon.addProperty("item", this.icon);

        JsonObject display = new JsonObject();
        display.add("icon", icon);
        display.addProperty("title", this.title);
        display.addProperty("description", this.description);
        display.addProperty("background", "minecraft:textures/gui/advancements/backgrounds/adventure.png");
        display.addProperty("frame", this.frame);
        display.addProperty("announce_to_chat", announce);
        display.addProperty("show_toast", toast);
        display.addProperty("hidden", true);

        JsonObject criteria = new JsonObject();
        JsonObject trigger = new JsonObject();

        trigger.addProperty("trigger", "minecraft:impossible");
        criteria.add("impossible", trigger);

        json.add("criteria", criteria);
        json.add("display", display);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        return gson.toJson(json);

    }
}

