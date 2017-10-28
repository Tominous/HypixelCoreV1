package me.ikeetjeop.hypixel.utilities.Guis;

import me.ikeetjeop.hypixel.HypixelCore;
import me.ikeetjeop.hypixel.mysql.mysql.MySQL;
import me.ikeetjeop.hypixel.utilities.UUIDFetcher;
import me.ikeetjeop.hypixel.utilities.configmanagement.PlusGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by @Ikeetjeop aka Nick on 6/09/2017.
 * If this project not uploaded on github you're not allowed to
 * use this code for your project!
 */

public class MvpPluseGui {

    public static ItemStack itemBuilder(Player p, Material material, int amount, int type, String name, String loreConfig) {
        ItemStack item = new ItemStack(material, amount, (short) type);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GREEN + name);
        PlusGUI config = new PlusGUI();
        String unlocked = unlocked(p) ? "Unlocked" : "Locked";

        //LORE
        List<String> lore = config.getStringList(loreConfig);
        List<String> coloredLore = new ArrayList<>();
        for (String s : lore) {
            coloredLore.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        for (String colors : config.getConfigurationSection("PlusGUI.Colors")) {
            if (p.getLevel() >= config.getInt("PlusGUI.Colors." + colors + ".Level")) {
                coloredLore.add(unlocked);
                itemMeta.setLore(coloredLore);
            } else {
                coloredLore.add(unlocked);
                itemMeta.setLore(coloredLore);
            }
        }
        //LORE STOP
        itemMeta.setLore(coloredLore);
        item.setItemMeta(itemMeta);
        return item;
    }

    /*
        public static ItemStack mvpBuilder(Material material, short type, String Rank, String ColorName, String pluseColor, int uLevel) {
            ItemStack item = new ItemStack(material, 1, type);
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(ChatColor.RED + ColorName + " " + Rank + "Rank Color");
            itemMeta.setLore(Arrays.asList(
                    ChatColor.translateAlternateColorCodes('&', ChatColor.GRAY + "Changes the color to the plus in " + HypixelCore.mvpp.replace("{plus}", HypixelCore.getInstance().getConfig().getString("Hypixel.Settings.Default.Plus")) + ChatColor.GRAY + "."),
                    ChatColor.translateAlternateColorCodes('&', ChatColor.GRAY + "to " + ColorName + ",turning it into " + HypixelCore.mvpp.replace("{plus}", pluseColor)),
                    ChatColor.translateAlternateColorCodes('&', ""),
                    ChatColor.translateAlternateColorCodes('&', ChatColor.GRAY + "Shown in tab list also when chatting and joining lobbies"),
                    ChatColor.translateAlternateColorCodes('&', ""),
                    ChatColor.translateAlternateColorCodes('&', ChatColor.DARK_AQUA + "Unlocked at Hypixel Level " + uLevel)));

            item.setItemMeta(itemMeta);
            return item;
        }
    */
    public static void inv(HypixelCore plugin, Player p) {
        PlusGUI config = new PlusGUI();
        Inventory inv = plugin.getServer().createInventory(null, 9 * config.getInt("PlusGUI.Settings.GUI.Size"), ChatColor.translateAlternateColorCodes('&', config.getString("PlusGUI.Settings.GUI.Title")));
        for (String colors : config.getConfigurationSection("PlusGUI.Colors")) {
            if (Material.matchMaterial(config.getString("PlusGUI.Colors." + colors + ".Item").toUpperCase()) != null) {
                inv.setItem((config.getInt("PlusGUI.Colors." + colors + ".Slot") - 1), itemBuilder(p, Material.matchMaterial(config.getString("PlusGUI.Colors." + colors + ".Item").toUpperCase()), 1, config.getInt("PlusGUI.Colors." + colors + ".Data"), ChatColor.translateAlternateColorCodes('&', config.getString("PlusGUI.Colors." + colors + ".Name")), "PlusGUI.Colors." + colors + ".Lore"));
            } else {
                Bukkit.getLogger().log(Level.WARNING, "[HyPixelcore] [ERROR] " + Time.valueOf(LocalTime.now()) + " Unknow Item found in inventory: " + inv.getName());
                return;
            }
        }
        p.openInventory(inv);
    }

    public static String status(Player p) {
        HypixelCore plugin = HypixelCore.getInstance();
        PlusGUI config = new PlusGUI();
        for (String colors : config.getConfigurationSection("PlusGUI.Colors")) {
            try {
                try {
                    MySQL mySQL = new MySQL(plugin.sqlHost, plugin.sqlPort, plugin.sqlDb, plugin.sqlUser, plugin.sqlPw);
                    Statement statement = mySQL.openConnection().createStatement();
                    ResultSet res = statement.executeQuery("SELECT * FROM players WHERE UUID='" + UUIDFetcher.getUUIDOf(p.getName()) + "'");
                    if (res.next()) {
                        if (res.getInt("Level") >= config.getInt("PlusGUI.Colors." + colors + ".Level")) {
                            return config.getString("PlusGUI.Settings.Status.Unlocked");
                        } else {
                            return config.getString("PlusGUI.Settings.Status.NeedMoreExp");
                        }
                    }
                } catch (SQLException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return status(p);
    }

    public static boolean unlocked(Player p) {
        PlusGUI config = new PlusGUI();
        for (String colors : config.getConfigurationSection("PlusGUI.Colors")) {
            if (p.getLevel() >= config.getInt("PlusGUI.Colors." + colors + ".Level")) {
                return true;
            } else {
                return false;
            }
        }
        return unlocked(p);
    }
}
