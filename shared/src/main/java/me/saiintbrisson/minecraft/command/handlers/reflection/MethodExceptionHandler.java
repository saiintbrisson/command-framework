package me.saiintbrisson.minecraft.command.handlers.reflection;

import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.handlers.ExceptionHandler;
import me.saiintbrisson.minecraft.command.parameter.Parameters;
import net.md_5.bungee.api.chat.BaseComponent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * @author Luiz Carlos Carvalho
 */
@AllArgsConstructor
public class MethodExceptionHandler<S, E extends Throwable> implements ExceptionHandler<S, E> {
    private final Object instance;
    private final Method method;
    private final Parameters parameters;

    @Override
    public void handle(Context<S> context, E exception) {
        try {
            Collection<Object> parameters = this.parameters.extractParameters(context, exception);
            Object result = method.invoke(instance, parameters.toArray());

            evalReturn(context, result);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    private void evalReturn(Context<?> context, Object result) {
        if (result instanceof String) {
            context.send((String) result);
        } else if (result instanceof String[]) {
            context.send((String[]) result);
        } else if (result instanceof BaseComponent) {
            context.send((BaseComponent) result);
        } else if (result instanceof BaseComponent[]) {
            context.send((BaseComponent[]) result);
        }
    }
}
