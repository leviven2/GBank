package com.rizzqt.gbank.gui;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PageBackItem {

    private final ItemStack itemStack;

    public PageBackItem() {
        itemStack = new ItemStack(Material.ARROW);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return;

        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                "&aGo to previous page"));
        itemStack.setItemMeta(itemMeta);
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
