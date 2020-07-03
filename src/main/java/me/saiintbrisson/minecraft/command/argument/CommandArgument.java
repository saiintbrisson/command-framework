package me.saiintbrisson.minecraft.command.argument;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommandArgument<T> {

    private final ArgumentType<T> type;

    private final boolean array;

    private final T defaultValue;
    private final boolean nullable;

    public Class<T> getClassType() {
        return type.getType();
    }

}
