package me.saiintbrisson.bukkit.command.executor;

import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.exception.CommandException;
import me.saiintbrisson.minecraft.command.executor.CompleterExecutor;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author SaiintBrisson
 */

public class BukkitCompleterExecutor implements CompleterExecutor<CommandSender> {
    private final Method method;
    private final Object holder;

    public BukkitCompleterExecutor(Method method, Object holder) {
        final Class<?> returnType = method.getReturnType();
        final Class<?>[] parameters = method.getParameterTypes();

        if (!List.class.isAssignableFrom(returnType)) {
            throw new CommandException("Illegal return type, '" + method.getName());
        }

        if (parameters.length > 1 || (parameters.length == 1 && !Context.class.isAssignableFrom(parameters[0]))) {
            throw new CommandException("Illegal parameter type, '" + method.getName());
        }

        this.method = method;
        this.holder = holder;
    }

    @Override
    public List<String> execute(Context<CommandSender> context) {
        Class<?>[] types = method.getParameterTypes();

        try {
            if (types.length == 0) {
                return (List<String>) method.invoke(holder);
            } else if (types.length == 1 && types[0] == Context.class) {
                return (List<String>) method.invoke(holder, context);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
}
