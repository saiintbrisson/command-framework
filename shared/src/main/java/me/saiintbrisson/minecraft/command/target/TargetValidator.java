package me.saiintbrisson.minecraft.command.target;

/**
 * @author SaiintBrisson
 */
public interface TargetValidator {

    boolean validate(CommandTarget target, Object object);
    CommandTarget fromSender(Object object);

}
