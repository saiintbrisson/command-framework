package me.saiintbrisson.minecraft.command.command;

import me.saiintbrisson.minecraft.command.executor.CommandExecutor;
import me.saiintbrisson.minecraft.command.executor.CompleterExecutor;

import java.util.List;

/**
 * @author SaiintBrisson
 */
public interface CommandHolder<S, C extends CommandHolder<S, C>> {
    CommandInfo getCommandInfo();

    int getPosition();

    CommandExecutor<S> getCommandExecutor();

    CompleterExecutor<S> getCompleterExecutor();

    List<C> getChildCommandList();

    C getChildCommand(String name);

    String getName();

    List<String> getAliasesList();

    String getPermission();

    String getUsage();

    String getDescription();

    String getFancyName();

    default boolean equals(String name) {
        if (getName().equalsIgnoreCase(name)) {
            return true;
        }

        for (String alias : getAliasesList()) {
            if (alias.equalsIgnoreCase(name)) return true;
        }

        return false;
    }
}
