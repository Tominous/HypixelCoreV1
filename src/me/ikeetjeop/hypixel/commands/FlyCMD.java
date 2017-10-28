package me.ikeetjeop.hypixel.commands;

import me.ikeetjeop.hypixel.utilities.configmanagement.MessagesYML;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by @Ikeetjeop aka Nick on 10/09/2017.
 * If this project not uploaded on github you're not allowed to
 * use this code for your project!
 */
public class FlyCMD implements CommandExecutor {


    MessagesYML config = new MessagesYML();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("fly")) {
            if (args.length == 0) {
                if (sender.hasPermission("Hypixel.fly")) {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        if (!p.getAllowFlight()) {
                            p.setAllowFlight(true);
                            p.setFlying(true);
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.Commands.Fly.Self.Enabled")));
                        } else {
                            p.setAllowFlight(false);
                            p.setFlying(false);
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.Commands.Fly.Self.Disabled")));
                        }
                    }
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.Global.AdminPerms")));
                }
            } else {
                Player target = Bukkit.getPlayer(args[0]);
                if (args[0].equalsIgnoreCase("all")) {
                    if (sender.hasPermission("hypixel.fly.everyone")) {
                        if (args.length == 1) {
                            sender.sendMessage(ChatColor.GREEN + "/fly all <on/off>");
                        } else {
                            //EVERYONE
                            if (args[1].equalsIgnoreCase("on")) {
                                for (Player all : Bukkit.getOnlinePlayers()) {
                                    if (!all.getAllowFlight()) {
                                        all.setAllowFlight(true);
                                        all.setFlying(true);
                                    }
                                }
                                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.Commands.Fly.Everyone.Enabled").replace("%player%", sender.getName())));
                            } else if (args[1].equalsIgnoreCase("off")) {
                                for (Player all : Bukkit.getOnlinePlayers()) {
                                    if (all.getAllowFlight()) {
                                        all.setAllowFlight(false);
                                        all.setFlying(false);
                                    }
                                }
                                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.Commands.Fly.Everyone.Disabled").replace("%player%", sender.getName())));
                            } else {
                                sender.sendMessage(ChatColor.RED + "Subcommand not found");
                            }
                        }
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.Global.AdminPerms")));
                    }
                    //TARGET
                } else if (target != null) {
                    if (sender.hasPermission("Hypixel.fly.other")) {
                        if (!target.getAllowFlight()) {
                            target.setAllowFlight(true);
                            target.setFlying(true);
                            target.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.Commands.Fly.Target.Enabled.Target").replace("%player%", sender.getName())));
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.Commands.Fly.Target.Enabled.Self").replace("%target%", target.getName())));
                        } else {
                            target.setAllowFlight(false);
                            target.setFlying(false);
                            target.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.Commands.Fly.Target.Disabled.Target").replace("%player%", sender.getName())));
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.Commands.Fly.Target.Disabled.Self").replace("%target%", target.getName())));
                        }
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.Global.AdminPerms")));
                    }
                } else {
                    if (sender.hasPermission("Hypixel.fly.other")) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.Global.PlayerNotFound")));
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.Global.AdminPerms")));
                    }
                }
            }
        }
        return false;
    }
}
