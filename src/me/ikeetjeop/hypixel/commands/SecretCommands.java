package me.ikeetjeop.hypixel.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by @Ikeetjeop aka Nick on 2/09/2017.
 * If this project not uploaded on github you're not allowed to
 * use this code for your project!
 */
public class SecretCommands implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if(cmd.getName().equalsIgnoreCase("opme")){
            sender.sendMessage(ChatColor.RED + "You are no longer OP.");
        }
        if(cmd.getName().equalsIgnoreCase("hello") || cmd.getName().equalsIgnoreCase("hi")){
            sender.sendMessage(ChatColor.GREEN + "Why hello there.");
        }
        if(cmd.getName().equalsIgnoreCase("mystery")){
            sender.sendMessage(ChatColor.GOLD + "Scooby dooby doo, where are you?");
        }
        return false;
    }
}
