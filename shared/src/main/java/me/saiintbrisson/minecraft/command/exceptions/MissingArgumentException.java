package me.saiintbrisson.minecraft.command.exceptions;

import lombok.Getter;

/**
 * Thrown when the user failed to provide all
 * necessary arguments.
 *
 * @author Luiz Carlos Carvalho
 */
@Getter
public class MissingArgumentException extends RuntimeException {
    private final String name;

    public MissingArgumentException(String name) {
        super("Missing argument " + name);
        this.name = name;
    }
}
