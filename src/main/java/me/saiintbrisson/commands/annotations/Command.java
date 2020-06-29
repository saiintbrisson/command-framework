package me.saiintbrisson.commands.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

    /**
     * Defines the command name, sub-commands are
     * separated with dots
     * <p>
     * <b>Example:</b><p>
     *   parentcommand<p>
     *   parentcommand.subcommand<p>
     *   parentcommand.childcommand.subcommand
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
     * @return the possible command options
     */
    String[] options() default {};

    /**
     * @return the command target
     */
    CommandTarget target() default CommandTarget.BOTH;

}
