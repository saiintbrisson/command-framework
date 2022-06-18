package me.saiintbrisson.minecraft.command.exceptions;

import lombok.Getter;
import me.saiintbrisson.minecraft.command.SenderType;

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
}
