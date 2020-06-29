package me.saiintbrisson.commands.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CommandException extends RuntimeException {

    public CommandException(String message) {
        super(message);
    }

}
