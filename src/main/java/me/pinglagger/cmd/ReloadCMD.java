package me.pinglagger.cmd;

import me.pinglagger.Effect;
import me.pinglagger.util.commands.Command;
import me.pinglagger.util.commands.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ReloadCMD {
    @Command(name = "effectsreload", inGameOnly = true, permission = "effect.reload", aliases = {"erl"})
    public void onCommand(CommandArgs cmd) {
        Player player = cmd.getPlayer();
        String ReloadMessage = ChatColor.translateAlternateColorCodes('&', Effect.getInstance().getConfig().getString("ReloadMessage"));
        player.sendMessage(String.valueOf(ReloadMessage));
        Effect.getInstance().reloadConfig();
    }
}
