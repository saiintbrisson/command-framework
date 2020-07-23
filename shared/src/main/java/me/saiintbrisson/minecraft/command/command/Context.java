package me.saiintbrisson.minecraft.command.command;

import me.saiintbrisson.minecraft.command.exception.CommandException;
import me.saiintbrisson.minecraft.command.target.CommandTarget;

import java.util.Arrays;

/**
 * @author SaiintBrisson
 */
public interface Context<S> {

    /**
     *
     * @return the label used to execute the command
     */
    String getLabel();

    /**
     *
     * @return who sent the command
     */
    S getSender();

    /**
     *
     * @return the executor type
     */
    CommandTarget getTarget();



    /**
     *
     * @return the number of arguments
     */
    int argsCount();

    /**
     *
     * @param index the index of the argument
     * @return the argument - null if the index is out of bounds
     */
    String getArg(int index);

    /**
     *
     * @return the arguments array
     */
    String[] getArgs();

    /**
     * Gets all args between indexes from and to
     * @param from defines the start of the array relative to the arguments, inclusive
     * @param to defines the end of the array relative to the arguments, exclusive
     * @return the arguments array - null if the indexes are out of bounds
     */
    default String[] getArgs(int from, int to) {
        try {
            return Arrays.copyOfRange(getArgs(), from, to);
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }



    /**
     * Sends a message to the executor
     * @param message the message to be sent
     */
    void sendMessage(String message);

    /**
     * Sends a message formatting it with the String#format() method
     * @param message the message to be sent
     * @param objects the objects to be inserted
     */
    default void sendMessage(String message, Object... objects) {
        sendMessage(String.format(message, objects));
    }



    /**
     * Tests whether the executor has a permission
     * @param permission the permission to be tested
     * @param silent whether a exception should be thrown
     * @return the test result if silent
     */
    boolean testPermission(String permission, boolean silent) throws CommandException;

    /**
     * Tests whether the executor is a target
     * @param target the target to be tested
     * @param silent whether a exception should be thrown
     * @return the test result if silent
     */
    boolean testTarget(CommandTarget target, boolean silent) throws CommandException;

}
