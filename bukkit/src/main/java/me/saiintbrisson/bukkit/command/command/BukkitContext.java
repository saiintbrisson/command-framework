package me.saiintbrisson.bukkit.command.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.saiintbrisson.bukkit.command.target.BukkitTargetValidator;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.exception.CommandException;
import me.saiintbrisson.minecraft.command.message.MessageType;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.bukkit.command.CommandSender;

/**
 * @author SaiintBrisson
 */

@AllArgsConstructor
public class BukkitContext implements Context<CommandSender> {

    @Getter
    private final String label;

    @Getter
    private final CommandSender sender;

    @Getter
    private final CommandTarget target;

    @Getter
    private final String[] args;

    @Override
    public int argsCount() {
        return args.length;
    }

    @Override
    public String getArg(int index) {
        try {
            return args[index];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public void sendMessage(String message) {
        sender.sendMessage(message);
    }

    @Override
    public void sendMessage(String[] messages) {
        sender.sendMessage(messages);
    }

    @Override
    public boolean testPermission(String permission, boolean silent) throws CommandException {
        if (sender.hasPermission(permission)) {
            return true;
        }

        if (!silent) {
            throw new CommandException(MessageType.NO_PERMISSION, permission);
        }

        return false;
    }

    @Override
    public boolean testTarget(CommandTarget target, boolean silent) throws CommandException {
        if (BukkitTargetValidator.INSTANCE.validate(target, sender)) {
            return true;
        }

        if (!silent) {
            throw new CommandException(MessageType.INCORRECT_USAGE, target.name());
        }

        return false;
    }

}
