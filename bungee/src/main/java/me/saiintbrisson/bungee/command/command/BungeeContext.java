package me.saiintbrisson.bungee.command.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.saiintbrisson.bungee.command.target.BungeeTargetValidator;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.exception.CommandException;
import me.saiintbrisson.minecraft.command.message.MessageType;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * @author Henry Fábio
 * Github: https://github.com/HenryFabio
 */
@Getter
@AllArgsConstructor
public class BungeeContext implements Context<CommandSender> {
    private final CommandSender sender;
    private final CommandTarget target;
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
        sender.sendMessage(new TextComponent(message));
    }

    @Override
    public void sendMessage(String[] messages) {
        for (String message : messages) {
            sendMessage(message);
        }
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
        if (BungeeTargetValidator.INSTANCE.validate(target, sender)) {
            return true;
        }

        if (!silent) {
            throw new CommandException(MessageType.INCORRECT_USAGE, target.name());
        }

        return false;
    }

    @Override
    public String getLabel() {
        throw new UnsupportedOperationException();
    }
}
