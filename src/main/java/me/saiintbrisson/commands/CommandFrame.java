package me.saiintbrisson.commands;

import lombok.Getter;
import lombok.Setter;
import me.saiintbrisson.commands.annotations.Command;
import me.saiintbrisson.commands.annotations.Completer;
import me.saiintbrisson.commands.argument.ArgumentType;
import me.saiintbrisson.commands.argument.ArgumentValidationRule;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class CommandFrame {

    private final Plugin owner;
    private CommandMap commandMap;

    private final Map<String, BukkitCommand> bukkitCommandMap = new HashMap<>();
    private final Map<Class<?>, ArgumentType<?>> types = new HashMap<>();

    @Setter
    private String lackPermMessage, incorrectTargetMessage, usageMessage, errorMessage;

    {
        lackPermMessage = "§cYou do not have enough permissions. Required permission: §f{permission}§c.";
        incorrectTargetMessage = "§cYou cannot use this command. Targeted to: §f{target}§c.";
        usageMessage = "§cCorrect usage: §e/{usage}§c.";
        errorMessage = "§cAn error has been thrown: §f{error}§c.";
    }

    public CommandFrame(Plugin plugin, boolean registerDefault) {
        this.owner = plugin;

        try {
            Server server = Bukkit.getServer();
            final Method mapMethod = server.getClass().getMethod("getCommandMap");

            commandMap = (CommandMap) mapMethod.invoke(server);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (registerDefault) {
            registerType(String.class, String::valueOf);
            registerType(Character.class, argument -> argument.charAt(0));
            registerType(Integer.class, Integer::valueOf);
            registerType(Double.class, Double::valueOf);
            registerType(Long.class, Long::valueOf);
            registerType(Boolean.class, Boolean::valueOf);
            registerType(Byte.class, Byte::valueOf);

            registerType(Character.TYPE, argument -> argument.charAt(0));
            registerType(Integer.TYPE, Integer::parseInt);
            registerType(Double.TYPE, Double::parseDouble);
            registerType(Long.TYPE, Long::parseLong);
            registerType(Boolean.TYPE, Boolean::parseBoolean);
            registerType(Byte.TYPE, Byte::parseByte);

            registerType(Player.class, Bukkit::getPlayer);
            registerType(Material.class, argument -> Material.valueOf(argument.toUpperCase()));
        }
    }

    public CommandFrame(Plugin plugin) {
        this(plugin, true);
    }

    public <T> ArgumentType<T> getType(Class<T> clazz) {
        for (ArgumentType<?> type : types.values()) {
            if (clazz.isAssignableFrom(type.getType())) {
                return (ArgumentType<T>) type;
            }
        }

        return null;
    }

    public <T> void registerType(Class<T> clazz, ArgumentValidationRule<T> rule) {
        registerType(
          ArgumentType.<T>builder()
            .type(clazz)
            .rule(rule)
            .build()
        );
    }

    public <T> void registerType(ArgumentType<T> type) {
        types.put(type.getType(), type);
    }

    public void register(Object... objects) {
        for (Object object : objects) {
            registerSingle(object);
        }
    }

    private void registerSingle(Object object) {
        for (Method method : object.getClass().getDeclaredMethods()) {
            Command command = method.getAnnotation(Command.class);
            if (command != null) {
                registerCommand(object, command, method);
                continue;
            }

            Completer completer = method.getAnnotation(Completer.class);
            if (completer != null) {
                registerCompleter(object, completer, method);
            }
        }
    }

    private void registerCommand(Object object, Command command, Method method) {
        Class<?> returnType = method.getReturnType();
        if (!returnType.equals(Void.TYPE)
          && !returnType.equals(Boolean.TYPE)) {
            System.err.println("Could not register command '" + command.name() + "' (check your return type)");
            return;
        }

        BukkitCommand recursive = getRecursive(command.name());
        if (recursive == null) {
            return;
        }

        recursive.setCommand(object, command, method);
        commandMap.register(owner.getName(), recursive);
    }

    private void registerCompleter(Object object, Completer completer, Method method) {
        Class<?> returnType = method.getReturnType();
        if (!returnType.equals(List.class)) {
            System.err.println("Could not register completer for '" + completer.name() + "' (check your return type)");
            return;
        }

        Class[] parameters = method.getParameterTypes();
        if (parameters.length > 1 || (parameters.length == 1 && !parameters[0].equals(Execution.class))) {
            System.err.println("Could not register completer for '" + completer.name() + "' (check your parameters)");
            return;
        }

        BukkitCommand recursive = getRecursive(completer.name());
        if (recursive == null) {
            return;
        }

        recursive.setCompleter(object, method);
    }

    public BukkitCommand getRecursive(String name) {
        int index = name.indexOf('.');
        String nextSubCommand = name;
        if (index != -1) {
            nextSubCommand = name.substring(0, index);
        }

        BukkitCommand subCommand = bukkitCommandMap.get(nextSubCommand);
        if (subCommand == null) {
            subCommand = new BukkitCommand(this, nextSubCommand);
            bukkitCommandMap.put(nextSubCommand, subCommand);
        }

        return subCommand.createRecursive(name);
    }

}
