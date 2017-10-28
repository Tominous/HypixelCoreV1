package me.ikeetjeop.hypixel.utilities.Guis;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.ikeetjeop.hypixel.HypixelCore;
import me.ikeetjeop.hypixel.mysql.mysql.MySQL;
import net.milkbowl.vault.chat.Chat;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


/**
 * Created by @Ikeetjeop aka Nick on 26/08/2017.
 * If this project not uploaded on github you're not allowed to
 * use this code for your project!
 */
public class ProfileGui {
    HypixelCore plugin = HypixelCore.getInstance();

    public static ItemStack skullUrlBuilder(String Dname, int amount, List<String> lore, String url) {
        ItemStack head = new ItemStack(Material.SKULL_ITEM, amount, (short)3);
        if(url.isEmpty())return head;


        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        headMeta.setDisplayName(ChatColor.GREEN + Dname);
        headMeta.setLore(lore);
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField = null;
        try {
            profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e1) {
            e1.printStackTrace();
        }
        head.setItemMeta(headMeta);
        return head;
    }

    public static ItemStack itemBuilder(Material material, int amount, short type, String name, List<String> lore){
        ItemStack item = new ItemStack(material, amount, type);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GREEN + name);
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }
    public static ItemStack skullNameBuilder(String Dname, String OwnerSkull, int amount, List<String> lore){
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, amount, (short) 3);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setDisplayName(ChatColor.GREEN + Dname);
        skullMeta.setLore(lore);
        skullMeta.setOwner(OwnerSkull);
        skull.setItemMeta(skullMeta);
        return skull;
    }

    public void inv(HypixelCore plugin, Player p) {
        Inventory inv = plugin.getServer().createInventory(null, 9 * 6, "My Profile");
        MySQL mySQL = new MySQL(plugin.sqlHost, plugin.sqlPort, plugin.sqlDb, plugin.sqlUser, plugin.sqlPw);
        try {
            Statement statement = mySQL.openConnection().createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM Players WHERE UUID='" + p.getUniqueId() + "'");
            if (res.next()) {
                inv.setItem(2, skullNameBuilder(ChatColor.translateAlternateColorCodes('&', plugin.getChat().getPlayerPrefix(p).replace("{plus}", res.getString("Pluse")) + " " + p.getName()), p.getName(),
                        1, Arrays.asList(
                                ChatColor.GRAY + "Hypixel Level: " + ChatColor.GOLD + res.getInt("Level"),
                                ChatColor.GRAY + "",
                                ChatColor.GRAY + "Guild: " + ChatColor.AQUA + "None",
                                ChatColor.GRAY + "Online Status: " + ChatColor.AQUA + res.getString("Online").replace("1", "Online"),
                                ChatColor.GRAY + "Server: " + ChatColor.AQUA + res.getString("Server"))));
                inv.setItem(3, skullUrlBuilder("Friends", 1, Arrays.asList(ChatColor.GRAY + "View your Hypixel friend's",ChatColor.GRAY + "profiles, and interact with", ChatColor.GRAY + "your online friends!"), "http://textures.minecraft.net/texture/e063eedb2184354bd43a19deffba51b53dd6b7222f8388caa239cabcdce84"));
                inv.setItem(4, skullUrlBuilder("Party", 1, Arrays.asList(ChatColor.GRAY + "Create a party and join up",ChatColor.GRAY + "with other players to play", ChatColor.GRAY + "games together!"), "http://textures.minecraft.net/texture/667963ca1ffdc24a10b397ff8161d0da82d6a3f4788d5f67f1a9f9bfbc1eb1"));
                inv.setItem(5, skullUrlBuilder("Guild", 1, Arrays.asList(ChatColor.GRAY + "Form a guild with other",ChatColor.GRAY + "Hypixel players to conquer", ChatColor.GRAY + "game modes and work towards", ChatColor.GRAY + "common Hypixel rewards."), "http://textures.minecraft.net/texture/fe8b59f8cce510809427c3843cf575fae8fe6a8b7d1560dd46958d148563815"));
                inv.setItem(6, skullUrlBuilder("Recent Players", 1, Arrays.asList(ChatColor.GRAY + "View players you have",ChatColor.GRAY + "played recent games with."), "http://textures.minecraft.net/texture/9993a356809532d696841a37a0549b81b159b79a7b2919cff4e5abdfea83d66"));
                for (int i=9; i<18; i++){
                   inv.setItem(i, itemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 1, "", null));
                }
                inv.setItem(21, skullUrlBuilder("Social Media", 1, Arrays.asList(ChatColor.GRAY + "Click to edit your Social", ChatColor.GRAY + "Media links."), "http://textures.minecraft.net/texture/3685a0be743e9067de95cd8c6d1ba21ab21d37371b3d597211bb75e43279"));
                inv.setItem(22, skullNameBuilder("Character Information", p.getName(),
                        1, Arrays.asList(
                                ChatColor.GRAY + "Rank: " + ChatColor.translateAlternateColorCodes('&', plugin.getChat().getPrimaryGroup(p).replace("{plus}", res.getString("Pluse"))),
                                ChatColor.GRAY + "Level: " + ChatColor.GOLD + res.getInt("Level"),
                                ChatColor.GRAY + "Experience until next Level: " + ChatColor.GOLD + res.getInt("Level"),
                                ChatColor.GRAY + "Achievement Points: " + ChatColor.GOLD + res.getInt("Level"),
                                ChatColor.GRAY + "Mystery Dust: " + ChatColor.AQUA + res.getInt("MysteryDust"),
                                ChatColor.GRAY + "Quests Completed: " + ChatColor.GOLD + res.getInt("Level"),
                                ChatColor.GRAY + "Karma: " + ChatColor.GOLD + res.getInt("Level")
                                )));
                inv.setItem(23, itemBuilder(Material.PAPER, 1, (short) 0, "Stats Viewer", Arrays.asList(
                        ChatColor.GRAY + "Showcases your stats for each",
                        ChatColor.GRAY + "game and an overview of all.",
                        ChatColor.GRAY + "",
                        ChatColor.GRAY + "Players ranked "+ ChatColor.AQUA +" MVP "+ ChatColor.GRAY +" or higher",
                        ChatColor.GRAY + "can use "+ ChatColor.WHITE +"/stats (username) "+ ChatColor.GRAY +"to view",
                        ChatColor.GRAY + "other player's stats.",
                        ChatColor.GRAY + "",
                        ChatColor.YELLOW + "Click to view your stats!")));
                inv.setItem(29, itemBuilder(Material.INK_SACK, 1, (short) 0, "MVP+ Rankcolor", Arrays.asList(
                        ChatColor.GRAY + "Players ranked " + ChatColor.AQUA + "MVP" + ChatColor.RED + "+",
                        ChatColor.GRAY + "can unlock and switch the",
                        ChatColor.GRAY + "color of their +.",
                        ChatColor.GRAY + "",
                        ChatColor.YELLOW + "Click to change!")));
                inv.setItem(30, itemBuilder(Material.DIAMOND, 1, (short) 0, "Actievements", Arrays.asList(
                        ChatColor.GRAY + "Track your progress as you unlock",
                        ChatColor.GRAY + "Activements and rank up points",
                        ChatColor.GRAY + "",
                        ChatColor.YELLOW + "Click to view your actievements!")));
                inv.setItem(31, itemBuilder(Material.BREWING_STAND_ITEM, 1, (short) 0, "Hypixel Leveling", Arrays.asList(
                        ChatColor.GRAY + "Playing games and completing quests",
                        ChatColor.GRAY + "will reward you with " + ChatColor.DARK_AQUA + "Hypixel Experience" + ChatColor.GRAY + ",",
                        ChatColor.GRAY + "which is required to level up and",
                        ChatColor.GRAY + "acquire new perks and rewards!",
                        ChatColor.GRAY + "",
                        ChatColor.DARK_AQUA + "Hypixel Level "+ ChatColor.GREEN+ res.getString("Level") +" |||||||||||||||||||||||||||||||||||||||| "+ ChatColor.AQUA +" 0%",
                        ChatColor.GRAY + "",
                        ChatColor.GRAY + "Experience until next level: 0",
                        ChatColor.GRAY + "",
                        ChatColor.YELLOW + "Click to see your rewards!")));
                inv.setItem(32, itemBuilder(Material.ENCHANTED_BOOK, 1, (short) 0, "Questions & Challange", Arrays.asList(
                        ChatColor.GRAY + "Completing quests and challanges",
                        ChatColor.GRAY + "will reward you with " + ChatColor.GOLD + "Coins" + ChatColor.GRAY + "," + ChatColor.DARK_AQUA + "Hypixel",
                        ChatColor.DARK_AQUA + "Experience "+ ChatColor.GRAY +" and more!",
                        ChatColor.GRAY + "",
                        ChatColor.GRAY + "You can complete a maximum of " + ChatColor.GREEN + "0",
                        ChatColor.GRAY + "challanges every day.",
                        ChatColor.GRAY + "",
                        ChatColor.GRAY + "Challanges completed today: " + ChatColor.GREEN + "0",
                        ChatColor.GRAY + "",
                        ChatColor.YELLOW + "Click to view Quests & Challenges")));
                inv.setItem(33, itemBuilder(Material.REDSTONE_COMPARATOR, 1, (short) 0, "Settings & Visibility", Arrays.asList(
                        ChatColor.GRAY + "Allows you to edit and control",
                        ChatColor.GRAY + "various personal settings.",
                        ChatColor.GRAY + "",
                        ChatColor.YELLOW + "Click to edit your settings!")));
                inv.setItem(39, itemBuilder(Material.DARK_OAK_DOOR_ITEM, 1, (short) 0, "Go to Housing", null));
                inv.setItem(40, itemBuilder(Material.POTION, 1, (short) 0, "Coin Boosters", Arrays.asList(
                        ChatColor.GRAY + "Activate your personal and network",
                        ChatColor.GRAY + "Boosters for extra coins",
                        ChatColor.GRAY + "",
                        ChatColor.YELLOW + "Click to active boosters!")));
                inv.setItem(41, skullUrlBuilder("Select Language", 1, Arrays.asList(
                        ChatColor.GRAY + "Change your language.",
                        ChatColor.GRAY + "",
                        ChatColor.GRAY + "Currently available:",
                        ChatColor.GRAY + "   ∙ "+ChatColor.WHITE+ "English",
                        ChatColor.GRAY + "   ∙ "+ChatColor.WHITE+ "Deutsch",
                        ChatColor.GRAY + "   ∙ "+ChatColor.WHITE+ "Français",
                        ChatColor.GRAY + "   ∙ "+ChatColor.WHITE+ "Nederlands",
                        ChatColor.GRAY + "   ∙ "+ChatColor.WHITE+ "Español",
                        ChatColor.GRAY + "   ∙ "+ChatColor.WHITE+ "简体中文",
                        ChatColor.GRAY + "   ∙ "+ChatColor.WHITE+ "Русский",
                        ChatColor.GRAY + "",
                        ChatColor.GRAY + "More languages coming soon!",
                        ChatColor.GRAY + "",
                        ChatColor.YELLOW + "Click to change your language!"),
                        "http://textures.minecraft.net/texture/98daa1e3ed94ff3e33e1d4c6e43f024c47d78a57ba4d38e75e7c9264106"));
                p.openInventory(inv);
            }
        } catch (SQLException | ClassNotFoundException ex) {
        }
    }
}
