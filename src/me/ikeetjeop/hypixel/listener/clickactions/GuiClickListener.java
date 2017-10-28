package me.ikeetjeop.hypixel.listener.clickactions;

import me.ikeetjeop.hypixel.HypixelCore;
import me.ikeetjeop.hypixel.utilities.Guis.MvpPluseGui;
import me.ikeetjeop.hypixel.utilities.configmanagement.PlusGUI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Created by @Ikeetjeop aka Nick on 30/08/2017.
 * If this project not uploaded on github you're not allowed to
 * use this code for your project!
 */
public class GuiClickListener implements Listener {

    PlusGUI config = new PlusGUI();
    MvpPluseGui GUI = new MvpPluseGui();

    @EventHandler
    public void inventoryClickEvent(InventoryClickEvent event) {
        HumanEntity player = event.getWhoClicked();
        if ((event.getInventory().getTitle().equals("My Profile"))) {
            if (event.getCurrentItem().getType() == Material.INK_SACK && event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "MVP+ Rankcolor")) {
                GUI.inv(HypixelCore.getInstance(), (Player) player);
                event.setCancelled(true);
            } else {
                event.setCancelled(true);
            }
        } else if(event.getInventory().getTitle().equals(ChatColor.translateAlternateColorCodes('&', config.getString("PlusGUI.Settings.GUI.Title")))){
            event.setCancelled(true);
        }
    }
}