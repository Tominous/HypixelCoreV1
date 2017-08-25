package me.ikeetjeop.hypixel.listener.chat;

import me.ikeetjeop.hypixel.HypixelCore;
import me.ikeetjeop.hypixel.mysql.mysql.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Created by Gebruiker on 23/08/2017.
 */
public class ChatListener implements Listener {

    private HypixelCore plugin = HypixelCore.getInstance();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player p = event.getPlayer();
        try {
            MySQL mySQL = new MySQL(plugin.sqlHost, plugin.sqlPort, plugin.sqlDb, plugin.sqlUser, plugin.sqlPw);
            Statement statement = mySQL.openConnection().createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM Players WHERE UUID='" + p.getUniqueId() + "'");
            if (res.next()) {
                try {
                    event.setFormat(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Hypixel.Settings.chat." + plugin.getPermission().getPrimaryGroup(p))
                            .replace("%rank%", plugin.chat.getGroupPrefix(p.getWorld(), plugin.getInstance().permission.getPrimaryGroup(p)))
                            .replace("%player%", p.getName()).replace("{plus}", res.getString("Pluse"))) + event.getMessage());
                } catch (NullPointerException EX) {

                    event.setFormat(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Hypixel.Settings.chat.Other")
                            .replace("%rank%", plugin.chat.getGroupPrefix(p.getWorld(), plugin.getInstance().permission.getPrimaryGroup(p)))
                            .replace("%player%", p.getName()).replace("{plus}", res.getString("Pluse"))) + event.getMessage());
                }

                if (p.hasPermission("Hypixel.ChatColor") || p.hasPermission("Hypixel.Admin")) {
                    if (event.getMessage().contains("&")) {
                        event.setMessage(ChatColor.translateAlternateColorCodes('&', event.getMessage()));
                    }
                }

            }
        } catch (SQLException | ClassNotFoundException ex) {
            Bukkit.getConsoleSender().sendMessage("AAQAAA");
        }
    }
}
