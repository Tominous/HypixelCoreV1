package me.ikeetjeop.hypixel.listener.connect;

import me.ikeetjeop.hypixel.HypixelCore;
import me.ikeetjeop.hypixel.listener.clickactions.ClickEventListener;
import me.ikeetjeop.hypixel.mysql.mysql.MySQL;
import me.ikeetjeop.hypixel.utilities.configmanagement.MessagesYML;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

/**
 * Created by Ikeeetjeop on 21/08/2017 at 12:37.
 */
public class JoinListener implements Listener {
    HypixelCore plugin = HypixelCore.getInstance();
    MessagesYML message;

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        this.message = new MessagesYML();
        e.setJoinMessage(null);
        playerTag(p);
        setForPlayer(p, ChatColor.translateAlternateColorCodes('&', message.getString("Messages.TabList.Header")), ChatColor.translateAlternateColorCodes('&', message.getString("Messages.TabList.Footer")));
        try {
            MySQL MySQL = new MySQL(plugin.sqlHost, plugin.sqlPort, plugin.sqlDb, plugin.sqlUser, plugin.sqlPw);
            Statement statement = MySQL.openConnection().createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM Players WHERE UUID = '" + p.getUniqueId() + "'");
            //Create player tabel + set online + update name
            if (res.next()) {
                //When playerJoin + mysql is connected he give items
                //Set EXP
                //And much more
                joinItems(p, res.getInt("Coins"), res.getInt("MysteryDust"));
                p.setLevel(res.getInt("Level"));
                p.setExp(res.getInt("Exp"));
                p.setPlayerListName(ChatColor.translateAlternateColorCodes('&', plugin.getChat().getPlayerPrefix(p.getPlayer()).replace("{plus}", res.getString("Pluse")) + " " + p.getName()));
                p.setCustomName(ChatColor.translateAlternateColorCodes('&', plugin.getChat().getPlayerPrefix(p.getPlayer()).replace("{plus}", res.getString("Pluse")) + " " + p.getName()));
                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999999, 0, false, false), true);
                if (!ClickEventListener.hidden.contains(p)) {
                    ClickEventListener.hidden.add(p);
                }
                //Check is JoinAlert 1(True)
                if (res.getInt("JoinAlert") == 1) {
                    //SetJoinMessage + check is default rank why: https://goo.gl/YW41KY
                    e.setJoinMessage(ChatColor.translateAlternateColorCodes('&', message.getString("Messages.Connect.JoinMessage")
                            .replace("%rank%", plugin.getChat().getPlayerPrefix(p.getPlayer())).replace("%player%", p.getName()).replace("{plus}", res.getString("Pluse"))));
                } else {
                    e.setJoinMessage(null);
                }
                //Set player online in mysql
                statement.executeUpdate("UPDATE Players SET Online=1 WHERE UUID='" + p.getUniqueId() + "'");
                statement.executeUpdate("UPDATE Players SET Server='" + plugin.getConfig().getString("Hypixel.Settings.ServerName") + "' WHERE UUID='" + p.getUniqueId() + "'");

            } else {
                statement.executeUpdate("INSERT INTO Players (`UUID`, `Coins`, `MysteryDust`, `Exp`, `Level`, `Pluse`, `JoinAlert`, `Online`, `Server`)" +
                        " VALUES('" + p.getUniqueId() + "'," +
                        " '" + plugin.getConfig().getInt("Hypixel.Settings.Default.Coins") + "'," +
                        " '" + plugin.getConfig().getInt("Hypixel.Settings.Default.MysteryDust") + "'," +
                        " '" + plugin.getConfig().getDouble("Hypixel.Settings.Default.EXP") + "'," +
                        " '" + plugin.getConfig().getInt("Hypixel.Settings.Default.Level") + "'," +
                        " '" + plugin.getConfig().getString("Hypixel.Settings.Default.Plus") + "'," +
                        " '" + plugin.getConfig().getInt("Hypixel.Settings.Default.JoinAlert") + "'," +
                        " '" + 1 + "'," +
                        " '" + plugin.getConfig().getString("Hypixel.Settings.ServerName") + "');");
                joinItems(p, plugin.getConfig().getInt("Hypixel.Settings.Default.Coins"), plugin.getConfig().getInt("Hypixel.Settings.Default.MysteryDust"));
            }
            //Check player join with JoinAlert
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }

    }

    public void joinItems(Player p, int hpcoins, int mysterydust) {
        ItemStack compass = new ItemStack(Material.COMPASS);
        ItemMeta compassMeta = compass.getItemMeta();
        compassMeta.setDisplayName(ChatColor.GREEN + "Game Menu" + ChatColor.GRAY + " (Right click)");
        compassMeta.setLore(Arrays.asList(ChatColor.GRAY + "Right Click to bring up the Game Menu!"));
        compass.setItemMeta(compassMeta);

        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setDisplayName(ChatColor.GREEN + "My Profile" + ChatColor.GRAY + " (Right click)");
        skullMeta.setLore(Arrays.asList(ChatColor.GRAY + "Right-click to browser quests, view activements,", ChatColor.GRAY + "activate Network Boosters and more!"));
        skullMeta.setOwner(p.getName());
        skull.setItemMeta(skullMeta);

        ItemStack chest = new ItemStack(Material.CHEST);
        ItemMeta chestMeta = chest.getItemMeta();
        chestMeta.setDisplayName(ChatColor.GREEN + "Collectibles" + ChatColor.GRAY + " (Right click)");
        chestMeta.setLore(Arrays.asList(ChatColor.GRAY + "Mystery Dust:" + ChatColor.AQUA + " {MysteryDust}".replace("{MysteryDust}", mysterydust + ""),
                ChatColor.GRAY + "Hypixel Credits:" + ChatColor.AQUA + " {HPixelCredits}".replace("{HPixelCredits}", hpcoins + ""),
                ChatColor.GRAY + "",
                ChatColor.GRAY + "Collect fun cosmetic items!",
                ChatColor.GRAY + "Get new items by opening",
                ChatColor.AQUA + "Mystery Boxes" + ChatColor.GRAY + " or hitting milestone rewards.",
                ChatColor.GRAY + "Some can also be crafted using",
                ChatColor.AQUA + "Mystery Dust" + ChatColor.GRAY + ".",
                ChatColor.AQUA + "",
                ChatColor.AQUA + "Mystery Dust" + ChatColor.GRAY + " can be earned by",
                ChatColor.GRAY + "opening" + ChatColor.AQUA + " Mystery boxes" + ChatColor.GRAY + ".",
                ChatColor.AQUA + "",
                ChatColor.GRAY + "You can support the Hypixel server by",
                ChatColor.GRAY + "buying" + ChatColor.AQUA + " Mystery Boxes" + ChatColor.GRAY + " on our store:",
                ChatColor.GRAY + "",
                ChatColor.YELLOW + "http://store.hypixel.net"));
        chest.setItemMeta(chestMeta);

        ItemStack vanish = new ItemStack(Material.INK_SACK, 1, (byte) 10);
        ItemMeta vanishMeta = vanish.getItemMeta();
        vanishMeta.setDisplayName(ChatColor.WHITE + "Players: " + ChatColor.GREEN + "Visible" + ChatColor.GRAY + " (Right click)");
        vanishMeta.setLore(Arrays.asList(ChatColor.GRAY + "Right-click to toggle player visibility"));
        vanish.setItemMeta(vanishMeta);

        ItemStack lobby = new ItemStack(Material.NETHER_STAR);
        ItemMeta lobbyMeta = lobby.getItemMeta();
        lobbyMeta.setDisplayName(ChatColor.GREEN + "Lobby Selector" + ChatColor.GRAY + " (Right click)");
        lobbyMeta.setLore(Arrays.asList(ChatColor.GRAY + "Right-click to switch between different lobbies!", ChatColor.GRAY + "Us this to stay with your friends."));
        lobby.setItemMeta(lobbyMeta);

        PlayerInventory pInv = p.getInventory();
        pInv.clear();
        pInv.setItem(0, compass);
        pInv.setItem(1, skull);
        //pInv.setItem(4, chest);
        pInv.setItem(7, vanish);
        pInv.setItem(8, lobby);

    }
    ;
    public void playerTag(Player p) {
        try {
            MySQL MySQL = new MySQL(plugin.sqlHost, plugin.sqlPort, plugin.sqlDb, plugin.sqlUser, plugin.sqlPw);
            Statement statement = MySQL.openConnection().createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM Players WHERE UUID = '" + p.getUniqueId() + "';");
            //Create player tabel + set online + update name
            if (res.next()) {
                ScoreboardManager manager = Bukkit.getScoreboardManager();
                Scoreboard board = manager.getNewScoreboard();
                for (Player all : Bukkit.getOnlinePlayers()) {
                    board.registerNewTeam(plugin.getPermission().getPrimaryGroup(all));
                }
                for (Player all : Bukkit.getOnlinePlayers()) {
                        board.getTeam(plugin.getPermission().getPrimaryGroup(all)).setPrefix(ChatColor.translateAlternateColorCodes('&', plugin.getChat().getPlayerPrefix(all).replace("{plus}", res.getString("Pluse")) + " "));
                        board.getTeam(plugin.getPermission().getPrimaryGroup(all)).addPlayer(all);
                        all.setScoreboard(board);
                }
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    //From https://bukkit.org/threads/footer-header.458933/
    public static void setForPlayer(Player p, String header, String footer){

        CraftPlayer craftplayer = (CraftPlayer)p;
        PlayerConnection connection = craftplayer.getHandle().playerConnection;
        IChatBaseComponent top = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + header + "\"}");
        IChatBaseComponent bottom = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + footer + "\"}");

        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
        try
        {
            Field headerField = packet.getClass().getDeclaredField("a");
            headerField.setAccessible(true);
            headerField.set(packet, top);
            headerField.setAccessible(!headerField.isAccessible());

            Field footerField = packet.getClass().getDeclaredField("b");
            footerField.setAccessible(true);
            footerField.set(packet, bottom);
            footerField.setAccessible(!footerField.isAccessible());
        } catch (Exception ev) {
            ev.printStackTrace();
        }

        connection.sendPacket(packet);
    }
}
