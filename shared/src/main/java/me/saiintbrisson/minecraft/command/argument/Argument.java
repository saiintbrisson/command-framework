package me.saiintbrisson.minecraft.command.argument;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * @author SaiintBrisson
 */

@Getter
@Builder
public class Argument<T> {

    private final String name;

    private final Class<T> type;
    private final TypeAdapter<T> adapter;

    private final T defaultValue;

    private final boolean isNullable;
    private final boolean isArray;

    public Argument(@NonNull String name,
                    @NonNull Class<T> type, TypeAdapter<T> adapter,
                    T defaultValue,
                    boolean isNullable, boolean isArray) {
        this.name = name;
        this.type = type;
        this.adapter = adapter;
        this.defaultValue = defaultValue;
        this.isNullable = isNullable;
        this.isArray = isArray;
    }

}
