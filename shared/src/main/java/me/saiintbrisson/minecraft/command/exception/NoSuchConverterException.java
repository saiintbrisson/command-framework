package me.saiintbrisson.minecraft.command.exception;

/**
 * @author SaiintBrisson
 */
public class NoSuchConverterException extends CommandException {
    public NoSuchConverterException(Class<?> type) {
        super("No converter found for type " + type.getTypeName());
    }
}
