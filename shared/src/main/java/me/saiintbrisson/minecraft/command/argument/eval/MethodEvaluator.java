package me.saiintbrisson.minecraft.command.argument.eval;

import lombok.RequiredArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.argument.AdapterMap;
import me.saiintbrisson.minecraft.command.argument.Argument;
import me.saiintbrisson.minecraft.command.argument.TypeAdapter;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.exception.NoSuchConverterException;
import me.saiintbrisson.minecraft.command.util.ArrayUtil;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class MethodEvaluator {
    private final AdapterMap adapterMap;

    public List<Argument<?>> evaluateMethod(Method method) {
        final List<Argument<?>> argumentList = new ArrayList<>();

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

            argumentList.add(createOptional(method, type, optional.def(), builder));
        }

        return argumentList;
    }

    private Argument createOptional(Method method, Class type,
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
}
