package me.ikeetjeop.hypixel.commands;

import me.ikeetjeop.hypixel.HypixelCore;
import me.ikeetjeop.hypixel.utilities.ChatUtilitie;
import me.ikeetjeop.hypixel.utilities.configmanagement.MessagesYML;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

/**
     * Created by @Ikeetjeop aka Nick on 10/09/2017.
     * If this project not uploaded on github you're not allowed to
     * use this code for your project!
     */
public class FireWorkCMD implements CommandExecutor {
    public static HashMap<UUID, Long> cooldown = new HashMap<>();
    MessagesYML config = new MessagesYML();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("fw")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (sender.hasPermission("Hypixel.firework") || sender.hasPermission("Hypixel.fw")) {
                    try {
                        if(p.hasPermission("Hypixel.CDBypass.Firework")) {
                            Firework f = p.getWorld().spawn(p.getPlayer().getLocation(), Firework.class);
                            FireworkMeta fm = f.getFireworkMeta();
                            fireworkCode(p, fm, f);
                        } else if (cooldown.containsKey(p.getUniqueId()) && cooldown.get(p.getUniqueId()) > System.currentTimeMillis()) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.Commands.FireWork.Cooldown").replace("%seconds%", HypixelCore.getInstance().getConfig().getInt("Hypixel.Settings.FireworkCooldown") + "")));
                        } else {
                            Firework f = p.getWorld().spawn(p.getPlayer().getLocation(), Firework.class);
                            FireworkMeta fm = f.getFireworkMeta();
                            fireworkCode(p, fm, f);
                            cooldown.put(p.getUniqueId(), System.currentTimeMillis() + (HypixelCore.getInstance().getConfig().getInt("Hypixel.Settings.FireworkCooldown") * 1000));
                        }
                    } catch (Exception ex) {
                        p.sendMessage(ChatColor.RED + "Error with running firework! Please report this error to an admin or higher!");
                        ChatUtilitie.Chat(ChatUtilitie.Format.Error, "With running firework please PM me A.S.A.P: https://www.spigotmc.org/conversations/add");
                        ChatUtilitie.Chat(ChatUtilitie.Format.Error, "Post this error under this message in a HasteBin.com:");
                        ex.printStackTrace();
                    }
                }
            }
        }
        return false;
    }

    public boolean randomBoolean() {
        switch (new Random().nextInt(2)) {
            case 0:
                return true;
            case 1:
                return false;
        }
        return randomBoolean();
    }

    public Type randomType() {
        switch (new Random().nextInt(5)) {
            case 0:
                return Type.BALL;
            case 1:
                return Type.BALL_LARGE;
            case 2:
                return Type.BURST;
            case 3:
                return Type.CREEPER;
            case 4:
                return Type.STAR;
        }
        return randomType();
    }

    public Color randomColor() {
        switch (new Random().nextInt(17)) {
            case 0:
                return Color.AQUA;
            case 1:
                return Color.BLACK;
            case 2:
                return Color.YELLOW;
            case 3:
                return Color.WHITE;
            case 4:
                return Color.TEAL;
            case 5:
                return Color.SILVER;
            case 6:
                return Color.RED;
            case 7:
                return Color.PURPLE;
            case 8:
                return Color.ORANGE;
            case 9:
                return Color.OLIVE;
            case 10:
                return Color.NAVY;
            case 11:
                return Color.MAROON;
            case 12:
                return Color.LIME;
            case 13:
                return Color.GREEN;
            case 14:
                return Color.GRAY;
            case 15:
                return Color.FUCHSIA;
            case 16:
                return Color.BLUE;
        }
        return randomColor();
    }

    public void fireworkCode(Player p, FireworkMeta fm, Firework f) {
        fm.addEffect(FireworkEffect.builder()
                .flicker(randomBoolean())
                .trail(randomBoolean())
                .with(randomType())
                .withColor(randomColor())
                .withFade(randomColor())
                .build());
        fm.setPower(1);
        f.setFireworkMeta(fm);
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.Commands.Firework.Launch")));
    }
}
