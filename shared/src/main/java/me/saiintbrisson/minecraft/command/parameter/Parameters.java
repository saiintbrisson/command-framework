package me.saiintbrisson.minecraft.command.parameter;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.saiintbrisson.minecraft.command.annotation.Extract;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.exceptions.NoSuchAdapterException;
import me.saiintbrisson.minecraft.command.parameter.interfaces.Extractor;
import me.saiintbrisson.minecraft.command.parameter.interfaces.TypeAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Luiz Carlos Carvalho
 */
@RequiredArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public final class Parameters {
    private final Collection<HandlerParameter> parameters;

    public Optional<Class<?>> getException() {
        return this.parameters
          .stream()
          .filter(parameter -> parameter.behavior == ParameterBehavior.EXCEPTION)
          .findFirst()
          .map(p -> p.type);
    }

    public Collection<Object> extractParameters(Context<?> ctx, @Nullable Throwable exception) {
        Iterator<String> inputs = ctx.getInputs().values().iterator();

        return parameters.stream().map(parameter -> {
            Class<?> type = parameter.type;

            switch (parameter.behavior) {
                case ARGUMENT:
                    if (!inputs.hasNext()) {
                        throw new IllegalArgumentException("received less arguments than needed");
                    }

                    return parseArgument(type, ctx, inputs.next());
                case EXTRACTOR:
                    return instantiateExtractor(type, ctx);
                case EXCEPTION:
                    return extractException(exception, type);
                default:
                    throw new UnsupportedOperationException("unknown parameter type");
            }
        }).collect(Collectors.toList());
    }

    private static Object instantiateExtractor(Class<?> parameterType, Context<?> ctx) {
        Extractor<?> extractor = ctx.getCommandFrame().getExtractorMap().get(parameterType);
        if (extractor == null) {
            throw new NoSuchAdapterException(parameterType);
        }

        return extractor.extract(ctx);
    }

    private static Object parseArgument(Class<?> parameterType, Context<?> ctx, String input) {
        TypeAdapter<?> adapter = ctx.getCommandFrame().getAdapterMap().get(parameterType);
        if (adapter == null) {
            throw new NoSuchAdapterException(parameterType);
        }

        return adapter.convertNonNull(input);
    }

    private static Throwable extractException(@Nullable Throwable exception, Class<?> type) {
        if (exception == null) {
            throw new IllegalArgumentException("expected an exception");
        }

        if (!type.equals(exception.getClass())) {
            throw new IllegalArgumentException("incompatible exception type");
        }

        return exception;
    }

    public static Parameters ofMethod(Method method, boolean exceptionHandler) {
        List<HandlerParameter> parameters = new LinkedList<>();

        for (Parameter parameter : method.getParameters()) {
            Class<?> type = parameter.getType();

            if (type.equals(Context.class) || parameter.getAnnotation(Extract.class) != null) {
                parameters.add(new HandlerParameter(type, ParameterBehavior.EXTRACTOR));
            } else {
                if (exceptionHandler && Throwable.class.isAssignableFrom(type)) {
                    parameters.add(new HandlerParameter(type, ParameterBehavior.EXCEPTION));
                } else {
                    parameters.add(new HandlerParameter(type, ParameterBehavior.ARGUMENT));
                }
            }
        }

        return new Parameters(parameters);
    }

    @RequiredArgsConstructor
    private static class HandlerParameter {
        private final Class<?> type;
        private final ParameterBehavior behavior;
    }

    enum ParameterBehavior {
        ARGUMENT,
        EXTRACTOR,
        EXCEPTION,
    }
}
