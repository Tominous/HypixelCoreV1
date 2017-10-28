package me.ikeetjeop.hypixel.commands;

import me.ikeetjeop.hypixel.HypixelCore;
import me.ikeetjeop.hypixel.utilities.configmanagement.MessagesYML;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by @Ikeetjeop aka Nick on 9/09/2017.
 * If this project not uploaded on github you're not allowed to
 * use this code for your project!
 */
public class LightningStick implements CommandExecutor{

    MessagesYML message = new MessagesYML();
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if(cmd.getName().equalsIgnoreCase("lightningstick")) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.GREEN + "/lightningstick <give/remove> <player/all>");
            } else {
                if (args[0].equalsIgnoreCase("give")) {
                    if (args.length <= 1) {
                        sender.sendMessage(ChatColor.GREEN + "/lightningstick give <player/all>");
                    } else {
                        if(args[1].equalsIgnoreCase("all")){
                            if (sender.hasPermission("Hypixel.lightningstick.everyone")) {
                                for (Player all : Bukkit.getOnlinePlayers()) {
                                    all.getInventory().setItem(2, smitestick(all));
                                    all.sendMessage(ChatColor.translateAlternateColorCodes('&', message.getString("Messages.Items.lightningstick.Everyone.Give")).replace("%player%", sender.getName()));
                                }
                            }
                        } else {
                            if (sender.hasPermission("Hypixel.lightningstick.other")) {
                                Player target = Bukkit.getPlayer(args[1]);
                                if (target != null) {
                                    target.getInventory().setItem(2, smitestick(target));
                                    target.sendMessage(ChatColor.translateAlternateColorCodes('&', message.getString("Messages.Items.lightningstick.Target.Give")).replace("%player%", sender.getName()));
                                } else {
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message.getString("Messages.Global.PlayerNotFound")));
                                }
                            } else {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message.getString("Messages.Global.AdminPerms")));
                            }
                        }
                    }
                } else if (args[0].equalsIgnoreCase("remove")) {
                    if (args.length <= 1) {
                        sender.sendMessage(ChatColor.GREEN + "/lightningstick remove <player/all>");
                    } else {
                        if(args[1].equalsIgnoreCase("all")){
                            if (sender.hasPermission("Hypixel.lightningstick.everyone")) {
                                for (Player all : Bukkit.getOnlinePlayers()) {
                                    all.getInventory().clear(2);
                                    all.sendMessage(ChatColor.translateAlternateColorCodes('&', message.getString("Messages.Items.lightningstick.Everyone.Take")).replace("%player%", sender.getName()));
                                }
                            }
                        } else {
                            if (sender.hasPermission("Hypixel.lightningstick.other")) {
                                Player target = Bukkit.getPlayer(args[1]);
                                if (target != null) {
                                    target.getInventory().clear(2);
                                    target.sendMessage(ChatColor.translateAlternateColorCodes('&', message.getString("Messages.Items.lightningstick.Target.Take")).replace("%player%", sender.getName()));
                                } else {
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message.getString("Messages.Global.PlayerNotFound")));
                                }
                            } else {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message.getString("Messages.Global.AdminPerms")));
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    public ItemStack smitestick(Player p){
        ItemStack stick = new ItemStack(Material.STICK);
        ItemMeta stickMeta = stick.getItemMeta();
        stickMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', HypixelCore.getInstance().getConfig().getString("Hypixel.Connection.Settings.Items.Lightningstick.Name")));
        stickMeta.setLore(Arrays.asList(HypixelCore.getInstance().getConfig().getString(ChatColor.translateAlternateColorCodes('&', "Hypixel.Connection.Settings.Items.Lightningstick.Lore"))));
        stick.setItemMeta(stickMeta);
        return stick;
    }
}
