package me.ikeetjeop.hypixel.utilities.configmanagement;

        import me.ikeetjeop.hypixel.HypixelCore;
        import org.bukkit.ChatColor;

        import java.util.Set;

/**
 * Created by Ikeeetjeop on 23/08/2017 at 10:50.
 */
public class MessagesYML extends ConfigManager {

    public MessagesYML(HypixelCore main) {
        super(main, "Messages.yml");
    }

    public void SetString(){
        config.addDefault("Messages.connect.JoinMessage", "%rank% %player% &6joined the lobby!");
        config.addDefault("Messages.Chat.Member", "&7%player%: ");
        config.addDefault("Messages.Chat.Ranked", "%rank%%player%&f: ");
        config.addDefault("Messages.Commands.JoinAlert.Self.Enabled", "&aYou set joinAlert On!");
        config.addDefault("Messages.Commands.JoinAlert.Self.Disable", "&cYou set joinAlert Off!");
        config.options().copyDefaults(true);
        save();
    }

    public String getString(String string) {
        return config.getString(ChatColor.translateAlternateColorCodes('&', string));
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
