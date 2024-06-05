package com.rizzqt.gbank.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;

public class PageBackGuiItem extends GuiItem {

    public PageBackGuiItem(BalanceGui balanceGui) {
        super(new PageGoItem().getItemStack(), inventoryClickEvent -> {
            inventoryClickEvent.setCancelled(true);
            PaginatedPane paginatedPane = balanceGui.getPaginatedPane();
            if (paginatedPane.getPage() <= 0) return;

            paginatedPane.setPage(paginatedPane.getPage() - 1);
            balanceGui.getChestGui().show(balanceGui.getPlayer());
        });
    }

}
