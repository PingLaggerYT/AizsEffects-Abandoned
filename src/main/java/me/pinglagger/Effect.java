package me.pinglagger;

import me.pinglagger.cmd.*;
import java.util.Arrays;
import java.util.Objects;

import me.pinglagger.util.commands.CommandFramework;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class Effect extends JavaPlugin implements Listener {
    public static Effect instance;

    public static Effect plugin;

    public static Effect getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;
        plugin = this;
        registerCommands();
        saveDefaultConfig();
        reloadConfig();
    }

    public void registerCommands() {
        registerCommands(new Object[] { new FireresCMD(), new SpeedCMD(), new HasteCMD(), new NightVisionCMD(), new HelpCMD(), new ReloadCMD() });
    }

    public void onDisable() {}

    private void registerCommands(Object... command) {
        CommandFramework commandFramework = new CommandFramework((Plugin)this);
        Objects.requireNonNull(commandFramework);
        Arrays.<Object>stream(command).forEach(commandFramework::registerCommands);
    }
}
