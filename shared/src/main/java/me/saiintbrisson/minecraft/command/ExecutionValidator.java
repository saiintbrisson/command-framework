package me.saiintbrisson.minecraft.command;

import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.exceptions.InsufficientPermissionsException;
import me.saiintbrisson.minecraft.command.exceptions.MismatchedTargetException;

/**
 * @author Luiz Carlos Carvalho
 */
public interface ExecutionValidator<S> {
    void validate(CommandInfo commandInfo, S sender)
      throws InsufficientPermissionsException, MismatchedTargetException;
}
