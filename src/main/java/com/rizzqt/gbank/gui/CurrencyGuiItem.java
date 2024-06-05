package com.rizzqt.gbank.gui;

import com.rizzqt.gbank.domain.Currency;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CurrencyGuiItem {

    private final ItemStack itemStack;

    public CurrencyGuiItem(Currency currency) {
        itemStack = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return;

        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', currency.getName()));
        List<String> loreList = new ArrayList<>();
        loreList.add(ChatColor.translateAlternateColorCodes('&', "&7Click to view your balance"));
        itemMeta.setLore(loreList);

        itemStack.setItemMeta(itemMeta);
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
