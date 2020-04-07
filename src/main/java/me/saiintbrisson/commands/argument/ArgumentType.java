package me.saiintbrisson.commands.argument;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ArgumentType<T> {

    private ArgumentValidationRule<T> rule;
    private Class<T> clazz;
}
