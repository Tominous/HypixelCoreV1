package me.ikeetjeop.hypixel.commands;

import me.ikeetjeop.hypixel.HypixelCore;
import me.ikeetjeop.hypixel.mysql.mysql.MySQL;
import me.ikeetjeop.hypixel.utilities.UUIDFetcher;
import me.ikeetjeop.hypixel.utilities.configmanagement.MessagesYML;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by @Ikeetjeop aka Nick on 2/09/2017.
 * If this project not uploaded on github you're not allowed to
 * use this code for your project!
 */
public class LevelCMD implements CommandExecutor {

    HypixelCore plugin = HypixelCore.getInstance();

    MessagesYML config = new MessagesYML();

    @Override
    public boolean onCommand(CommandSender p, Command cmd, String commandLabel, String[] args) {
        try {
            MySQL mySQL = new MySQL(plugin.sqlHost, plugin.sqlPort, plugin.sqlDb, plugin.sqlUser, plugin.sqlPw);

            Statement statement = mySQL.openConnection().createStatement();
            if (cmd.getName().equalsIgnoreCase("level")) {
                if (args.length == 0) {
                    if (p.hasPermission("Hypixel.level.add") || p.hasPermission("Hypixel.level.remove") || p.hasPermission("Hypixel.level.set") || p.hasPermission("Hypixel.level.Check")) {
                        p.sendMessage(ChatColor.YELLOW + "===================================================");
                        p.sendMessage(ChatColor.DARK_AQUA + "/level add <player> <exp/level> <amount>");
                        p.sendMessage(ChatColor.DARK_AQUA + "/level remove <player> <exp/level> <amount>");
                        p.sendMessage(ChatColor.DARK_AQUA + "/level set <player> <exp/level> <amount>");
                        p.sendMessage(ChatColor.DARK_AQUA + "/level check <player>");
                        p.sendMessage(ChatColor.YELLOW + "===================================================");

                    }
                } else {
                    if (args[0].equalsIgnoreCase("add")) {
                        if (p.hasPermission("Hypixel.level.add")){
                            if (args.length == 1) {
                                p.sendMessage(ChatColor.GOLD + "/level add <Player> <exp/level> <amount>");

                            } else if (args.length == 2) {
                                p.sendMessage(ChatColor.GOLD + "/level add " + args[1] + " <exp/level> <amount>");

                            } else if (args.length >= 3) {
                                if (args[2].equalsIgnoreCase("exp")) {
                                    try {
                                        ResultSet res = statement.executeQuery("SELECT * FROM Players WHERE UUID='" + UUIDFetcher.getUUIDOf(args[1]) + "'");
                                        if (res.next()) {
                                            Player op = Bukkit.getPlayer(args[1]);
                                            if (op != null) {
                                                op.setExp((res.getFloat("Exp") + Float.parseFloat(args[3])));
                                            }

                                            p.sendMessage(ChatColor.AQUA + args[1] + ChatColor.GOLD + " had " + ChatColor.AQUA + res.getDouble("Exp") + "EXP" + ChatColor.GOLD + " And now has " + ChatColor.AQUA + (res.getDouble("Exp") + Float.parseFloat(args[3])) + "EXP");
                                            statement.executeUpdate("UPDATE Players SET Exp=" + (res.getDouble("Exp") + Float.parseFloat(args[3])) + " WHERE UUID='" + UUIDFetcher.getUUIDOf(args[1]) + "'");
                                        } else {
                                            p.sendMessage(ChatColor.RED + "This player never joined this server!");
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                } else if (args[2].equalsIgnoreCase("level")) {
                                    try {
                                        ResultSet res = statement.executeQuery("SELECT * FROM Players WHERE UUID='" + UUIDFetcher.getUUIDOf(args[1]) + "'");
                                        if (res.next()) {
                                            Player op = Bukkit.getPlayer(args[1]);
                                            if (op != null) {
                                                op.setLevel((res.getInt("Level") + Integer.parseInt(args[3])));
                                            }
                                            p.sendMessage(ChatColor.AQUA + args[1] + ChatColor.GOLD + " had " + ChatColor.AQUA + res.getInt("Level") + "Level" + ChatColor.GOLD + " And now has " + ChatColor.AQUA + (res.getInt("Level") + Integer.parseInt(args[3])) + "Level");
                                            statement.executeUpdate("UPDATE Players SET Level=" + (res.getInt("Level") + Integer.parseInt(args[3])) + " WHERE UUID='" + UUIDFetcher.getUUIDOf(args[1]) + "'");
                                        } else {
                                            p.sendMessage(ChatColor.RED + "This player never joined this server!");
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                } else {
                                    p.sendMessage(ChatColor.RED + "You can only choose Exp or Level.");
                                }
                            }
                        }else {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.Global.AdminPerms")));
                        }
                    } else if (args[0].equalsIgnoreCase("remove")) {
                        if(p.hasPermission("Hypixel.level.remove")) {
                            if (args.length == 1) {
                                p.sendMessage(ChatColor.GOLD + "/level remove <Player> <exp/level> <amount>");

                            } else if (args.length == 2) {
                                p.sendMessage(ChatColor.GOLD + "/level remove " + args[1] + " <exp/level> <amount>");

                            } else if (args.length >= 3) {
                                if (args[2].equalsIgnoreCase("exp")) {
                                    try {
                                        ResultSet res = statement.executeQuery("SELECT * FROM Players WHERE UUID='" + UUIDFetcher.getUUIDOf(args[1]) + "'");
                                        if (res.next()) {
                                            Player op = Bukkit.getPlayer(args[1]);
                                            if (op != null) {
                                                op.setExp((res.getFloat("Exp") + Float.parseFloat(args[3])));
                                            }
                                            p.sendMessage(ChatColor.AQUA + args[1] + ChatColor.GOLD + " had " + ChatColor.AQUA + res.getDouble("Exp") + "EXP" + ChatColor.GOLD + " And now has " + ChatColor.AQUA + (res.getDouble("Exp") - Float.parseFloat(args[3])) + "EXP");
                                            statement.executeUpdate("UPDATE Players SET Exp=" + (res.getDouble("Exp") - Float.parseFloat(args[3])) + " WHERE UUID='" + UUIDFetcher.getUUIDOf(args[1]) + "'");
                                        } else {
                                            p.sendMessage(ChatColor.RED + "This player never joined this server!");
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                } else if (args[2].equalsIgnoreCase("level")) {
                                    try {
                                        ResultSet res = statement.executeQuery("SELECT * FROM Players WHERE UUID='" + UUIDFetcher.getUUIDOf(args[1]) + "'");
                                        if (res.next()) {
                                            Player op = Bukkit.getPlayer(args[1]);
                                            if (op != null) {
                                                op.setLevel((res.getInt("Level") - Integer.parseInt(args[3])));
                                            }
                                            p.sendMessage(ChatColor.AQUA + args[1] + ChatColor.GOLD + " Have " + ChatColor.AQUA + (res.getInt("Level") - Integer.parseInt(args[3])) + "Level" + ChatColor.GOLD + " And now has " + ChatColor.AQUA + res.getInt("Level") + "Level");
                                            statement.executeUpdate("UPDATE Players SET Level=" + (res.getInt("Level") - Integer.parseInt(args[3])) + " WHERE UUID='" + UUIDFetcher.getUUIDOf(args[1]) + "'");
                                        } else {
                                            p.sendMessage(ChatColor.RED + "This player never joined this server!");
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                } else {
                                    p.sendMessage(ChatColor.RED + "You can only choose Exp or Level.");
                                }
                            }
                        } else {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.Global.AdminPerms")));
                        }
                    } else if (args[0].equalsIgnoreCase("set")) {
                        if(p.hasPermission("Hypixel.Level.set")) {
                            if (args.length == 1) {
                                p.sendMessage(ChatColor.GOLD + "/level set <Player> <exp/level> <amount>");

                            } else if (args.length == 2) {
                                p.sendMessage(ChatColor.GOLD + "/level set " + args[1] + " <exp/level> <amount>");

                            } else if (args.length >= 3) {
                                if (args[2].equalsIgnoreCase("exp")) {
                                    try {
                                        ResultSet res = statement.executeQuery("SELECT * FROM Players WHERE UUID='" + UUIDFetcher.getUUIDOf(args[1]) + "'");
                                        if (res.next()) {
                                            Player op = Bukkit.getPlayer(args[1]);
                                            if (op != null) {
                                                op.setExp(Float.parseFloat(args[3]));
                                            }
                                            p.sendMessage(ChatColor.AQUA + args[1] + ChatColor.GOLD + " Have now " + ChatColor.AQUA + Integer.parseInt(args[3]) + "EXP");
                                            statement.executeUpdate("UPDATE Players SET Exp=" + Float.parseFloat(args[3]) + " WHERE UUID='" + UUIDFetcher.getUUIDOf(args[1]) + "'");
                                        } else {
                                            p.sendMessage(ChatColor.RED + "This player never joined this server!");
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                } else if (args[2].equalsIgnoreCase("level")) {
                                    try {
                                        ResultSet res = statement.executeQuery("SELECT * FROM Players WHERE UUID='" + UUIDFetcher.getUUIDOf(args[1]) + "'");
                                        if (res.next()) {
                                            Player op = Bukkit.getPlayer(args[1]);
                                            if (op != null) {
                                                op.setLevel(Integer.parseInt(args[3]));
                                            }
                                            p.sendMessage(ChatColor.AQUA + args[1] + ChatColor.GOLD + " Have now " + ChatColor.AQUA + Integer.parseInt(args[3]) + "Level");
                                            statement.executeUpdate("UPDATE Players SET Level=" + Integer.parseInt(args[3]) + " WHERE UUID='" + UUIDFetcher.getUUIDOf(args[1]) + "'");
                                        } else {
                                            p.sendMessage(ChatColor.RED + "This player never joined this server!");
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                } else {
                                    p.sendMessage(ChatColor.RED + "You can only choose Exp or Level.");
                                }
                            }
                        }else {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.Global.AdminPerms")));
                        }
                    }else if (args[0].equalsIgnoreCase("check")) {
                        if(p.hasPermission("Hypixel.level.check")) {
                            if (args.length == 1) {
                                p.sendMessage(ChatColor.GOLD + "/level check <Player>");

                            } else if (args.length == 2) {
                                ResultSet res = statement.executeQuery("SELECT * FROM Players WHERE UUID='" + UUIDFetcher.getUUIDOf(args[1]) + "'");
                                if (res.next()) {
                                    p.sendMessage(ChatColor.AQUA + args[1] + ChatColor.GOLD + " is level " + ChatColor.AQUA + res.getInt("Level") + ChatColor.GOLD + " And has " + ChatColor.AQUA + res.getDouble("Exp") + "Exp('s)");
                                } else {
                                    p.sendMessage(ChatColor.RED + "This player never joined this server!");
                                }
                            }
                        }else {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.Global.AdminPerms")));
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "Unknow sub-command");
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;
    }
}
