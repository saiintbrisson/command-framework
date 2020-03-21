package me.saiintbrisson.commands.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

    String name();
    String[] aliases() default {};

    String description() default "";
    String usage() default "";
    String permission() default "";

    String[] options() default {};

    boolean async() default false;
    boolean inGameOnly() default false;

}
