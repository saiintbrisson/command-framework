package me.saiintbrisson.minecraft.command.command;

import me.saiintbrisson.minecraft.command.executor.CommandExecutor;
import me.saiintbrisson.minecraft.command.executor.CompleterExecutor;
import me.saiintbrisson.minecraft.command.target.CommandTarget;

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

    String getPermission();
    String getUsage();
    String getDescription();

    String getFancyName();

}
