package me.ikeetjeop.hypixel.utilities.configmanagement;
import org.bukkit.ChatColor;
import java.util.Set;

/**
 * Created by Ikeeetjeop on 23/08/2017 at 10:50.
 */
public class MessagesYML extends ConfigManager {

    public MessagesYML() {
        super("Messages.yml");
    }

    public void setString(){
        //connect
        config.addDefault("Messages.Connect.JoinMessage", "%rank% %player% &6joined the lobby!");
        //Chat
        config.addDefault("Messages.Chat.Member", "&7%player%: ");
        config.addDefault("Messages.Chat.Ranked", "%rank%%player%&f: ");
        //Tablist (Github user? look connect package for edit code)
        config.addDefault("Messages.TabList.Header", "&bYou are playing on &e&lMC.HYPIXEL.RED");
        config.addDefault("Messages.TabList.Footer", "&aRanks, Boosters & MORE! &c&lSTORE.HYPIXEL.RED");
        //Command JoinAlertCMD
        config.addDefault("Messages.Commands.JoinAlert.Self.Enabled", "&aJoin alert enabled!");
        config.addDefault("Messages.Commands.JoinAlert.Self.Disabled", "&cJoin alert disabled!");
        //Command Walker
        config.addDefault("Messages.Commands.Walker.Self.Enabled", "&aWalker enabled!");
        config.addDefault("Messages.Commands.Walker.Self.Disabled", "&aWalker disabled!");
        //Command Fly
        config.addDefault("Messages.Commands.Fly.Self.Enabled", "&aTurned on flight!");
        config.addDefault("Messages.Commands.Fly.Self.Disabled", "&aTurned off flight!");
        config.addDefault("Messages.Commands.Fly.Target.Enabled.Target", "&a%player% turned on your flight!");
        config.addDefault("Messages.Commands.Fly.Target.Disabled.Target", "&a%player% turned off your flight!");
        config.addDefault("Messages.Commands.Fly.Target.Enabled.Self", "&aYou turned on flight for %target%");
        config.addDefault("Messages.Commands.Fly.Target.Disabled.Self", "&aYou turned off flight for %target%");
        config.addDefault("Messages.Commands.Fly.Everyone.Enabled", "&c%player%&f enabled &efly&f for everyone!");
        config.addDefault("Messages.Commands.Fly.Everyone.Disabled", "&c%player%&f disabled &efly&f for everyone!");
        //Command FireWork
        config.addDefault("Messages.Commands.Firework.Launch", "&eLaunched a firework!");
        config.addDefault("Messages.Commands.FireWork.Cooldown", "&cYou have to wait %seconds% seconds between sending fireworks!");
        //Command KABOOOOMM
        config.addDefault("Messages.Commands.Kaboom.Self", "&aLaunched %player%!");
        //Items Visibility item
        config.addDefault("Messages.Items.Visibility.Action.Enabled", "&aPlayer visibility enabled!");
        config.addDefault("Messages.Items.Visibility.Action.Disabled", "&cPlayer visibility disabled!");
        //Items SmiteStick
        config.addDefault("Messages.Items.lightningstick.Target.Give", "&c%player%&f gave you a &eLightning Stick&f!");
        config.addDefault("Messages.Items.lightningstick.Target.Take", "&c%player%&f took your &eLightning Stick&f!");
        config.addDefault("Messages.Items.lightningstick.Everyone.Give", "&c%player%&f gave everyone a &eLightning Stick&f!");
        config.addDefault("Messages.Items.lightningstick.Everyone.Take", "&c%player%&f took everyone his &eLightning Stick&f!");
        //Can use for everyting
        config.addDefault("Messages.Global.PlayerNotFound", "&cThis player does not exist");
        config.addDefault("Messages.Global.AdminPerms", "&cYou must be admin or higher to use this command!");
        config.options().copyDefaults(true);
        save();
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
