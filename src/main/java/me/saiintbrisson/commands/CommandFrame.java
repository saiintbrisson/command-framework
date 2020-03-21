package me.saiintbrisson.commands;

import lombok.Getter;
import lombok.Setter;
import me.saiintbrisson.commands.annotations.Command;
import me.saiintbrisson.commands.annotations.Completer;
import me.saiintbrisson.commands.result.ResultType;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class CommandFrame {

    private Plugin owner;

    private CommandMap commandMap;

    private @Setter String lackPermMessage;
    private @Setter String inGameOnlyMessage;
    private @Setter String usageMessage;

    private final List<LocalCommand> commands = new ArrayList<>();

    public CommandFrame(Plugin plugin) {
        this.owner = plugin;

        try {
            Server server = Bukkit.getServer();
            Field field = server.getClass().getDeclaredField("commandMap");
            field.setAccessible(true);

            commandMap = (CommandMap) field.get(server);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registerCommands(Object... holders) {
        for (Object holder : holders) {
            for (Method method : holder.getClass().getMethods()) {
                Command command = method.getDeclaredAnnotation(Command.class);
                if(command == null) {
                    continue;
                }

                Class<?>[] types = method.getParameterTypes();
                if(types.length > 1
                        || (types.length == 1
                        && types[0] != Execution.class)) continue;

                Class<?> returnType = method.getReturnType();
                if(returnType != Void.class
                        && returnType != Boolean.class
                        && returnType != ResultType.class) continue;

                registerCommand(holder, command, method);
            }
        }
    }

    public void registerCompleters(Object... holders) {
        for (Object holder : holders) {
            for (Method method : holder.getClass().getMethods()) {
                Completer completer = method.getDeclaredAnnotation(Completer.class);
                if(completer == null) {
                    continue;
                }

                Class<?>[] types = method.getParameterTypes();
                if(types.length > 1
                        || (types.length == 1
                        && types[0] != Execution.class)) continue;

                Class<?> returnType = method.getReturnType();
                if(returnType != List.class) continue;

                registerCompleter(completer, method);
            }
        }
    }

    public void registerCompleter(Completer completer, Method method) {
        String[] split = completer.name().split("\\.");
        LocalCommand localCommand = matchCommand(split[0], commands);

        if(localCommand == null) {
            return;
        }

        split = Arrays.copyOfRange(split, 1, split.length);
        for (String s : split) {
            LocalCommand newCommand = matchCommand(s, localCommand.getSubCommands());

            if(newCommand == null) {
                return;
            }

            localCommand = newCommand;
        }

        localCommand.setCompleter(method);
    }

    public void registerCommand(Object holder, Command command, Method method) {
        String[] split = command.name().split("\\.");
        LocalCommand localCommand = matchCommand(split[0], commands);

        if(localCommand == null) {
            localCommand = new LocalCommand(this, holder, command, method);
            commandMap.register(owner.getName(), localCommand);
            commands.add(localCommand);
        }

        split = Arrays.copyOfRange(split, 1, split.length);
        for (String s : split) {
            LocalCommand newCommand = matchCommand(s, localCommand.getSubCommands());

            if(newCommand == null) {
                newCommand = new LocalCommand(this, holder, command, method);
                localCommand.getSubCommands().add(newCommand);
            }

            localCommand = newCommand;
        }
    }

    public LocalCommand matchCommand(String input, List<LocalCommand> commands) {
        for (LocalCommand command : commands) {
            if(input.equalsIgnoreCase(command.getName())) {
                return command;
            }

            for (String alias : command.getAliases()) {
                if(input.equalsIgnoreCase(alias)) {
                    return command;
                }
            }
        }

        return null;
    }

}
