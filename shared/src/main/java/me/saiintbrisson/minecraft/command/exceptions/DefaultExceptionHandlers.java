package me.saiintbrisson.minecraft.command.exceptions;

import me.saiintbrisson.minecraft.command.SenderType;
import me.saiintbrisson.minecraft.command.annotation.ExceptionHandler;
import me.saiintbrisson.minecraft.command.command.Context;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;

/**
 * @author Luiz Carlos Carvalho
 */
public class DefaultExceptionHandlers {
    @ExceptionHandler
    public void handleInsufficientPermissions(Context<?> ctx, InsufficientPermissionsException ex) {
        if (!ctx.checkTarget(SenderType.PLAYER, true)) {
            throw new UnsupportedOperationException();
        }

        ctx.send(new ComponentBuilder("You don't have access to this command (").color(ChatColor.RED)
          .append(ex.getPermission())
          .color(ChatColor.WHITE)
          .append(")")
          .color(ChatColor.RED)
          .create());
    }

    @ExceptionHandler
    public void handleMismatchedTarget(Context<?> ctx, MismatchedTargetException ex) {
        if (!ctx.checkTarget(ex.getTarget(), true)) {
            throw new UnsupportedOperationException();
        }

        String target;

        switch (ex.getTarget()) {
            case TERMINAL:
                target = "the terminal";
                break;
            case PLAYER:
                target = "players";
                break;
            default:
                throw new IllegalArgumentException();
        }

        ctx.send(new ComponentBuilder("This command is only available to ").color(ChatColor.RED)
          .append(target)
          .color(ChatColor.WHITE)
          .create());
    }

    @ExceptionHandler
    public void handleIncorrectUsage(Context<?> ctx, IncorrectUsageException ex) {
        ctx.send(new ComponentBuilder("Usage: /").color(ChatColor.RED)
          .append(ctx.getCommandInfo().usage())
          .create());
    }
}
