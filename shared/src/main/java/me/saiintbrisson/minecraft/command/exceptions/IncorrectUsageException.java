package me.saiintbrisson.minecraft.command.exceptions;

import lombok.Getter;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.handlers.ExceptionHandler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;

/**
 * Thrown when the user failed to provide all
 * necessary arguments.
 *
 * @author Luiz Carlos Carvalho
 */
@Getter
public class IncorrectUsageException extends RuntimeException {
    private final String name;

    public IncorrectUsageException(String name) {
        super("Incorrect usage on " + name);
        this.name = name;
    }

    public static class Handler<S> implements ExceptionHandler<S, IncorrectUsageException> {
        @Override
        public void handle(Context<S> ctx, IncorrectUsageException exception) {
            ctx.send(new ComponentBuilder("Usage: /").color(ChatColor.RED)
              .append(ctx.getPathInfo().usage())
              .create());
        }
    }
}
