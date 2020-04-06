package me.saiintbrisson.commands.argument;

public interface ArgumentType<T> {

    ArgumentValidationRule<T> rule();

    Class<T> getClazz();
}
