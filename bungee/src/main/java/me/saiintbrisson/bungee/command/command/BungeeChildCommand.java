package me.saiintbrisson.bungee.command.command;

import lombok.Getter;
import me.saiintbrisson.bungee.command.BungeeFrame;

/**
 * @author Henry Fábio
 * Github: https://github.com/HenryFabio
 */
public class BungeeChildCommand extends BungeeCommand {
    @Getter
    private final BungeeCommand parent;

    public BungeeChildCommand(BungeeFrame frame, String name, BungeeCommand parent) {
        super(frame, name, parent.getPosition() + 1);
        this.parent = parent;
    }

    @Override
    public String getFancyName() {
        return parent.getFancyName() + " " + getName();
    }
}
