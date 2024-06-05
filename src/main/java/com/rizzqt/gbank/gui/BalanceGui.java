package com.rizzqt.gbank.gui;

import com.github.stefvanschie.inventoryframework.adventuresupport.ComponentHolder;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.rizzqt.gbank.GBank;
import com.rizzqt.gbank.utils.AdvancementMessage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class BalanceGui {

    private final GBank gBank;
    private final Player player;
    private final ChestGui chestGui;
    private final PaginatedPane paginatedPane;

    public BalanceGui(GBank gBank, Player player) {
        this.gBank = gBank;
        this.player = player;
        this.chestGui = new ChestGui(6, ComponentHolder.of(Component.text("Balance view")
                .color(TextColor.fromHexString("#00FFFF"))));

        StaticPane dividerPane = new StaticPane(0, 4, 9, 1);
        dividerPane.fillWith(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), inventoryClickEvent -> inventoryClickEvent.setCancelled(true));

        paginatedPane = new PaginatedPane(0, 0, 9, 4);
        paginatedPane.populateWithGuiItems(getCurrencyItems());

        StaticPane buttonsPane = new StaticPane(0, 5, 9, 1);
        buttonsPane.addItem(new PageBackGuiItem(this), 0, 0);
        buttonsPane.addItem(new PageGoGuiItem(this), 8, 0);

        chestGui.addPane(dividerPane);
        chestGui.addPane(paginatedPane);
        chestGui.addPane(buttonsPane);

        chestGui.show(player);
    }

    private List<GuiItem> getCurrencyItems() {
        return gBank.getCurrencies().stream()
                .map(currency -> new GuiItem(new CurrencyGuiItem(currency).getItemStack(), inventoryClickEvent -> {
                    inventoryClickEvent.setCancelled(true);
                    HumanEntity player = inventoryClickEvent.getWhoClicked();
                    player.closeInventory();

                    double balance = gBank.getStoreBankData().getBalance(player.getUniqueId(), currency.getId());
                    AdvancementMessage toastNotification = new AdvancementMessage("You have " + currency.getPrefix() + balance, "Use /balance to view other currencies",
                            "emerald", gBank);
                    toastNotification.showTo((Player) player);
                }))
                .collect(Collectors.toList());
    }

    public ChestGui getChestGui() {
        return chestGui;
    }

    public Player getPlayer() {
        return player;
    }

    public PaginatedPane getPaginatedPane() {
        return paginatedPane;
    }
}
