package me.ikeetjeop.hypixel.utilities;

import org.bukkit.Bukkit;

import java.sql.Time;
import java.time.LocalTime;
import java.util.logging.Level;

/**
 * Created by @Ikeetjeop aka Nick on 16/09/2017.
 * If this project not uploaded on github you're not allowed to
 * use this code for your project!
 */
public class ChatUtilitie {

    public enum Format {
        Error
    }
    public static void Chat(Enum<Format> type, String errorInfo){
        if(type.equals(Format.Error)){
            Bukkit.getLogger().log(Level.WARNING, "[HyPixelcore] [ERROR] " + Time.valueOf(LocalTime.now()) + " " + errorInfo);
        }
    }
}
