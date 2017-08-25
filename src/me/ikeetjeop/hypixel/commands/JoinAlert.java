package me.ikeetjeop.hypixel.commands;

import me.ikeetjeop.hypixel.HypixelCore;
import me.ikeetjeop.hypixel.mysql.mysql.MySQL;
import me.ikeetjeop.hypixel.utilities.configmanagement.MessagesYML;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Ikeeetjeop on 22/08/2017 at 14:46.
 */
public class JoinAlert implements CommandExecutor{

    HypixelCore plugin = HypixelCore.getInstance();
    MessagesYML message = new MessagesYML(plugin);

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if(cmd.getName().equalsIgnoreCase("JoinAlert")){
            if(sender instanceof Player) {
                try {
                    MySQL mySQL = new MySQL(plugin.sqlHost, plugin.sqlPort, plugin.sqlDb, plugin.sqlUser, plugin.sqlPw);
                    Statement statement = mySQL.openConnection().createStatement();
                    Player p = (Player) sender;
                    if (p.hasPermission("Hypixel.joinalert")) {
                        if(args.length == 0) {
                            ResultSet resPlayer = statement.executeQuery("SELECT * FROM Players WHERE UUID='" + p.getUniqueId() + "'");
                            if (resPlayer.next()) {
                                if (resPlayer.getInt("JoinAlert") == 0) {
                                    statement.executeUpdate("UPDATE Players SET JoinAlert=1 WHERE UUID='" + p.getUniqueId() + "'");
                                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', message.getString("Messages.Commands.JoinAlert.Self.Enabled")));
                                } else {
                                    statement.executeUpdate("UPDATE Players SET JoinAlert=0 WHERE UUID='" + p.getUniqueId() + "'");
                                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', message.getString("Messages.Commands.JoinAlert.Self.Disable")));
                                }
                            }
                        } else if(args.length == 2){
                            ResultSet resPlayer = statement.executeQuery("SELECT * FROM Players WHERE UUID='" + p.getUniqueId() + "'");

                        }
                        }
                    }catch(SQLException | ClassNotFoundException ex){

                    }

            }
        }
        return false;
    }
}
