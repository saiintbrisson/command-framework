package me.saiintbrisson.minecraft.command.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.saiintbrisson.minecraft.command.message.MessageType;

/**
 * @author SaiintBrisson
 */

@NoArgsConstructor
public class CommandException extends RuntimeException {
    @Getter
    private MessageType messageType;

    public CommandException(MessageType messageType, String message) {
        super(message);
        this.messageType = messageType;
    }

    public CommandException(Throwable cause) {
        super(cause);
    }

    public CommandException(String message) {
        super(message);
    }
}
