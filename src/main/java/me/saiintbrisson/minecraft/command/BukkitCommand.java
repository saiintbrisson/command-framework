package me.saiintbrisson.minecraft.command;

import lombok.Getter;
import me.saiintbrisson.minecraft.command.annotations.Command;
import me.saiintbrisson.minecraft.command.argument.Argument;
import me.saiintbrisson.minecraft.command.argument.ArgumentType;
import me.saiintbrisson.minecraft.command.argument.CommandArgument;
import me.saiintbrisson.minecraft.command.exceptions.IncorrectTargetException;
import me.saiintbrisson.minecraft.command.exceptions.IncorrectUsageException;
import me.saiintbrisson.minecraft.command.exceptions.MissingPermissionException;
import me.saiintbrisson.minecraft.command.exceptions.NoRegisteredConverterException;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class BukkitCommand extends org.bukkit.command.Command {

    private final CommandFrame owner;

    @Getter
    private final List<BukkitSubCommand> subCommands = new ArrayList<>();

    @Getter
    private final int position;

    @Getter
    private Command command;

    private String[] options;

    private Object commandHolder;
    private Method commandMethod;

    private Object completerHolder;
    private Method completerMethod;

    private final List<CommandArgument> arguments = new ArrayList<>();

    public BukkitCommand(CommandFrame owner, String name, int position) {
        super(name);
        this.owner = owner;
        this.position = position;
    }

    public BukkitCommand(CommandFrame owner, String name) {
        this(owner, name, 0);
    }

    public void setCommand(Object holder, Command command, Method method) {
        this.commandHolder = holder;
        this.command = command;
        this.commandMethod = method;

        this.options = command.options();

        setAliases(Arrays.asList(command.aliases()));

        registerArguments();

        setPermission(command.permission());

        setUsage(command.usage());
        if (getUsage().length() == 0) {
            createUsage();
        }
    }

    public void setCompleter(Object holder, Method method) {
        this.completerHolder = holder;
        this.completerMethod = method;
    }

    @Override
    public String getDescription() {
        return command.description();
    }

    public String getFancyName() {
        return getName();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!owner.getOwner().isEnabled()) {
            return false;
        }

        if (!testPermissionSilent(sender)) {
            sender.sendMessage(owner.getLackPermMessage().replace("{permission}", getPermission()));
            return false;
        }

        if (!command.target().validate(sender)) {
            sender.sendMessage(owner.getIncorrectTargetMessage().replace("{target}", command.target().name()));
            return false;
        }

        if (args.length > 0) {
            BukkitSubCommand command = getSubCommand(args[0]);
            if (command != null) {
                return command.execute(
                  sender,
                  commandLabel + " " + args[0],
                  Arrays.copyOfRange(args, 1, args.length)
                );
            }
        }

        String[] options = getOptions(args);
        String[] finalArgs = Arrays.copyOfRange(args, 0, args.length - options.length);
        Execution execution = new Execution(sender, commandLabel, finalArgs, options);

        Object object = invokeCommand(execution);

        if (object != null && object.getClass().equals(Boolean.TYPE)) {
            return (boolean) object;
        } else {
            return false;
        }
    }

    public Object invokeCommand(Execution execution) {
        try {
            if (arguments.size() == 0) {
                return commandMethod.invoke(commandHolder);
            }

            Object[] parameters = parseArguments(execution);
            if (parameters == null) {
                throw new InvocationTargetException(new IncorrectUsageException());
            }

            return commandMethod.invoke(commandHolder, parameters);
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof IncorrectTargetException) {
                execution.sendMessage(owner.getIncorrectTargetMessage().replace("{target}", command.target().name()));
            } else if (e.getTargetException() instanceof IncorrectUsageException) {
                execution.sendMessage(owner.getUsageMessage().replace("{usage}", getUsage()));
            }  else if (e.getTargetException() instanceof MissingPermissionException) {
                execution.sendMessage(owner.getLackPermMessage().replace("{permission}", getPermission()));
            } else {
                e.printStackTrace();

                if (e.getMessage() != null) {
                    execution.sendMessage(owner.getErrorMessage().replace("{error}", e.getMessage()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            execution.sendMessage("§cAn internal error occurred, please contact the staff team.");
        }

        return false;
    }

    private Object[] parseArguments(Execution execution) {
        Object[] parameters = new Object[0];
        int i = 0;

        try {
            for (CommandArgument<?> argument : arguments) {
                if (argument.getClassType().equals(Execution.class)) {
                    parameters = ArrayUtils.add(parameters, execution);
                    continue;
                }

                String arg = execution.getArg(i);
                if (arg == null && !argument.isNullable()) {
                    Object defaultValue = argument.getDefaultValue();
                    if (defaultValue == null) {
                        throw new IncorrectUsageException();
                    }

                    i++;
                    parameters = ArrayUtils.add(parameters, defaultValue);
                    continue;
                }

                i++;

                if (arg == null) {
                    parameters = ArrayUtils.add(parameters, null);
                    continue;
                }

                Object parse;
                if (argument.isArray()) {
                    parse = Array.newInstance(argument.getClassType(), 0);

                    do {
                        parse = ArrayUtils.add(
                          (Object[]) parse,
                          argument.getType().getRule().validateNonNull(arg)
                        );

                        i++;
                    } while ((arg = execution.getArg(i - 1)) != null);
                } else {
                    parse = argument.getType().getRule().validateNonNull(arg);
                }

                parameters = ArrayUtils.add(parameters, parse);
            }

            return parameters;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args)
      throws IllegalArgumentException {
        if (!testPermissionSilent(sender)) {
            return null;
        }

        if (args.length > 0) {
            BukkitSubCommand command = getSubCommand(args[0]);
            if (command != null) {
                return command.tabComplete(
                  sender,
                  alias + " " + args[0],
                  Arrays.copyOfRange(args, 1, args.length)
                );
            }
        }

        if (completerMethod == null || completerMethod.getReturnType() != List.class) {
            if (subCommands.size() == 0) {
                return Bukkit.getOnlinePlayers()
                  .stream()
                  .map(Player::getName)
                  .filter(s -> args.length <= 0 || s.toLowerCase().startsWith(args[0].toLowerCase()))
                  .collect(Collectors.toList());
            } else {
                return subCommands.stream()
                  .map(BukkitCommand::getName)
                  .filter(s -> args.length <= 0 || s.toLowerCase().startsWith(args[0].toLowerCase()))
                  .collect(Collectors.toList());
            }
        }

        Class<?>[] types = completerMethod.getParameterTypes();

        try {
            if (types.length == 0) {
                return (List<String>) completerMethod.invoke(completerHolder);
            } else if (types.length == 1 && types[0] == Execution.class) {
                return (List<String>) completerMethod.invoke(completerHolder,
                  new Execution(sender, alias, args, new String[0]));
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public BukkitSubCommand getSubCommand(String name) {
        for (BukkitSubCommand subCommand : subCommands) {
            if (subCommand.equals(name)) return subCommand;
        }

        return null;
    }

    private String[] getOptions(String[] args) {
        List<String> options = new LinkedList<>();
        for (int i = args.length - 1; i >= 0; i--) {
            String option = args[i];
            if (option.length() == 1 || !option.startsWith("-")) break;
            option = option.substring(1);

            for (String s : this.options) {
                if ((option.length() == 1 && s.charAt(0) == option.charAt(0)) || option.equals("-" + s)) {
                    options.add(s);
                    break;
                }
            }
        }

        return options.toArray(new String[0]);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BukkitCommand that = (BukkitCommand) o;

        return position == that.position;
    }

    public boolean equals(String name) {
        if (getName().equalsIgnoreCase(name)) {
            return true;
        }

        for (String alias : getAliases()) {
            if (alias.equalsIgnoreCase(name)) return true;
        }

        return false;
    }

    // Command setup

    public BukkitCommand createRecursive(String name) {
        int position = getPosition() + StringUtils.countMatches(name, ".");
        if (position == getPosition()) {
            return this;
        }

        String subName = name.substring(Math.max(name.indexOf('.') + 1, 0));

        int index = subName.indexOf('.');
        String nextSubCommand = subName;
        if (index != -1) {
            nextSubCommand = subName.substring(0, index);
        }

        BukkitSubCommand subCommand = getSubCommand(nextSubCommand);

        if (subCommand == null) {
            subCommand = new BukkitSubCommand(owner, nextSubCommand, this);
            getSubCommands().add(subCommand);
        }

        return subCommand.createRecursive(name);
    }

    public void registerArguments() {
        if (commandMethod == null) {
            return;
        }

        Parameter[] parameters = commandMethod.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Class<?> clazz = parameter.getType();

            if (clazz.equals(Execution.class)) {
                arguments.add(new CommandArgument<>(
                    new ArgumentType<>(null, clazz),
                    false, null, false
                  )
                );

                continue;
            }

            boolean isArray = false;
            if (clazz.isArray()) {
                if (i != parameters.length - 1) {
                    throw new IllegalArgumentException("Arrays must be the last parameter in a command, "
                      + parameter.getName()
                      + "(" + getName() + ")");
                }

                isArray = true;
                clazz = clazz.getComponentType();
            }

            ArgumentType<?> type = owner.getType(clazz);

            if (type == null) {
                throw new NoRegisteredConverterException("No registered type for parameter "
                  + parameter.getName()
                  + "(" + getName() + ")");
            }

            Argument argument = parameter.getDeclaredAnnotation(Argument.class);
            Object defaultValue;
            boolean nullable;

            if (argument != null) {
                nullable = argument.nullable();

                if (nullable && clazz.isPrimitive()) {
                    throw new IllegalArgumentException("Primitive type parameter "
                      + parameter.getName() + " may not be nullable "
                      + "(" + getName() + ")");
                }

                String[] strings = argument.defaultValue();
                if (strings.length == 0) {
                    arguments.add(new CommandArgument(type, isArray, null, nullable));
                    continue;
                }

                if (isArray) {
                    defaultValue = Array.newInstance(clazz, 0);

                    for (String string : strings) {

                        try {
                            defaultValue = ArrayUtils.add(
                              (Object[]) defaultValue,
                              type.getRule().validateNonNull(string)
                            );
                        } catch (Exception e) {
                            throw new IllegalArgumentException("Invalid default value for parameter: "
                              + parameter.getName()
                              + "(" + getName() + "): " + string);
                        }

                    }
                } else {

                    try {
                        defaultValue = type.getRule().validateNonNull(String.join(" ", strings[0]));
                    } catch (Exception e) {
                        throw new IllegalArgumentException("Invalid default value for parameter: "
                          + parameter.getName()
                          + "(" + getName() + "): " + strings[0]);
                    }

                }

                arguments.add(new CommandArgument(type, isArray, defaultValue, nullable));
            } else {
                arguments.add(new CommandArgument(type, isArray, null, false));
            }
        }
    }

    public void createUsage() {
        StringBuilder builder = new StringBuilder(getFancyName());

        for (CommandArgument<?> argument : arguments) {
            if (argument.getClassType().equals(Execution.class)) {
                continue;
            }

            builder.append(argument.isNullable() ? " [" : " <");

            builder.append(StringUtils.uncapitalize(argument.getClassType().getSimpleName()));
            if (argument.isArray()) {
                builder.append("...");
            }

            builder.append(argument.isNullable() ? "]" : ">");
        }

        setUsage(builder.toString());
    }

}
