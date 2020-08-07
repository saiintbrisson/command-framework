package me.saiintbrisson.minecraft.command;

import me.saiintbrisson.minecraft.command.argument.AdapterMap;
import me.saiintbrisson.minecraft.command.argument.TypeAdapter;
import me.saiintbrisson.minecraft.command.command.CommandHolder;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.executor.CommandExecutor;
import me.saiintbrisson.minecraft.command.executor.CompleterExecutor;
import me.saiintbrisson.minecraft.command.message.MessageHolder;

import java.util.Map;

/**
 * @author SaiintBrisson
 */
public interface CommandFrame<P, S, C extends CommandHolder<S, ? extends C>> {

    P getPlugin();

    AdapterMap getAdapterMap();

    MessageHolder getMessageHolder();

    Map<String, C> getCommandMap();

    C getCommand(String name);

    default <T> void registerAdapter(Class<T> type, TypeAdapter<T> adapter) {
        getAdapterMap().put(type, adapter);
    }

    void registerCommands(Object... objects);

    void registerCommand(CommandInfo commandInfo, CommandExecutor<S> commandExecutor);

    void registerCompleter(String name, CompleterExecutor<S> completerExecutor);

}
