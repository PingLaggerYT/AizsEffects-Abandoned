package me.pinglagger.cmd;

import me.pinglagger.Effect;
import me.pinglagger.util.commands.Command;
import me.pinglagger.util.commands.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SpeedCMD {
    @Command(name = "speed", inGameOnly = true, permission = "effect.speed", aliases = {"speed"})
    public void OnCommand(CommandArgs cmd) {
        Player player = cmd.getPlayer();
        if (!player.hasPotionEffect(PotionEffectType.SPEED)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 3600000, Effect.getInstance().getConfig().getInt("SpeedLevel") - 1));
            String Prefix = ChatColor.translateAlternateColorCodes('&', Effect.getInstance().getConfig().getString("Prefix"));
            String SpeedDisable = ChatColor.translateAlternateColorCodes('&', Effect.getInstance().getConfig().getString("SpeedEnable"));
            player.sendMessage(String.valueOf(String.valueOf(Prefix)) + " " + SpeedDisable);
        } else if (player.hasPotionEffect(PotionEffectType.SPEED)) {
            player.removePotionEffect(PotionEffectType.SPEED);
            String Prefix = ChatColor.translateAlternateColorCodes('&', Effect.getInstance().getConfig().getString("Prefix"));
            String SpeedDisable = ChatColor.translateAlternateColorCodes('&', Effect.getInstance().getConfig().getString("SpeedDisable"));
            player.sendMessage(String.valueOf(String.valueOf(Prefix)) + " " + SpeedDisable);
        }
    }
}
