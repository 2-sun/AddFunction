package com.sun2.addfunction;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class AFListener implements Listener {
    AddFunction mainPlugin;

    public AFListener(AddFunction plugin) {
        mainPlugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        try {
            Player target = mainPlugin.getServer().getPlayer(event.getView().getTitle()); // 클릭한 인벤토리의 이름이 플레이어의 이름이면 target에 저장
            Inventory inv = event.getInventory();

            if (event.getSlot() == 53) { // 클릭한 slot이 53(초록 유리판)이라면
                for (int i=0; i<36; i++) {
                    target.getInventory().setItem(i, inv.getItem(i));
                }
                target.getInventory().setHelmet(inv.getItem(47));
                target.getInventory().setChestplate(inv.getItem(48));
                target.getInventory().setLeggings(inv.getItem(49));
                target.getInventory().setBoots(inv.getItem(50));
            }
            else if (event.getSlot() == 52) { // 클릭한 slot이 52(빨간 유리판)이라면
                event.getWhoClicked().closeInventory();
            }
        } catch (Exception e) { // 클릭한 인벤토리의 이름이 플레이어의 이름이 아니라면ㄱ
            return;
        }
    }
}
