package me.pinglagger.cmd;

import me.pinglagger.Effect;
import me.pinglagger.util.commands.Command;
import me.pinglagger.util.commands.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class NightVisionCMD {
    @Command(name = "nightvision", inGameOnly = true, permission = "effect.nv", aliases = {"nv"})
    public void OnCommand(CommandArgs cmd) {
        Player player = cmd.getPlayer();
        if (!player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 3600000, Effect.getInstance().getConfig().getInt("NightVisionLevel") - 1));
            String Prefix = ChatColor.translateAlternateColorCodes('&', Effect.getInstance().getConfig().getString("Prefix"));
            String NightVisionDisable = ChatColor.translateAlternateColorCodes('&', Effect.getInstance().getConfig().getString("NigthVisionEnable"));
            player.sendMessage(String.valueOf(String.valueOf(Prefix)) + " " + NightVisionDisable);
        } else if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
            String Prefix = ChatColor.translateAlternateColorCodes('&', Effect.getInstance().getConfig().getString("Prefix"));
            String NightVisionDisable = ChatColor.translateAlternateColorCodes('&', Effect.getInstance().getConfig().getString("NightVisionDisable"));
            player.sendMessage(String.valueOf(String.valueOf(Prefix)) + " " + NightVisionDisable);
        }
    }
}
