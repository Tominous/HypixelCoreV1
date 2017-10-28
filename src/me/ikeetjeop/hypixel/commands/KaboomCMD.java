package me.ikeetjeop.hypixel.commands;

import com.google.common.cache.Cache;
import me.ikeetjeop.hypixel.utilities.configmanagement.MessagesYML;
import net.milkbowl.vault.chat.Chat;
import net.minecraft.server.v1_8_R3.DedicatedServer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Properties;

/**
 * Created by @Ikeetjeop aka Nick on 9/09/2017.
 * If this project not uploaded on github you're not allowed to
 * use this code for your project!
 */
public class KaboomCMD implements CommandExecutor{

    MessagesYML config = new MessagesYML();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("kaboom")) {
            if (sender.hasPermission("Hypixel.kaboom")) {
                for (Player all : Bukkit.getOnlinePlayers()) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.Commands.Kaboom.Self").replace("%player%", all.getName())));
                    for (int i = 1; i < 50; i++) {
                        all.playEffect(all.getLocation(), Effect.CLOUD, 999999999);
                    }
                    all.setVelocity(all.getLocation().getDirection().multiply(0).setY(4));
                    all.setVelocity(all.getLocation().getDirection().multiply(0).setY(4));
                    all.getWorld().strikeLightningEffect(all.getLocation());
                }
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.Global.AdminPerms")));
            }
        }
        return false;
    }
}
