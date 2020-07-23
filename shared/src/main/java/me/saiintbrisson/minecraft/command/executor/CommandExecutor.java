package me.saiintbrisson.minecraft.command.executor;

import me.saiintbrisson.minecraft.command.command.Context;

/**
 * @author SaiintBrisson
 */

@FunctionalInterface
public interface CommandExecutor<S> {

    boolean execute(Context<S> context);

}
