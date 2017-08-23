package me.ikeetjeop.hypixel.listener.connect;

import me.ikeetjeop.hypixel.HypixelCore;
import me.ikeetjeop.hypixel.mysql.mysql.MySQL;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Ikeeetjeop on 21/08/2017 at 12:37.
 */
public class JoinListener implements Listener{
    HypixelCore plugin = HypixelCore.getInstance();

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        try {
            MySQL MySQL = new MySQL(plugin.sqlHost, plugin.sqlPort, plugin.sqlDb, plugin.sqlUser, plugin.sqlPw);
            Statement statement = MySQL.openConnection().createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM Players WHERE UUID = '" + p.getUniqueId() + "';");
            //Create player tabel + set online + update name
            if(res.next()) {
                    statement.executeUpdate("UPDATE Players SET IsOnline=1 WHERE UUID = '" + p.getUniqueId() + "'");
                    statement.executeUpdate("UPDATE Players SET Name='"+ p.getName() +"' WHERE UUID = '" + p.getUniqueId() + "'");
            } else {
                    statement.executeUpdate("INSERT INTO Players (`UUID`, `Name`, `Rank`, `Coins`, `MysteryDust`, `Exp`, `Pluse`, `JoinAlert`, `IsOnline`)" +
                            " VALUES('" + p.getUniqueId() + "', '" + p.getName() + "', 'Default', '0', '0', '0', '&c+', '0', '1');");
            }
            //Check player join with JoinAlert

            if(res.getInt("JoinAlert") == 1) {
                e.setJoinMessage("");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}
