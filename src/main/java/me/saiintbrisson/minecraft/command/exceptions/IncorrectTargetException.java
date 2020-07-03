package me.saiintbrisson.minecraft.command.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.saiintbrisson.minecraft.command.annotations.CommandTarget;

@AllArgsConstructor
public class IncorrectTargetException extends CommandException {

    @Getter
    private final CommandTarget target;

}
