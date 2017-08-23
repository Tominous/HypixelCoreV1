package me.ikeetjeop.hypixel;

import me.ikeetjeop.hypixel.listener.connect.JoinListener;
import me.ikeetjeop.hypixel.listener.connect.QuitListener;
import me.ikeetjeop.hypixel.mysql.mysql.MySQL;
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

        try {
            c = MySQL.openConnection();
            Bukkit.getConsoleSender().sendMessage("MySql connected!");
            createDB();
        }catch (SQLException | ClassNotFoundException e){
            getLogger().log(Level.WARNING, "Error while connect to database! check config.yml > MySQL connection");
            Bukkit.getPluginManager().disablePlugin(Bukkit.getPluginManager().getPlugin("HypixelCore"));
            return;
        }
        registerListeners();
        registerCommands();
        registerConfig();
    }

    public void onDisable() {
        try {
            if (!c.isClosed()) {
                MySQL.closeConnection();
            }
        } catch (SQLException ex) {

        }
    }

    private void registerCommands(){

    }

    public void registerListeners(){
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new JoinListener(), this);
        pm.registerEvents(new QuitListener(), this);

    }

    private void registerConfig(){
        getConfig().addDefault("Hypixel.Connection.MySQL.Host", "localhost");
        getConfig().addDefault("Hypixel.Connection.MySQL.Port", 3306);
        getConfig().addDefault("Hypixel.Connection.MySQL.Database", "hypixelcore");
        getConfig().addDefault("Hypixel.Connection.MySQL.User", "root");
        getConfig().addDefault("Hypixel.Connection.MySQL.Password", "");
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
                                "`Name` VARCHAR(32), " +
                                "`Rank` VARCHAR(60)," +
                                " `Coins` INT(11)," +
                                " `MysteryDust` INT(11)," +
                                " `Exp` INT(20)," +
                                " `Pluse` VARCHAR(6)," +
                                " `JoinAlert` BOOLEAN," +
                                " `IsOnline` BOOLEAN);");
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


}
