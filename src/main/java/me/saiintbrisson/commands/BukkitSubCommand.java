package me.saiintbrisson.commands;

import lombok.Getter;

public class BukkitSubCommand extends BukkitCommand {

    @Getter
    private final BukkitCommand parent;

    public BukkitSubCommand(CommandFrame owner, String name, BukkitCommand parent) {
        super(owner, name, parent.getPosition() + 1);
        this.parent = parent;
    }

    @Override
    public String getFancyName() {
        return parent.getFancyName() + " " + getName();
    }

}
