package me.ikeetjeop.hypixel.utilities;

        import me.ikeetjeop.hypixel.HypixelCore;

import java.util.Set;

/**
 * Created by Ikeeetjeop on 23/08/2017 at 10:50.
 */
public class RankYML extends ConfigManager {

    public RankYML(HypixelCore main) {
        super(main, "Rank");
    }

    public String getString(String string) {
        return config.getString(string);
    }

    public Set<String> getConfigurationSection(String string) {
        return config.getConfigurationSection(string).getKeys(false);
    }

    public void setString(String path, String value) {
        config.addDefault(path, value);
        config.options().copyDefaults(true);
        save();
    }
    public void setString(String path, String[] value) {
        config.addDefault(path, value);
        config.options().copyDefaults(true);
        save();
    }
}
