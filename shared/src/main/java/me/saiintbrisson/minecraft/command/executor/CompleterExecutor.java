package me.saiintbrisson.minecraft.command.executor;

import me.saiintbrisson.minecraft.command.command.Context;

import java.util.List;

/**
 * @author SaiintBrisson
 */

@FunctionalInterface
public interface CompleterExecutor<S> {

    List<String> execute(Context<S> context);

}
