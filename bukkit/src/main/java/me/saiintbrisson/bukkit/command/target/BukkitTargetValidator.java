package me.saiintbrisson.bukkit.command.target;

import me.saiintbrisson.minecraft.command.target.CommandTarget;
import me.saiintbrisson.minecraft.command.target.TargetValidator;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 * @author SaiintBrisson
 */
public class BukkitTargetValidator implements TargetValidator {

    public static final BukkitTargetValidator INSTANCE = new BukkitTargetValidator();

    @Override
    public boolean validate(CommandTarget target, Object object) {
        if (target == CommandTarget.CONSOLE) {
            return object instanceof ConsoleCommandSender;
        }

        if (target == CommandTarget.PLAYER) {
            return object instanceof Player;
        }

        return true;
    }

    @Override
    public CommandTarget fromSender(Object object) {
        if (object instanceof Player) {
            return CommandTarget.PLAYER;
        }

        if (object instanceof ConsoleCommandSender) {
            return CommandTarget.CONSOLE;
        }

        return CommandTarget.ALL;
    }

}
