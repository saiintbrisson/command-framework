package me.saiintbrisson.minecraft.command;

import me.saiintbrisson.minecraft.command.exceptions.InsufficientPermissionsException;
import me.saiintbrisson.minecraft.command.exceptions.MismatchedTargetException;
import me.saiintbrisson.minecraft.command.path.PathInfo;

/**
 * @author Luiz Carlos Carvalho
 */
public interface ExecutionValidator<S> {
    void validate(PathInfo pathInfo, S sender)
      throws InsufficientPermissionsException, MismatchedTargetException;
}
