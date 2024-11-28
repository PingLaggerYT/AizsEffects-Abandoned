package me.pinglagger.cmd;


import me.pinglagger.Effect;
import me.pinglagger.util.commands.Command;
import me.pinglagger.util.commands.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FireresCMD {
    @Command(name = "fres", inGameOnly = true, permission = "effect.fire", aliases = {"fireresistan", "fireres"})
    public void OnCommand(CommandArgs cmd) {
        Player player = cmd.getPlayer();
        if (!player.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 3600000, Effect.getInstance().getConfig().getInt("FireResLevel") - 1));
            String Prefix = ChatColor.translateAlternateColorCodes('&', Effect.getInstance().getConfig().getString("Prefix"));
            String FireResDisable = ChatColor.translateAlternateColorCodes('&', Effect.getInstance().getConfig().getString("FireResEnable"));
            player.sendMessage(String.valueOf(String.valueOf(Prefix)) + " " + FireResDisable);
        } else if (player.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE)) {
            player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
            String Prefix = ChatColor.translateAlternateColorCodes('&', Effect.getInstance().getConfig().getString("Prefix"));
            String FireResDisable = ChatColor.translateAlternateColorCodes('&', Effect.getInstance().getConfig().getString("FireResDisable"));
            player.sendMessage(String.valueOf(String.valueOf(Prefix)) + " " + FireResDisable);
        }
    }
}
