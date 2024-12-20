package me.pinglagger.util.commands;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.help.GenericCommandHelpTopic;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.HelpTopicComparator;
import org.bukkit.help.IndexHelpTopic;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;

public class CommandFramework implements CommandExecutor {
    private Map<String, Map.Entry<Method, Object>> commandMap = new HashMap<>();

    private CommandMap map;

    private Plugin plugin;

    public CommandFramework(Plugin plugin) {
        this.plugin = plugin;
        if (plugin.getServer().getPluginManager() instanceof SimplePluginManager) {
            SimplePluginManager manager = (SimplePluginManager)plugin.getServer().getPluginManager();
            try {
                Field field = SimplePluginManager.class.getDeclaredField("commandMap");
                field.setAccessible(true);
                this.map = (CommandMap)field.get(manager);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        return handleCommand(sender, cmd, label, args);
    }

    public boolean handleCommand(CommandSender sender, Command cmd, String label, String[] args) {
        for (int i = args.length; i >= 0; i--) {
            StringBuffer buffer = new StringBuffer();
            buffer.append(label.toLowerCase());
            for (int x = 0; x < i; x++)
                buffer.append("." + args[x].toLowerCase());
            String cmdLabel = buffer.toString();
            if (this.commandMap.containsKey(cmdLabel)) {
                Method method = (Method)((Map.Entry)this.commandMap.get(cmdLabel)).getKey();
                Object methodObject = ((Map.Entry)this.commandMap.get(cmdLabel)).getValue();
                Command command = method.<Command>getAnnotation(Command.class);
                if (!command.permission().equalsIgnoreCase("") && !sender.hasPermission(command.permission())) {
                    sender.sendMessage(ChatColor.RED + command.noPerm());
                    return true;
                }
                if (command.inGameOnly() && !(sender instanceof org.bukkit.entity.Player)) {
                    sender.sendMessage("This command is only performable in game");
                    return true;
                }
                try {
                    method.invoke(methodObject, new Object[] { new CommandArgs(sender, cmd, label, args, (cmdLabel
                            .split("\\.")).length - 1) });
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        defaultCommand(new CommandArgs(sender, cmd, label, args, 0));
        return true;
    }

    public void registerCommands(Object obj) {
        for (Method m : obj.getClass().getMethods()) {
            if (m.getAnnotation(Command.class) != null) {
                Command command = m.<Command>getAnnotation(Command.class);
                if ((m.getParameterTypes()).length > 1 || m.getParameterTypes()[0] != CommandArgs.class) {
                    System.out.println("Unable to register command " + m.getName() + ". Unexpected method arguments");
                } else {
                    registerCommand(command, command.name(), m, obj);
                    for (String alias : command.aliases())
                        registerCommand(command, alias, m, obj);
                }
            } else if (m.getAnnotation(Completer.class) != null) {
                Completer comp = m.<Completer>getAnnotation(Completer.class);
                if ((m.getParameterTypes()).length > 1 || (m.getParameterTypes()).length == 0 || m
                        .getParameterTypes()[0] != CommandArgs.class) {
                    System.out.println("Unable to register tab completer " + m.getName() + ". Unexpected method arguments");
                } else if (m.getReturnType() != List.class) {
                    System.out.println("Unable to register tab completer " + m.getName() + ". Unexpected return type");
                } else {
                    registerCompleter(comp.name(), m, obj);
                    for (String alias : comp.aliases())
                        registerCompleter(alias, m, obj);
                }
            }
        }
    }

    public void registerHelp() {
        Set<HelpTopic> help = new TreeSet<>((Comparator<? super HelpTopic>)HelpTopicComparator.helpTopicComparatorInstance());
        for (String s : this.commandMap.keySet()) {
            if (!s.contains(".")) {
                Command cmd = this.map.getCommand(s);
                GenericCommandHelpTopic genericCommandHelpTopic = new GenericCommandHelpTopic(cmd);
                help.add(genericCommandHelpTopic);
            }
        }
        IndexHelpTopic topic = new IndexHelpTopic(this.plugin.getName(), "All commands for " + this.plugin.getName(), null, help, "Below is a list of all " + this.plugin.getName() + " commands:");
        Bukkit.getServer().getHelpMap().addTopic((HelpTopic)topic);
    }

    public void registerCommand(Command command, String label, Method m, Object obj) {
        this.commandMap.put(label.toLowerCase(), new AbstractMap.SimpleEntry<>(m, obj));
        this.commandMap.put(this.plugin.getName() + ':' + label.toLowerCase(), new AbstractMap.SimpleEntry<>(m, obj));
        String cmdLabel = label.split("\\.")[0].toLowerCase();
        if (this.map.getCommand(cmdLabel) == null) {
            Command cmd = new BukkitCommand(cmdLabel, this, this.plugin);
            this.map.register(this.plugin.getName(), cmd);
        }
        if (!command.description().equalsIgnoreCase("") && cmdLabel == label)
            this.map.getCommand(cmdLabel).setDescription(command.description());
        if (!command.usage().equalsIgnoreCase("") && cmdLabel == label)
            this.map.getCommand(cmdLabel).setUsage(command.usage());
    }

    public void registerCompleter(String label, Method m, Object obj) {
        String cmdLabel = label.split("\\.")[0].toLowerCase();
        if (this.map.getCommand(cmdLabel) == null) {
            Command command = new BukkitCommand(cmdLabel, this, this.plugin);
            this.map.register(this.plugin.getName(), command);
        }
        if (this.map.getCommand(cmdLabel) instanceof BukkitCommand) {
            BukkitCommand command = (BukkitCommand)this.map.getCommand(cmdLabel);
            if (command.completer == null)
                command.completer = new BukkitCompleter();
            command.completer.addCompleter(label, m, obj);
        } else if (this.map.getCommand(cmdLabel) instanceof org.bukkit.command.PluginCommand) {
            try {
                Object command = this.map.getCommand(cmdLabel);
                Field field = command.getClass().getDeclaredField("completer");
                field.setAccessible(true);
                if (field.get(command) == null) {
                    BukkitCompleter completer = new BukkitCompleter();
                    completer.addCompleter(label, m, obj);
                    field.set(command, completer);
                } else if (field.get(command) instanceof BukkitCompleter) {
                    BukkitCompleter completer = (BukkitCompleter)field.get(command);
                    completer.addCompleter(label, m, obj);
                } else {
                    System.out.println("Unable to register tab completer " + m.getName() + ". A tab completer is already registered for that command!");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void defaultCommand(CommandArgs args) {
        args.getSender().sendMessage(args.getLabel() + " is not handled! Oh noes!");
    }
}
