package me.pinglagger.cmd;

import me.pinglagger.Effect;
import me.pinglagger.util.commands.Command;
import me.pinglagger.util.commands.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class HelpCMD {
    @Command(name = "effects", inGameOnly = true, permission = "", aliases = {""})
    public void onCommand(CommandArgs cmd) {
        Player player = cmd.getPlayer();
        for (String msg : Effect.getInstance().getConfig().getStringList("Help"))
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }
}
