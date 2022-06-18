package me.saiintbrisson.minecraft.command.exceptions;

import lombok.Getter;

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
}
