package me.saiintbrisson.commands;

import lombok.Getter;
import lombok.Setter;
import me.saiintbrisson.commands.annotations.Command;
import me.saiintbrisson.commands.argument.CommandArgument;
import me.saiintbrisson.commands.argument.ArgumentType;
import me.saiintbrisson.commands.argument.Argument;
import me.saiintbrisson.commands.result.ResultType;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class LocalCommand extends org.bukkit.command.Command {

    private CommandFrame owner;
    private final @Getter
    List<LocalCommand> subCommands = new ArrayList<>();

    private Object holder;
    private Method method;

    private String[] options;

    private boolean async;
    private boolean inGameOnly;

    @Setter
    private Method completer;

    private List<CommandArgument> arguments = new ArrayList<>();

    public LocalCommand(CommandFrame owner, Object holder, Command command, Method method) {
        super(command.name().split("\\.")[command.name().split("\\.").length - 1]);
        // TODO: 3/21/2020 fix this shitty line ^^

        this.owner = owner;
        this.holder = holder;
        this.method = method;

        setAliases(Arrays.asList(command.aliases()));
        setDescription(command.description());
        setUsage(command.usage().equals("") ? command.name() : command.usage());
        setPermission(command.permission().equals("") ? null : command.permission());

        options = command.options();

        async = command.async();
        inGameOnly = command.inGameOnly();

        registerArguments();
    }

    public LocalCommand(String name) {
        super(name);
    }

    public void override(CommandFrame owner, Object holder, Command command, Method method) {
        this.owner = owner;
        this.holder = holder;
        this.method = method;

        setAliases(Arrays.asList(command.aliases()));
        setDescription(command.description());
        setUsage(command.usage().equals("") ? command.name() : command.usage());
        setPermission(command.permission().equals("") ? null : command.permission());

        options = command.options();

        async = command.async();
        inGameOnly = command.inGameOnly();

        registerArguments();
    }

    public boolean needsOverride() {
        return holder == null;
    }

    private void registerArguments() {
        for(Parameter parameter : method.getParameters()) {
            Class<?> clazz = parameter.getType();
            if(clazz.equals(Execution.class)) {
                arguments.add(new CommandArgument<>(
                    new ArgumentType<>(
                      null,
                      clazz
                    ),
                    null,
                    false
                  )
                );

                continue;
            }

            ArgumentType<?> type = owner.getType(clazz);
            if(type == null) {
                throw new NullPointerException("No registered type for parameter "
                  + parameter.getName()
                  + "(" + getName() + ")");
            }

            Argument argument = parameter.getDeclaredAnnotation(Argument.class);
            Object defaultValue = null;
            boolean nullable = false;

            if(argument != null) {
                nullable = argument.nullable();

                if(nullable && clazz.isPrimitive()) {
                    throw new IllegalArgumentException("Primitive type parameter "
                      + parameter.getName() + " may not be nullable "
                      + "(" + getName() + ")");
                }

                String[] strings = argument.defaultValue();
                if(strings.length > 0) {

                    try {
                        defaultValue = type.getRule().validateNonNull(
                          String.join(" ", strings[0])
                        );
                    } catch (Exception e) {
                        throw new IllegalArgumentException("Invalid default value for parameter: "
                          + parameter.getName()
                          + "(" + getName() + ")");
                    }

                }

            }

            arguments.add(new CommandArgument(type, defaultValue, nullable));
        }
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if(!owner.getOwner().isEnabled()) {
            return false;
        }

        if(inGameOnly && !(sender instanceof Player)) {
            sender.sendMessage(owner.getInGameOnlyMessage());
            return false;
        }

        if(!testPermissionSilent(sender)) {
            sender.sendMessage(owner.getLackPermMessage());
            return false;
        }

        if(args.length > 0) {
            LocalCommand command = owner.matchCommand(args[0], subCommands);
            if(command != null) {
                return command.execute(
                  sender,
                  commandLabel + " " + args[0],
                  Arrays.copyOfRange(args, 1, args.length)
                );
            }
        }

        String[] options = getOptions(args);
        args = Arrays.copyOfRange(args, 0, args.length - options.length);
        Execution execution = new Execution(sender, commandLabel, args, options);

        if(async) {
            CompletableFuture.runAsync(() -> run(execution));
        } else {
            return run(execution);
        }

        return false;
    }

    private boolean run(Execution execution) {
        Object object = invoke(execution);

        if(object instanceof Void) {
            return false;
        } else if(object instanceof Boolean) {
            return (Boolean) object;
        } else if(object instanceof ResultType) {
            ResultType resultType = (ResultType) object;

            switch(resultType) {
                case NO_PERMISSION:
                    String message = owner.getLackPermMessage();
                    if(message == null) return false;

                    execution.sendMessage(message);
                    return false;
                case INCORRECT_USAGE:
                    message = owner.getUsageMessage();
                    if(message == null) return false;

                    execution.sendMessage(message.replace("{usage}", getUsage()));
                    return false;
                case IN_GAME_ONLY:
                    message = owner.getInGameOnlyMessage();
                    if(message == null) return false;

                    execution.sendMessage(owner.getInGameOnlyMessage());
                    return false;
                default:
                    return true;
            }
        } else {
            return false;
        }
    }

    private Object invoke(Execution execution) {
        try {
            if(arguments.size() == 0) {
                return method.invoke(holder);
            }

            Object[] parameters = new Object[0];
            int i = 0;

            try {

                for(CommandArgument<?> argument : arguments) {
                    if(argument.getClassType().equals(Execution.class)) {
                        parameters = ArrayUtils.add(parameters, execution);
                        continue;
                    }

                    String arg = execution.getArg(i);
                    if(arg == null && !argument.isNullable()) {
                        Object defaultValue = argument.getDefaultValue();
                        if(defaultValue == null) {
                            throw new NullPointerException();
                        }

                        parameters = ArrayUtils.add(parameters, defaultValue);
                        i++;
                        continue;
                    }

                    i++;

                    if(arg == null) {
                        parameters = ArrayUtils.add(parameters, null);
                        continue;
                    }

                    Object parse = argument.getType().getRule().validateNonNull(arg);
                    parameters = ArrayUtils.add(parameters, parse);
                }

            } catch (Exception e) {
                String usageMessage = owner.getUsageMessage();
                if(usageMessage != null) {
                    execution.sendMessage(usageMessage.replace("{usage}", getUsage()));
                }

                return null;
            }

            return method.invoke(holder, parameters);
        } catch (Exception e) {
            e.printStackTrace();

            final String errorMessage = owner.getErrorMessage();
            final String message = e.getMessage();

            if(errorMessage != null && message != null) {
                execution.sendMessage(errorMessage
                  .replace("{error}", message));
            }

            return null;
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args)
      throws IllegalArgumentException {
        if(!testPermissionSilent(sender)) {
            return new ArrayList<>();
        }

        if(args.length > 0) {
            LocalCommand command = owner.matchCommand(args[0], subCommands);
            if(command != null) {
                return command.tabComplete(
                  sender,
                  alias + " " + args[0],
                  Arrays.copyOfRange(args, 1, args.length)
                );
            }
        }

        if(completer == null || completer.getReturnType() != List.class) {
            if(subCommands.size() == 0) {
                return Bukkit.getOnlinePlayers()
                  .stream()
                  .map(Player::getName)
                  .filter(s -> args.length <= 0
                    || s.toLowerCase()
                    .startsWith(args[0].toLowerCase()))
                  .collect(Collectors.toList());
            } else {
                return subCommands.stream()
                  .map(LocalCommand::getName)
                  .filter(s -> args.length <= 0
                    || s.toLowerCase()
                    .startsWith(args[0].toLowerCase()))
                  .collect(Collectors.toList());
            }
        }

        Class<?>[] types = completer.getParameterTypes();

        try {
            if(types.length == 0) {
                return (List<String>) completer.invoke(holder);
            } else if(types.length == 1 && types[0] == Execution.class) {
                return (List<String>) completer.invoke(holder,
                  new Execution(sender, alias, args, new String[0]));
            } else {
                return new ArrayList<>();
            }
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private String[] getOptions(String[] args) {
        List<String> options = new LinkedList<>();
        for(int i = args.length - 1; i >= 0; i--) {
            String option = args[i];
            if(option.length() == 1 || !option.startsWith("-")) break;
            option = option.substring(1);

            for(String s : this.options) {
                if(s.equals(option)) {
                    options.add(option);
                    break;
                }
            }
        }

        return options.toArray(new String[0]);
    }

    public boolean containsSubCommand(String name) {
        for(LocalCommand subCommand : subCommands) {
            if(subCommand.getName().equals(name)) return true;
        }

        return false;
    }

    public LocalCommand getSubCommand(String name) {
        for(LocalCommand subCommand : subCommands) {
            if(subCommand.getName().equals(name)) return subCommand;
        }

        return null;
    }

}
