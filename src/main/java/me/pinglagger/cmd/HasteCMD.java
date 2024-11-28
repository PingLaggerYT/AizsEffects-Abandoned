package me.pinglagger.cmd;

import me.pinglagger.Effect;
import me.pinglagger.util.commands.Command;
import me.pinglagger.util.commands.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HasteCMD {
    @Command(name = "haste", inGameOnly = true, permission = "effect.haste", aliases = {"haste"})
    public void OnCommand(CommandArgs cmd) {
        Player player = cmd.getPlayer();
        if (!player.hasPotionEffect(PotionEffectType.FAST_DIGGING)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 3600000, Effect.getInstance().getConfig().getInt("HasteLevel") - 1));
            String Prefix = ChatColor.translateAlternateColorCodes('&', Effect.getInstance().getConfig().getString("Prefix"));
            String HasteDisable = ChatColor.translateAlternateColorCodes('&', Effect.getInstance().getConfig().getString("HasteEnable"));
            player.sendMessage(String.valueOf(String.valueOf(Prefix)) + " " + HasteDisable);
        } else if (player.hasPotionEffect(PotionEffectType.FAST_DIGGING)) {
            player.removePotionEffect(PotionEffectType.FAST_DIGGING);
            String Prefix = ChatColor.translateAlternateColorCodes('&', Effect.getInstance().getConfig().getString("Prefix"));
            String HasteDisable = ChatColor.translateAlternateColorCodes('&', Effect.getInstance().getConfig().getString("HasteDisable"));
            player.sendMessage(String.valueOf(String.valueOf(Prefix)) + " " + HasteDisable);
        }
    }
}
