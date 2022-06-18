package me.saiintbrisson.minecraft.command.handlers.reflection;

import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.handlers.CompleterHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Luiz Carlos Carvalho
 */
@AllArgsConstructor
public class MethodCompleterHandler implements CompleterHandler<Object> {
    private final Object instance;
    private final Method method;

    @Override
    public List<String> handle(Context<Object> context) {
        try {
            Object result = method.invoke(instance);

            if (!(result instanceof List)) {
                return null;
            }

            return (List<String>) result;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            return null;
        }
    }
}
