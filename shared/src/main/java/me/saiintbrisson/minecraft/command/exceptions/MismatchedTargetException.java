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
public class MismatchedTargetException extends RuntimeException {
    private final SenderType target;
    private final SenderType sender;

    public MismatchedTargetException(SenderType target, SenderType sender) {
        super("Received " + sender + ", expected " + target);
        this.target = target;
        this.sender = sender;
    }

    public static class Handler<S> implements ExceptionHandler<S, MismatchedTargetException> {
        @Override
        public void handle(Context<S> ctx, MismatchedTargetException e) {
            if (!ctx.checkTarget(e.getTarget(), true)) {
                throw new UnsupportedOperationException();
            }

            String target;

            switch (e.getTarget()) {
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
    }
}
