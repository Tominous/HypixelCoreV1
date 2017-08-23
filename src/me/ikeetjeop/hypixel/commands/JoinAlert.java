package me.ikeetjeop.hypixel.commands;

import me.ikeetjeop.hypixel.HypixelCore;
import me.ikeetjeop.hypixel.mysql.mysql.MySQL;
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

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if(cmd.getName().equalsIgnoreCase("JoinAlert")){
            if(sender instanceof Player) {
                try{
                Player p = (Player) sender;
                if(p.hasPermission("Hypixel.joinalert")){
                    MySQL mySQL = new MySQL(plugin.sqlHost, plugin.sqlPort, plugin.sqlDb, plugin.sqlUser, plugin.sqlPw);
                    Statement statement = mySQL.openConnection().createStatement();
                    ResultSet res = statement.executeQuery("SELECT * FROM Players WHERE '"+ p.getUniqueId() +"'");

                }
                }catch (SQLException | ClassNotFoundException ex){

                }
            }
        }
        return false;
    }
}
