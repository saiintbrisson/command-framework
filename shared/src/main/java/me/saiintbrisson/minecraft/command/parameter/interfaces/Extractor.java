package me.saiintbrisson.minecraft.command.parameter.interfaces;

import me.saiintbrisson.minecraft.command.command.Context;
import org.jetbrains.annotations.NotNull;

/**
 * @author Luiz Carlos Carvalho
 */
@FunctionalInterface
public interface Extractor<T> {
    T extract(@NotNull Context<?> ctx);
}
