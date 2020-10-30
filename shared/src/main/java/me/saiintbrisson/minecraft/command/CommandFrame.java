package me.saiintbrisson.minecraft.command;

import me.saiintbrisson.minecraft.command.argument.AdapterMap;
import me.saiintbrisson.minecraft.command.argument.TypeAdapter;
import me.saiintbrisson.minecraft.command.argument.eval.MethodEvaluator;
import me.saiintbrisson.minecraft.command.command.CommandHolder;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.executor.CommandExecutor;
import me.saiintbrisson.minecraft.command.executor.CompleterExecutor;
import me.saiintbrisson.minecraft.command.message.MessageHolder;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.Executor;

/**
 * @author SaiintBrisson
 */
public interface CommandFrame<P, S, C extends CommandHolder<S, ? extends C>> {
    P getPlugin();
    AdapterMap getAdapterMap();
    MessageHolder getMessageHolder();

    Map<String, C> getCommandMap();
    MethodEvaluator getMethodEvaluator();

    @Nullable
    Executor getExecutor();

    C getCommand(String name);

    default <T> void registerAdapter(Class<T> type, TypeAdapter<T> adapter) {
        getAdapterMap().put(type, adapter);
    }

    void registerCommands(Object... objects);
    void registerCommand(CommandInfo commandInfo, CommandExecutor<S> commandExecutor);
    void registerCompleter(String name, CompleterExecutor<S> completerExecutor);

    boolean unregisterCommand(String name);
}
