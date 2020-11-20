package me.saiintbrisson.minecraft.command;

import lombok.RequiredArgsConstructor;
import me.saiintbrisson.minecraft.command.command.CommandHolder;

import java.util.Iterator;

/**
 * @author SaiintBrisson
 */

@RequiredArgsConstructor
public class CommandInfoIterator implements Iterator<CommandHolder<?, ?>> {
    private final CommandHolder<?, ?> root;

    private int index = -1;
    private CommandInfoIterator current;

    @Override
    public boolean hasNext() {
        return (current != null && current.hasNext()) || index < root.getChildCommandList().size();
    }

    @Override
    public CommandHolder<?, ?> next() {
        if (index == -1) {
            index++;
            return root;
        }

        if (current == null || !current.hasNext()) {
            current = new CommandInfoIterator(root.getChildCommandList().get(index));
            index++;
        }

        return current.next();
    }
}
