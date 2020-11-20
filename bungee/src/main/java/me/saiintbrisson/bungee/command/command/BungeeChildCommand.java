package me.saiintbrisson.bungee.command.command;

import me.saiintbrisson.bungee.command.BungeeFrame;
import me.saiintbrisson.minecraft.command.command.CommandHolder;

import java.util.Optional;

/**
 * @author Henry Fábio
 * Github: https://github.com/HenryFabio
 */
public class BungeeChildCommand extends BungeeCommand {
    private final BungeeCommand parentCommand;

    public BungeeChildCommand(BungeeFrame frame, String name, BungeeCommand parentCommand) {
        super(frame, name, parentCommand.getPosition() + 1);
        this.parentCommand = parentCommand;
    }

    @Override
    public String getFancyName() {
        return parentCommand.getFancyName() + " " + getName();
    }

    public Optional<CommandHolder<?, ?>> getParentCommand() {
        return Optional.of(parentCommand);
    }
}
