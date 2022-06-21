package me.saiintbrisson.minecraft.command.exceptions;

import lombok.Getter;
import me.saiintbrisson.minecraft.command.SenderType;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.handlers.ExceptionHandler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;

/**
 * @author Luiz Carlos Carvalho
 */
@Getter
public class InsufficientPermissionsException extends RuntimeException {
    private final String permission;

    public InsufficientPermissionsException(String permission) {
        super("Insufficient permissions, requires " + permission);
        this.permission = permission;
    }

    public static class Handler<S> implements ExceptionHandler<S, InsufficientPermissionsException> {
        @Override
        public void handle(Context<S> ctx, InsufficientPermissionsException e) {
            if (!ctx.checkTarget(SenderType.PLAYER, true)) {
                throw new UnsupportedOperationException();
            }

            ctx.send(new ComponentBuilder("You don't have access to this command (").color(ChatColor.RED)
              .append(e.getPermission())
              .color(ChatColor.WHITE)
              .append(")")
              .color(ChatColor.RED)
              .create());
        }
    }
}
