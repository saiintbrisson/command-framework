package me.saiintbrisson.minecraft.command.exception;

import lombok.Getter;
import me.saiintbrisson.minecraft.command.target.CommandTarget;

/**
 * @author Luiz Carlos Carvalho
 */
@Getter
public class MismatchedTargetException extends RuntimeException {
    private final CommandTarget target;
    private final CommandTarget sender;

    public MismatchedTargetException(CommandTarget target, CommandTarget sender) {
        super("Received " + sender + ", expected " + target);
        this.target = target;
        this.sender = sender;
    }
}
