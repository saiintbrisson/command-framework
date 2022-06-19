package me.saiintbrisson.minecraft.command.exceptions;

import lombok.Getter;

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
}
