package me.saiintbrisson.commands.exceptions;

public class NoRegisteredConverterException extends RuntimeException {

    public NoRegisteredConverterException(String message) {
        super(message);
    }

}
