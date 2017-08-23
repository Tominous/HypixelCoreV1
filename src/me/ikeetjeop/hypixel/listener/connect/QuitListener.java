package me.ikeetjeop.hypixel.listener.connect;

import me.ikeetjeop.hypixel.HypixelCore;
import me.ikeetjeop.hypixel.mysql.mysql.MySQL;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Ikeeetjeop on 21/08/2017 at 12:37.
 */
public class QuitListener implements Listener{
    HypixelCore plugin = HypixelCore.getInstance();


    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        try {
            MySQL MySQL = new MySQL(plugin.sqlHost, plugin.sqlPort, plugin.sqlDb, plugin.sqlUser, plugin.sqlPw);
            Statement statement = MySQL.openConnection().createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM Players WHERE UUID = '" + p.getUniqueId() + "';");
            if(res.next()) {
                statement.executeUpdate("UPDATE Players SET IsOnline=0 WHERE UUID = '" + p.getUniqueId() + "'");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}
