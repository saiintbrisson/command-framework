package me.saiintbrisson.commands;

import lombok.Getter;
import lombok.Setter;
import me.saiintbrisson.commands.annotations.Command;
import me.saiintbrisson.commands.annotations.Completer;
import me.saiintbrisson.commands.argument.ArgumentType;
import me.saiintbrisson.commands.argument.ArgumentValidationRule;
import me.saiintbrisson.commands.result.ResultType;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ExecutorService;

@Getter
public class CommandFrame {

    private Plugin owner;

    private CommandMap commandMap;

    @Setter
    private String lackPermMessage, inGameOnlyMessage, usageMessage, errorMessage, usageArrayOf;

    private final List<LocalCommand> commands = new ArrayList<>();
    private final List<ArgumentType<?>> types = new ArrayList<>();

    @Setter
    private ExecutorService service;

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
        }
    }

    public CommandFrame(Plugin plugin) {
        this(plugin, true);
    }

    {
        lackPermMessage = "§cYou do not have enough permissions.";
        inGameOnlyMessage = "§cThis command is only available in-game";
        usageMessage = "§cCorrect usage: §e/{usage}§c.";
        errorMessage = "§cAn error has been thrown: §f{error}§c.";
        usageArrayOf = "array of";
    }

    public <T> void registerType(Class<T> clazz, ArgumentValidationRule<T> rule) {
        registerType(
                ArgumentType.<T>builder()
                        .clazz(clazz)
                        .rule(rule)
                        .build()
        );
    }

    public <T> void registerType(ArgumentType<T> type) {
        types.add(type);
    }

    public <T> ArgumentType<T> getType(Class<T> clazz) {
        for (ArgumentType<?> type : types) {
            if (clazz.isAssignableFrom(type.getClazz())) {
                return (ArgumentType<T>) type;
            }
        }

        return null;
    }

    public void register(Object... holders) {
        registerCommands(holders);
        registerCompleters(holders);
    }

    public void registerCommands(Object... holders) {
        for (Object holder : holders) {
            Map<String, Method> map = new HashMap<>();

            for (Method method : holder.getClass().getMethods()) {
                Command command = method.getDeclaredAnnotation(Command.class);
                if (command == null) {
                    continue;
                }

                Class<?> returnType = method.getReturnType();
                if (!returnType.equals(Void.TYPE)
                        && !returnType.equals(Boolean.TYPE)
                        && !returnType.equals(Boolean.class)
                        && !returnType.equals(ResultType.class)) continue;

                map.put(command.name(), method);
            }

            registerCommands(holder, map);
        }
    }

    private void registerCommands(Object holder, Map<String, Method> map) {
        for (Map.Entry<String, Method> entry : map.entrySet()) {
            Method method = entry.getValue();
            Command command = method.getDeclaredAnnotation(Command.class);
            String[] split = command.name().split("\\.");
            LocalCommand localCommand = getCommand(split[0]);

            StringBuilder name = new StringBuilder(split[0]);
            if (localCommand == null) {
                localCommand = getCommand(holder, map, name.toString());

                commandMap.register(owner.getName(), localCommand);
                commands.add(localCommand);
            }

            for (int i = 1; i < split.length; i++) {
                name.append(".").append(split[i]);

                LocalCommand newCommand = localCommand.getSubCommand(split[i]);
                if (newCommand != null) {
                    localCommand = newCommand;
                    continue;
                }

                newCommand = getCommand(holder, map, name.toString());
                localCommand.getSubCommands().add(newCommand);
                localCommand = newCommand;
            }
        }
    }

    private LocalCommand getCommand(Object holder, Map<String, Method> map, String name) {
        Method method = map.get(name);
        Command command = method.getDeclaredAnnotation(Command.class);

        return new LocalCommand(this, holder, command, method);
    }

    private LocalCommand getCommand(String name) {
        for (LocalCommand command : commands) {
            if (command.getName().equals(name)) return command;
        }

        return null;
    }

    public void registerCompleters(Object... holders) {
        for (Object holder : holders) {
            for (Method method : holder.getClass().getMethods()) {
                Completer completer = method.getDeclaredAnnotation(Completer.class);
                if (completer == null) {
                    continue;
                }

                Class<?>[] types = method.getParameterTypes();
                if (types.length > 1
                        || (types.length == 1
                        && types[0] != Execution.class)) continue;

                Class<?> returnType = method.getReturnType();
                if (returnType != List.class) continue;

                registerCompleter(completer, method);
            }
        }
    }

    public void registerCompleter(Completer completer, Method method) {
        String[] split = completer.name().split("\\.");
        LocalCommand localCommand = matchCommand(split[0], commands);

        if (localCommand == null) {
            return;
        }

        split = Arrays.copyOfRange(split, 1, split.length);
        for (String s : split) {
            LocalCommand newCommand = matchCommand(s, localCommand.getSubCommands());

            if (newCommand == null) {
                return;
            }

            localCommand = newCommand;
        }

        localCommand.setCompleter(method);
    }

    public LocalCommand matchCommand(String input, List<LocalCommand> commands) {
        for (LocalCommand command : commands) {
            if (input.equalsIgnoreCase(command.getName())) {
                return command;
            }

            for (String alias : command.getAliases()) {
                if (input.equalsIgnoreCase(alias)) {
                    return command;
                }
            }
        }

        return null;
    }

}
