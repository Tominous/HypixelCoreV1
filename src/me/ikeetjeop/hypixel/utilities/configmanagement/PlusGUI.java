package me.ikeetjeop.hypixel.utilities.configmanagement;

import java.util.Set;

/**
 * Created by @Ikeetjeop aka Nick on 7/09/2017.
 * If this project not uploaded on github you're not allowed to
 * use this code for your project!
 */
public class PlusGUI extends ConfigManager{

    public PlusGUI() {
        super("PlusGUI.yml");
    }

    public void setString(){

        //SettingsGUI
        config.addDefault("PlusGUI.Settings.GUI.Title", "MVP Rank Color");
        config.addDefault("PlusGUI.Settings.GUI.Size", 5);
        //Settings status
        config.addDefault("PlusGUI.Settings.Status.RequiresMVP", "&cRequires &bMVP&c+");
        config.addDefault("PlusGUI.Settings.Status.NeedMoreExp", "&3Unlocked at Hypixel Level %exp%");
        config.addDefault("PlusGUI.Settings.Status.Unlocked", "&eClick to select!");
        config.addDefault("PlusGUI.Settings.Status.Selected", "&aCurrently selected!");

        //Item
        String[] lore = {"&7The default color for &bMVP&c+&7.", "", "%status%"};
        config.addDefault("PlusGUI.Colors.Red.Item", "DYE");
        config.addDefault("PlusGUI.Colors.Red.Data", 1);
        config.addDefault("PlusGUI.Colors.Red.Slot", 11);
        config.addDefault("PlusGUI.Colors.Red.Prefix", "&c");
        config.addDefault("PlusGUI.Colors.Red.Level", -1);
        config.addDefault("PlusGUI.Colors.Red.Name", "&cRed MVP+ Rank Color");
        config.addDefault("PlusGUI.Colors.Red.Lore", lore);

        config.options().copyDefaults(true);
        save();
    }

    public Set<String> getConfigurationSection(String string) {
        return config.getConfigurationSection(string).getKeys(false);
    }

}
