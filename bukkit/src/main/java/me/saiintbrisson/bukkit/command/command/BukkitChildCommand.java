package me.saiintbrisson.bukkit.command.command;

import lombok.Getter;
import me.saiintbrisson.bukkit.command.BukkitFrame;

/**
 * @author SaiintBrisson
 */
public class BukkitChildCommand extends BukkitCommand {
    @Getter
    private final BukkitCommand parent;

    public BukkitChildCommand(BukkitFrame frame, String name, BukkitCommand parent) {
        super(frame, name, parent.getPosition() + 1);

        this.parent = parent;
    }

    @Override
    public String getFancyName() {
        return parent.getFancyName() + " " + getName();
    }
}
