package me.saiintbrisson.bungee.command.target;

import me.saiintbrisson.minecraft.command.target.CommandTarget;
import me.saiintbrisson.minecraft.command.target.TargetValidator;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * @author Henry Fábio
 * Github: https://github.com/HenryFabio
 */
public class BungeeTargetValidator implements TargetValidator {
    public static final BungeeTargetValidator INSTANCE = new BungeeTargetValidator();

    @Override
    public boolean validate(CommandTarget target, Object object) {
        if (target == CommandTarget.CONSOLE) {
            return !(object instanceof ProxiedPlayer);
        }

        if (target == CommandTarget.PLAYER) {
            return object instanceof ProxiedPlayer;
        }

        return true;
    }

    @Override
    public CommandTarget fromSender(Object object) {
        if (object instanceof ProxiedPlayer) {
            return CommandTarget.PLAYER;
        }

        if (object instanceof CommandSender) {
            return CommandTarget.CONSOLE;
        }

        return CommandTarget.ALL;
    }
}
