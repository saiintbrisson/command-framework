package me.saiintbrisson.minecraft.command.argument;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ArgumentType<T> {

    private ArgumentValidationRule<T> rule;
    private Class<T> type;

}
