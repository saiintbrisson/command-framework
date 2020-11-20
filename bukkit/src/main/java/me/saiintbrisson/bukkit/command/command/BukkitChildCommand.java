package me.saiintbrisson.bukkit.command.command;

import me.saiintbrisson.bukkit.command.BukkitFrame;
import me.saiintbrisson.minecraft.command.command.CommandHolder;

import java.util.Optional;

/**
 * @author SaiintBrisson
 */
public class BukkitChildCommand extends BukkitCommand {
    private final BukkitCommand parentCommand;

    public BukkitChildCommand(BukkitFrame frame, String name, BukkitCommand parentCommand) {
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
