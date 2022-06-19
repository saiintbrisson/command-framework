package me.saiintbrisson.minecraft.command.parameter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.saiintbrisson.minecraft.command.annotation.Extract;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.exceptions.NoSuchAdapterException;
import me.saiintbrisson.minecraft.command.parameter.interfaces.Extractor;
import me.saiintbrisson.minecraft.command.parameter.interfaces.TypeAdapter;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Luiz Carlos Carvalho
 */
@RequiredArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public final class Parameters {
    private final Collection<HandlerParameter> parameters;

    public Collection<Object> extractParameters(Context<?> ctx) {
        Iterator<String> inputs = ctx.getInputs().values().iterator();

        return parameters.stream().map(parameter -> {
            if (parameter instanceof ArgumentParameter) {
                if (!inputs.hasNext()) {
                    throw new IllegalArgumentException("received less arguments than needed");
                }

                return parseArgument((ArgumentParameter) parameter, ctx, inputs.next());
            } else if (parameter instanceof ExtractorParameter) {
                return instantiateExtractor((ExtractorParameter) parameter, ctx);
            }

            throw new UnsupportedOperationException("unknown parameter type");
        }).collect(Collectors.toList());
    }

    public Object instantiateExtractor(ExtractorParameter parameter, Context<?> ctx) {
        Extractor<?> extractor = ctx.getCommandFrame().getExtractorMap().get(parameter.getType());
        if (extractor == null) {
            throw new NoSuchAdapterException(parameter.getType());
        }

        return extractor.extract(ctx);
    }

    public Object parseArgument(ArgumentParameter parameter, Context<?> ctx, String input) {
        TypeAdapter<?> adapter = ctx.getCommandFrame().getAdapterMap().get(parameter.getType());
        if (adapter == null) {
            throw new NoSuchAdapterException(parameter.getType());
        }

        return adapter.convertNonNull(input);
    }

    public static Parameters ofMethod(Method method) {
        List<HandlerParameter> parameters = new LinkedList<>();

        for (Parameter parameter : method.getParameters()) {
            Class<?> type = parameter.getType();

            if (type.equals(Context.class) || parameter.getAnnotation(Extract.class) != null) {
                parameters.add(new ExtractorParameter(type));
            } else {
                parameters.add(new ArgumentParameter(type));
            }
        }

        return new Parameters(parameters);
    }

    private interface HandlerParameter {
        Class<?> getType();
    }

    @Getter
    @RequiredArgsConstructor
    private static class ArgumentParameter implements HandlerParameter {
        private final Class<?> type;
    }

    @Getter
    @RequiredArgsConstructor
    private static class ExtractorParameter implements HandlerParameter {
        private final Class<?> type;
    }
}
