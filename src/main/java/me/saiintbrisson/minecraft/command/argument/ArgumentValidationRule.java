package me.saiintbrisson.minecraft.command.argument;

public interface ArgumentValidationRule<T> {

    T validate(String argument) throws Exception;

    default T validateNonNull(String argument) throws Exception {
        T parse = validate(argument);

        if(parse == null) {
            throw new IllegalArgumentException("Argument may not be null");
        }

        return parse;
    }
}
