package me.ikeetjeop.hypixel;

import me.ikeetjeop.hypixel.commands.*;
import me.ikeetjeop.hypixel.listener.clickactions.ClickEventListener;
import me.ikeetjeop.hypixel.listener.chat.ChatListener;
import me.ikeetjeop.hypixel.listener.clickactions.GuiClickListener;
import me.ikeetjeop.hypixel.listener.connect.JoinListener;
import me.ikeetjeop.hypixel.listener.connect.QuitListener;
import me.ikeetjeop.hypixel.mysql.mysql.MySQL;
import me.ikeetjeop.hypixel.utilities.configmanagement.MessagesYML;
import me.ikeetjeop.hypixel.utilities.configmanagement.PlusGUI;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;
import java.util.logging.Level;

/**
 * Created by Ikeetjeop on 19/08/2017 at 12:39.
 */
public class HypixelCore extends JavaPlugin{
    public static HypixelCore instance;
    public static Permission permission = null;

    public static Chat chat = null;
    public static String mvp = getInstance().getConfig().getString("Hypixel.Settings.MvpRankGUI") + getInstance().getConfig().getString("MVP+");

    public HypixelCore(){
        instance = this;
    }

    public static HypixelCore getInstance(){
        return instance;
    }

    public String sqlHost = this.getConfig().getString("Hypixel.Connection.MySQL.Host");
    public String sqlPort = this.getConfig().getString("Hypixel.Connection.MySQL.Port");
    public String sqlDb = this.getConfig().getString("Hypixel.Connection.MySQL.Database");
    public String sqlUser = this.getConfig().getString("Hypixel.Connection.MySQL.User");
    public String sqlPw = this.getConfig().getString("Hypixel.Connection.MySQL.Password");
    MySQL MySQL = new MySQL(sqlHost, sqlPort, sqlDb, sqlUser, sqlPw);
    SQLite sqLite = new SQLite(getDataFolder() + "/Database.db");
    Connection c = null;


    public void onEnable(){
        Bukkit.broadcastMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "The Hypixelcore is back online, enjoy the server!");
        setupChat();
        setupPermissions();
        registerConfig();
        if(!this.getDescription().getAuthors().contains("Ikeetjeop")){
            getLogger().severe("Can i ask you something, wtf are you doing?");
            getLogger().severe("Stop change my author credits!");
            getLogger().severe("So i have disable my plugin, when you change it back and restart your server");
            getLogger().severe("Plugin will be enable automatic. enjoy! -Ikeetjeop");
            getLogger().severe("PS: My favorite pokemon is Zelda on MineCraft");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        try {
            c = MySQL.openConnection();
            Bukkit.getConsoleSender().sendMessage("MySql connected!");
            createDB();
            registerListeners();
            registerCommands();
        }catch (SQLException | ClassNotFoundException e){
            getLogger().log(Level.WARNING, "Error while connect to database! check config.yml > MySQL connection");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
    }

    public void onDisable() {
        try {
            if (!c.isClosed()) {
                MySQL.closeConnection();
            }
        } catch (SQLException | NullPointerException ex) {
            return;
        }
        Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "The Hypixelcore has been disabled, maybe a restart or a reload.");
    }

    private void registerCommands(){
        getCommand("JoinAlert").setExecutor(new JoinAlert());
        getCommand("opme").setExecutor(new SecretCommands());
        getCommand("hello").setExecutor(new SecretCommands());
        getCommand("mystery").setExecutor(new SecretCommands());
        getCommand("level").setExecutor(new LevelCMD());
        getCommand("lightningstick").setExecutor(new LightningStick());
        getCommand("kaboom").setExecutor(new KaboomCMD());
        getCommand("walker").setExecutor(new WalkCmd());
        getCommand("fw").setExecutor(new FireWorkCMD());
        getCommand("fly").setExecutor(new FlyCMD());

    }

