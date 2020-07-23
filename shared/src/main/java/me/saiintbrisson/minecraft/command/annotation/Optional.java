package me.saiintbrisson.minecraft.command.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allows a argument to be optional, nullable if primitive
 * @author SaiintBrisson
 */

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Optional {

    /**
     * Required if the argument type is a primitive
     * @return the default value for a argument
     */
    String[] def() default {};

}
