package me.saiintbrisson.minecraft.command.annotation;

import me.saiintbrisson.minecraft.command.target.CommandTarget;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author SaiintBrisson
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

    /**
     * Defines the command name, sub-commands are split with dots
     * <p><p>
     * <b>Example:</b><p>
     *   parentcommand<p>
     *   parentcommand.subcommand<p>
     *
     * @return the command name
     */
    String name();

    /**
     * @return the command aliases
     */
    String[] aliases() default {};

    /**
     * @return the command description
     */
    String description() default "";

    /**
     * @return the command usage example
     */
    String usage() default "";

    /**
     * @return the required permission to execute the command
     */
    String permission() default "";

    /**
     * @return the command target
     */
    CommandTarget target() default CommandTarget.ALL;

}
