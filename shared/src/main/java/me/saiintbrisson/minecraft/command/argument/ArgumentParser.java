package me.saiintbrisson.minecraft.command.argument;

import lombok.Getter;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.exception.CommandException;
import me.saiintbrisson.minecraft.command.exception.NoSuchConverterException;
import me.saiintbrisson.minecraft.command.message.MessageType;
import me.saiintbrisson.minecraft.command.util.ArrayUtil;
import me.saiintbrisson.minecraft.command.util.StringUtil;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author SaiintBrisson
 */
public class ArgumentParser<S> {

    @Getter
    private final List<Argument<?>> argumentList = new ArrayList<>();

    private final AdapterMap adapterMap;

    private final Method method;

    public ArgumentParser(AdapterMap adapterMap, Method method) {
        this.adapterMap = adapterMap;
        this.method = method;

        createArguments();
    }

    public Object[] parseArguments(Context<S> context) {
        Object[] parameters = new Object[0];
        AtomicInteger currentArg = new AtomicInteger(0);

        for (Argument<?> argument : argumentList) {
            if (Context.class.isAssignableFrom(argument.getType())) {
                parameters = ArrayUtil.add(parameters, context);
                continue;
            }

            String arg = readFullString(currentArg, context);
            if (arg == null) {
                if (!argument.isNullable()) {
                    throw new CommandException(MessageType.INCORRECT_USAGE, null);
                } else {
                    parameters = ArrayUtil.add(parameters, argument.getDefaultValue());
                }

                currentArg.incrementAndGet();
                continue;
            }

            Object object;

            if (argument.isArray()) {
                object = Array.newInstance(argument.getType(), 0);

                do {

                    object = ArrayUtil.add(
                      (Object[]) object,
                      argument.getAdapter().convertNonNull(arg)
                    );

                } while ((arg = readFullString(currentArg, context)) != null);

            } else {
                object = argument.getAdapter().convertNonNull(arg);
            }

            parameters = ArrayUtil.add(parameters, object);
        }

        return parameters;
    }

    private String readFullString(AtomicInteger currentArg, Context<S> context) {
        String arg = context.getArg(currentArg.get());
        if (arg == null) {
            return null;
        }

        currentArg.incrementAndGet();

        if (arg.charAt(0) == '"') {
            StringBuilder builder = new StringBuilder(arg.substring(1));

            while ((arg = context.getArg(currentArg.get())) != null) {
                builder.append(" ");
                currentArg.incrementAndGet();

                final int length = arg.length();

                if (arg.charAt(length - 1) == '"' && (length == 1 || arg.charAt(length - 2) != '\\')) {
                    builder.append(arg, 0, length - 1);
                    break;
                }

                builder.append(arg);
            }

            return builder.toString().replace("\\\"", "\"");
        } else {
            return arg;
        }
    }

    private void createArguments() {
        Parameter[] parameters = method.getParameters();

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Class type = parameter.getType();

            final Argument.ArgumentBuilder builder = Argument
              .builder()
              .name(parameter.getName())
              .type(type)
              .isArray(type.isArray());

            if (Context.class.isAssignableFrom(type)) {
                argumentList.add(builder.build());
                continue;
            }

            if (type.isArray()) {
                if (i != parameters.length - 1) {
                    throw new IllegalArgumentException("Arrays must be the last parameter in a command, "
                      + method.getName());
                }

                type = type.getComponentType();
                builder.type(type);
            }

            builder.adapter(adapterMap.get(type));

            final Optional optional = parameter.getDeclaredAnnotation(Optional.class);
            if (optional == null) {
                argumentList.add(builder.build());
                continue;
            }

            argumentList.add(createOptional(type, optional.def(), builder));
        }
    }

    private Argument createOptional(Class type,
                                    String[] def, Argument.ArgumentBuilder builder) {
        if (type.isPrimitive()) {
            throw new IllegalArgumentException("Use wrappers instead of primitive types for nullability, "
              + method.getName());
        }

        final TypeAdapter<?> adapter = adapterMap.get(type);
        if (adapter == null) {
            throw new NoSuchConverterException(type);
        }

        builder.isNullable(true);

        if (type.isArray() && def.length != 0) {
            builder.defaultValue(createArray(type, adapter, def));
        } else if (def.length != 0) {
            builder.defaultValue(adapter.convertNonNull(def[0]));
        }

        return builder.build();
    }

    private Object[] createArray(Class type, TypeAdapter<?> adapter, String[] def) {
        Object[] value = (Object[]) Array.newInstance(type, 0);

        for (String s : def) {
            value = ArrayUtil.add(
              value,
              adapter.convertNonNull(s)
            );
        }

        return value;
    }

    public String buildUsage(String name) {
        StringBuilder builder = new StringBuilder(name);

        for (Argument<?> argument : argumentList) {
            if (Context.class.isAssignableFrom(argument.getType())) {
                continue;
            }

            builder.append(argument.isNullable() ? " [" : " <");

            builder.append(StringUtil.uncapitalize(argument.getType().getSimpleName()));
            if (argument.isArray()) {
                builder.append("...");
            }

            builder.append(argument.isNullable() ? "]" : ">");
        }

        return builder.toString();
    }

}
