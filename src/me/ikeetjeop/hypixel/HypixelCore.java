package me.ikeetjeop.hypixel;

import me.ikeetjeop.hypixel.commands.JoinAlert;
import me.ikeetjeop.hypixel.listener.chat.ChatListener;
import me.ikeetjeop.hypixel.listener.connect.JoinListener;
import me.ikeetjeop.hypixel.listener.connect.QuitListener;
import me.ikeetjeop.hypixel.mysql.mysql.MySQL;
import me.ikeetjeop.hypixel.utilities.configmanagement.MessagesYML;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
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
    //SQLite sqLite = new SQLite(getDataFolder() + "/Database.db");
    Connection c = null;


    public void onEnable(){
        setupChat();
        setupPermissions();
        registerConfig();
        //If you edit the plugin, Feels free to remove this =)
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
    }

    private void registerCommands(){
        getCommand("JoinAlert").setExecutor(new JoinAlert());

    }

    public void registerListeners(){
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new JoinListener(), this);
        pm.registerEvents(new QuitListener(), this);
        pm.registerEvents(new ChatListener(), this);

    }

    private MessagesYML message;
    private void registerConfig(){
        this.message = new MessagesYML(this);
        message.SetString();
        getConfig().options().header("MYSQL needed for save playerdata!");
        //Mysql
        getConfig().addDefault("Hypixel.Connection.MySQL.Host", "localhost");
        getConfig().addDefault("Hypixel.Connection.MySQL.Port", 3306);
        getConfig().addDefault("Hypixel.Connection.MySQL.Database", "hypixelcore");
        getConfig().addDefault("Hypixel.Connection.MySQL.User", "root");
        getConfig().addDefault("Hypixel.Connection.MySQL.Password", "ThisIsABadPassword123!!");
        //ServerName
        getConfig().addDefault("Hypixel.Settings.ServerName", "Main Lobby");
        //DefaultSettings
        getConfig().addDefault("Hypixel.Settings.Default.Coins", 0);
        getConfig().addDefault("Hypixel.Settings.Default.MysteryDust", 0);
        getConfig().addDefault("Hypixel.Settings.Default.EXP", 0);
        getConfig().addDefault("Hypixel.Settings.Default.Plus", "&c+");
        getConfig().addDefault("Hypixel.Settings.Default.JoinAlert", 0);
        //ChatManeger
        getConfig().addDefault("Hypixel.Settings.chat.default", "&7%player%: ");
        getConfig().addDefault("Hypixel.Settings.chat.ExampleRank1", "&7%rank% %player%>> ");
        getConfig().addDefault("Hypixel.Settings.chat.ExampleRank2", "&8[YouCantChangeThisPrefix] %player%>> ");
        getConfig().addDefault("Hypixel.Settings.chat.Other", "%rank% %player%&f: ");
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    private void createDB(){
        try {
            DatabaseMetaData dm = c.getMetaData();
            ResultSet tables = dm.getTables(null, null, "Players", null);
            tables = dm.getTables(null, null, "Players", null);
            if(!tables.next()) {
                Statement s = c.createStatement();
                s.executeUpdate(
                        "CREATE TABLE Players (UUID VARCHAR(36)," +
                                " `Coins` INT(11)," +
                                " `MysteryDust` INT(11)," +
                                " `Exp` INT(20)," +
                                " `Pluse` VARCHAR(6)," +
                                " `JoinAlert` BOOLEAN," +
                                " `Online` BOOLEAN);");
            }
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
