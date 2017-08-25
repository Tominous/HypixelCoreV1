package me.ikeetjeop.hypixel.listener.connect;

import me.ikeetjeop.hypixel.HypixelCore;
import me.ikeetjeop.hypixel.mysql.mysql.MySQL;
import me.ikeetjeop.hypixel.utilities.configmanagement.MessagesYML;
import org.bukkit.ChatColor;
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
    MessagesYML message;

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        this.message = new MessagesYML(HypixelCore.getInstance());
        try {
            MySQL MySQL = new MySQL(plugin.sqlHost, plugin.sqlPort, plugin.sqlDb, plugin.sqlUser, plugin.sqlPw);
            Statement statement = MySQL.openConnection().createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM Players WHERE UUID = '" + p.getUniqueId() + "';");
            //Create player tabel + set online + update name
            if(res.next()) {
                //Check is JoinAlert 1(True)
                if(res.getInt("JoinAlert") == 1) {
                    //SetJoinMessage + check is default rank why: https://goo.gl/YW41KY
                    e.setJoinMessage(ChatColor.translateAlternateColorCodes('&', message.getString("Messages.connect.JoinMessage")
                            .replace("%rank%", plugin.chat.getGroupPrefix(p.getWorld(), plugin.getInstance().permission.getPrimaryGroup(p))).replace("%player%", p.getName())));
                }else{
                    e.setJoinMessage(null);
                }
                //Set player online in mysql
                    statement.executeUpdate("UPDATE Players SET Online=1 WHERE UUID = '" + p.getUniqueId() + "'");

            } else {
                    statement.executeUpdate("INSERT INTO Players (`UUID`, `Coins`, `MysteryDust`, `Exp`, `Pluse`, `JoinAlert`, `Online`)" +
                            " VALUES('" + p.getUniqueId() + "'," +
                            " '"+ plugin.getConfig().getInt("Hypixel.Settings.Default.Coins") +"'," +
                            " '"+ plugin.getConfig().getInt("Hypixel.Settings.Default.MysteryDust") +"'," +
                            " '"+ plugin.getConfig().getDouble("Hypixel.Settings.Default.EXP") +"'," +
                            " '"+ plugin.getConfig().getString("Hypixel.Settings.Default.Plus") +"'," +
                            " '"+ plugin.getConfig().getInt("Hypixel.Settings.Default.JoinAlert") +"'," +
                            " '1');");
            }
            //Check player join with JoinAlert
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}
