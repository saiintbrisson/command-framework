package me.saiintbrisson.minecraft.command.handlers.reflection;

import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.CommandExecutor;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.handlers.CommandHandler;
import me.saiintbrisson.minecraft.command.parameter.Parameters;
import net.md_5.bungee.api.chat.BaseComponent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author Luiz Carlos Carvalho
 */
@AllArgsConstructor
public class MethodCommandHandler<S> implements CommandHandler<S> {
    private final Object instance;
    private final Method method;
    private final Parameters parameters;

    @Override
    public HandlerResponse handle(Context<S> context) {
        try {
            Collection<?> parameters = this.parameters.extractParameters(context, null);
            Object result = method.invoke(instance, parameters.toArray());

            return evalReturn(context, result);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            return HandlerResponse.error(e);
        }
    }

    private HandlerResponse evalReturn(Context<?> context, Object result) {
        if (result == null) {
            return HandlerResponse.error();
        } else if (result.getClass().equals(Boolean.TYPE)) {
            return (boolean) result ? HandlerResponse.success() : HandlerResponse.error();
        } else if (result instanceof String) {
            context.send((String) result);
        } else if (result instanceof String[]) {
            context.send((String[]) result);
        } else if (result instanceof BaseComponent) {
            context.send((BaseComponent) result);
        } else if (result instanceof BaseComponent[]) {
            context.send((BaseComponent[]) result);
        } else if (result instanceof HandlerResponse) {
            return ((HandlerResponse) result);
        }

        return HandlerResponse.success();
    }
}
