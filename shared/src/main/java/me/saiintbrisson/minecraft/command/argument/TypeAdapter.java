package me.saiintbrisson.minecraft.command.argument;

/**
 * @author SaiintBrisson
 */
public interface TypeAdapter<T> {

    T convert(String raw);

    default T convertNonNull(String raw) {
        final T result = convert(raw);

        if (result == null) {
            throw new NullPointerException();
        }

        return result;
    }

}
