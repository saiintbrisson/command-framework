package me.saiintbrisson.commands.argument;

public interface ArgumentValidationRule<T> {

    T parse(String argument) throws Exception;

    default T parseNonNull(String argument) throws Exception {
        T parse = parse(argument);

        if(parse != null) return parse;
        else throw new NullPointerException();
    }
}
