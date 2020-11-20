package me.saiintbrisson.minecraft.command.command;

import me.saiintbrisson.minecraft.command.CommandInfoIterator;
import me.saiintbrisson.minecraft.command.executor.CommandExecutor;
import me.saiintbrisson.minecraft.command.executor.CompleterExecutor;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * @author SaiintBrisson
 */
public interface CommandHolder<S, C extends CommandHolder<S, C>> extends Iterable<CommandHolder<?, ?>> {
    int getPosition();

    CommandExecutor<S> getCommandExecutor();
    CompleterExecutor<S> getCompleterExecutor();

    default Optional<CommandHolder<?, ?>> getParentCommand() {
        return Optional.empty();
    }
    List<C> getChildCommandList();
    C getChildCommand(String name);

    CommandInfo getCommandInfo();

    String getName();
    String getFancyName();
    List<String> getAliasesList();

    String getPermission();
    String getUsage();
    String getDescription();

    default boolean equals(String name) {
        if (getName().equalsIgnoreCase(name)) {
            return true;
        }

        for (String alias : getAliasesList()) {
            if (alias.equalsIgnoreCase(name)) return true;
        }

        return false;
    }

    @NotNull
    @Override
    default Iterator<CommandHolder<?, ?>> iterator() {
        return new CommandInfoIterator(this);
    }
}