    public void registerListeners(){
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new JoinListener(), this);
        pm.registerEvents(new QuitListener(), this);
        pm.registerEvents(new ChatListener(), this);
        pm.registerEvents(new ClickEventListener(), this);
        pm.registerEvents(new GuiClickListener(), this);

    }

    private void registerConfig(){
        MessagesYML message = new MessagesYML();
        PlusGUI plus = new PlusGUI();
        message.setString();
        plus.setString();
        getConfig().options().header("Mysql needed\n");
        //Mysql
        getConfig().addDefault("Hypixel.Connection.MySQL.Host", "localhost");
        getConfig().addDefault("Hypixel.Connection.MySQL.Port", 3306);
        getConfig().addDefault("Hypixel.Connection.MySQL.Database", "hypixelcore");
        getConfig().addDefault("Hypixel.Connection.MySQL.User", "root");
        getConfig().addDefault("Hypixel.Connection.MySQL.Password", "ThisIsABadPassword123!!");
        //Settings
        getConfig().addDefault("Hypixel.Settings.ServerName", "Main Lobby 1");
        getConfig().addDefault("Hypixel.Settings.MvpRankPrefixGUI", "&bMVP{plus}");
        getConfig().addDefault("Hypixel.Settings.FireworkCooldown", 15);
        getConfig().addDefault("Hypixel.Connection.Settings.Items.Lightningstick.Name", "&eLightning stick");
        getConfig().addDefault("Hypixel.Connection.Settings.Items.Lightningstick.Lore", "Tesla would be happy!");
        getConfig().addDefault("Hypixel.Connection.Settings.Items.Lightningstick.BroadCast", "&c%player%&f gave everyone a &eLightning Stick&f!");
        //FirstJoinDefaultSettings
        getConfig().addDefault("Hypixel.Settings.Default.Coins", 0);
        getConfig().addDefault("Hypixel.Settings.Default.MysteryDust", 0);
        getConfig().addDefault("Hypixel.Settings.Default.EXP", 0.0);
        getConfig().addDefault("Hypixel.Settings.Default.Plus", "&c+");
        getConfig().addDefault("Hypixel.Settings.Default.JoinAlert", 0);
        getConfig().addDefault("Hypixel.Settings.Default.Level", 0);
        //ChatManeger
        getConfig().addDefault("Hypixel.Settings.chat.default", "&7%player%: ");
        getConfig().addDefault("Hypixel.Settings.chat.admin", "&7%rank% %player%&e: ");
        getConfig().addDefault("Hypixel.Settings.chat.owner", "&7%rank% %player%&e: ");
        getConfig().addDefault("Hypixel.Settings.chat.Other", "%rank% %player%&f: ");

        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    private void createDB(){
        try {
            DatabaseMetaData dm = c.getMetaData();
            ResultSet tables = dm.getTables(null, null, "Players", null);
            tables = dm.getTables(null, null, "Players", null);
                Statement s = c.createStatement();
                s.executeUpdate(
                        "CREATE TABLE IF NOT EXISTS Players (UUID VARCHAR(36)," +
                                " `Coins` INT(11)," +
                                " `MysteryDust` INT(11)," +
                                " `EXP` FLOAT," +
                                " `Level` INT(20)," +
                                " `Pluse` VARCHAR(6)," +
                                " `JoinAlert` BOOLEAN," +
                                " `Online` BOOLEAN," +
                                " `Server` VARCHAR(50));");
        } catch(Exception e) {
            e.printStackTrace();
            getLogger().severe("has experienced a fatal error. Please check your SQL setup and/or config.yml");
            Bukkit.getPluginManager().disablePlugin(Bukkit.getPluginManager().getPlugin("HypixelCore"));
        }
    }

    private boolean setupPermissions()
    {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }

    private boolean setupChat()
    {
        RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (chatProvider != null) {
            chat = chatProvider.getProvider();
        }

        return (chat != null);
    }
    public Permission getPermission() {
        return permission;
    }

    public Chat getChat() {
        return chat;
    }


}
