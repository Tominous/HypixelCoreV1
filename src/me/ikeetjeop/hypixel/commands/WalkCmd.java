package me.ikeetjeop.hypixel.commands;

import me.ikeetjeop.hypixel.utilities.configmanagement.MessagesYML;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.spigotmc.SpigotConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by @Ikeetjeop aka Nick on 9/09/2017.
 * If this project not uploaded on github you're not allowed to
 * use this code for your project!
 */
public class WalkCmd implements CommandExecutor {

    public static List<UUID> walker = new ArrayList<>();
    MessagesYML config = new MessagesYML();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String command, String[] args) {
        if(cmd.getName().equalsIgnoreCase("walker")){
            if(sender.hasPermission("Hypixel.walker")){
                if(sender instanceof Player) {
                    if(args.length == 0){
                        sender.sendMessage(ChatColor.GREEN + "/walker <Player/all>");
                    } else {
                        if (args[0].equalsIgnoreCase("all")) {
                            if (args.length == 1) {
                                sender.sendMessage(ChatColor.GREEN + "/walker all <on/off>");
                            } else {
                                Player target = Bukkit.getPlayer(args[2]);
                                if (args[2].equalsIgnoreCase("on")) {
                                    for (Player all : Bukkit.getOnlinePlayers()) {
                                        walker.add(all.getUniqueId());
                                    }
                                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.Commands.Walker.Everyone.Enabled").replace("%player%", sender.getName())));
                                } else if (args[2].equalsIgnoreCase("off")) {
                                    for (Player all : Bukkit.getOnlinePlayers()) {
                                        walker.remove(all.getUniqueId());
                                    }
                                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.Commands.Walker.Everyone.Disabled").replace("%player%", sender.getName())));
                                } else {
                                    if (target != null) {
                                        if (!walker.contains(target.getUniqueId())) {
                                            walker.add(target.getUniqueId());
                                            target.sendMessage((ChatColor.translateAlternateColorCodes('&',config.getString("Messages.Commands.Walker.Target.Enabled.Target").replace("%player%", sender.getName()))));
                                            sender.sendMessage((ChatColor.translateAlternateColorCodes('&',config.getString("Messages.Commands.Walker.Target.Enabled.Self").replace("%target%", sender.getName()))));
                                        } else {
                                            walker.remove(target.getUniqueId());
                                            target.sendMessage((ChatColor.translateAlternateColorCodes('&',config.getString("Messages.Commands.Walker.Target.Disabled.Target").replace("%player%", sender.getName()))));
                                            sender.sendMessage((ChatColor.translateAlternateColorCodes('&',config.getString("Messages.Commands.Walker.Target.Disabled.Self").replace("%target%", sender.getName()))));
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Player p = (Player) sender;
                 if(!walker.contains(p.getUniqueId())){
                     p.sendMessage((ChatColor.translateAlternateColorCodes('&',config.getString("Messages.Commands.Walker.Self.Enabled"))));
                     walker.add(p.getUniqueId());
                 }else{
                     p.sendMessage((ChatColor.translateAlternateColorCodes('&', config.getString("Messages.Commands.Walker.Self.Disabled"))));
                     walker.remove(p.getUniqueId());
                 }
                }
            }else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.Global.AdminPerms")));
            }
        }
        return false;
    }
}
