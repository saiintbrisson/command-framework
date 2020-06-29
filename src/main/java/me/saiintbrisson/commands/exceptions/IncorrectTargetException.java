package me.saiintbrisson.commands.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.saiintbrisson.commands.annotations.CommandTarget;

@AllArgsConstructor
public class IncorrectTargetException extends CommandException {

    @Getter
    private final CommandTarget target;

}
