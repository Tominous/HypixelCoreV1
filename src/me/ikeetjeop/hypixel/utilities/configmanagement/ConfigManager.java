package me.ikeetjeop.hypixel.utilities.configmanagement;

import me.ikeetjeop.hypixel.HypixelCore;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ikeeetjeop on 23/08/2017 at 10:48.
 */
public class ConfigManager {

    protected File file;
    protected FileConfiguration config;

    public ConfigManager(String FileName) {
        this.file = new File(HypixelCore.getInstance().getDataFolder() + "/" + FileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
            }
        }
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        try {
            config.load(file);
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
        }
    }

    public String getString(String string) {
        return config.getString(string);
    }
    public int getInt(String string) {
        return config.getInt(string);
    }
    public List<String> getStringList(String string){
        return config.getStringList(string);
    }
}
