package com.rizzqt.gbank.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;

public class PageGoGuiItem extends GuiItem {

    public PageGoGuiItem(BalanceGui balanceGui) {
        super(new PageGoItem().getItemStack(), inventoryClickEvent -> {
            inventoryClickEvent.setCancelled(true);
            PaginatedPane paginatedPane = balanceGui.getPaginatedPane();
            if (paginatedPane.getPage() >= paginatedPane.getPages() - 1) return;

            paginatedPane.setPage(paginatedPane.getPage() + 1);
            balanceGui.getChestGui().show(balanceGui.getPlayer());
        });
    }

}
