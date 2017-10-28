package me.ikeetjeop.hypixel.listener.clickactions;

import me.ikeetjeop.hypixel.HypixelCore;
import me.ikeetjeop.hypixel.commands.WalkCmd;
import me.ikeetjeop.hypixel.utilities.Guis.ProfileGui;
import me.ikeetjeop.hypixel.utilities.configmanagement.MessagesYML;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.*;
import org.bukkit.util.Vector;

import java.util.*;

/**
 * Created by @Ikeetjeop aka Nick on 25/08/2017.
 * If this project not uploaded on github you're not allowed to
 * use this code for your project!
 */
public class ClickEventListener implements Listener {
    public static ArrayList<Player> hidden = new ArrayList<>();

    HypixelCore plugin = HypixelCore.getInstance();

    public ItemStack dyeBuilder(String name, byte type) {
        ItemStack vanish = new ItemStack(Material.INK_SACK, 1, type);
        ItemMeta vanishMeta = vanish.getItemMeta();
        vanishMeta.setDisplayName(ChatColor.WHITE + "Players: " + name + ChatColor.GRAY + " (Right click)");
        vanishMeta.setLore(Arrays.asList(ChatColor.GRAY + "Right-click to toggle player visibility"));
        vanish.setItemMeta(vanishMeta);
        return vanish;
    }

    MessagesYML message = new MessagesYML();

    @EventHandler
    public void playerClickListener(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        try {
            if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR) || (e.getAction().equals(Action.LEFT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_AIR))) {
                if (p.getInventory().getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Players: " + ChatColor.GREEN + "Visible" + ChatColor.GRAY + " (Right click)")) {
                /*
                TODO: Invitibility OFF
                 */
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', message.getString("Messages.Items.Visibility.Action.Disabled")));
                    p.getInventory().remove(p.getItemInHand());
                    p.getInventory().setItem(p.getInventory().getHeldItemSlot(), dyeBuilder(ChatColor.RED + "Hidden", (byte) 8));
                } else if (p.getInventory().getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Players: " + ChatColor.RED + "Hidden" + ChatColor.GRAY + " (Right click)")) {
                /*
                TODO: Invitibility ON
                 */
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', message.getString("Messages.Items.Visibility.Action.Enabled")));
                    p.getInventory().remove(p.getItemInHand());
                    p.getInventory().setItem(p.getInventory().getHeldItemSlot(), dyeBuilder(ChatColor.GREEN + "Visible", (byte) 10));

                } else if (p.getInventory().getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "My Profile" + ChatColor.GRAY + " (Right click)")) {
                    ProfileGui inv = new ProfileGui();
                    inv.inv(plugin, p);
                }else if(p.getInventory().getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&',HypixelCore.getInstance().getConfig().getString("Hypixel.Connection.Settings.Items.Lightningstick.Name"))) && p.getInventory().getItemInHand().getItemMeta().getLore().equals(Arrays.asList(HypixelCore.getInstance().getConfig().getString(ChatColor.translateAlternateColorCodes('&',"Hypixel.Connection.Settings.Items.Lightningstick.Lore"))))){
                    p.getWorld().strikeLightningEffect(p.getTargetBlock((Set<Material>) null, 300).getLocation());
                }
            }
        } catch (NullPointerException ex) {
        }
    }
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if(WalkCmd.walker.contains(e.getPlayer().getUniqueId())) {
            for (int i = 1; i < 10; i++) {
                for (Player p : Bukkit.getOnlinePlayers()){
                    p.playEffect(new Location(e.getPlayer().getWorld(), e.getPlayer().getLocation().getX(), e.getPlayer().getLocation().getY() + 0.1, e.getPlayer().getLocation().getZ() + 0.5), Effect.FOOTSTEP, 999999999);
                    p.playEffect(new Location(e.getPlayer().getWorld(), e.getPlayer().getLocation().getX() + 0.5, e.getPlayer().getLocation().getY() + 0.1, e.getPlayer().getLocation().getZ()), Effect.FOOTSTEP, 999999999);
                }
            }
        }
        if (e.getTo().getBlock().getType() == Material.STONE_PLATE) {
            e.getPlayer().setVelocity(e.getPlayer().getLocation().getDirection().multiply(3));
            e.getPlayer().setVelocity(new Vector(e.getPlayer().getVelocity().getX(), 0.50D, e.getPlayer().getVelocity().getZ()));
        }
    }
}