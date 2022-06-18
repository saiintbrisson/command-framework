package me.saiintbrisson.bukkit.command;

import me.saiintbrisson.minecraft.command.ExecutionValidator;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.exceptions.InsufficientPermissionsException;
import me.saiintbrisson.minecraft.command.exceptions.MismatchedTargetException;
import me.saiintbrisson.minecraft.command.SenderType;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 * @author Luiz Carlos Carvalho
 */
final class BukkitExecutionValidator implements ExecutionValidator<CommandSender> {
    @Override
    public void validate(CommandInfo commandInfo, CommandSender sender)
      throws InsufficientPermissionsException, MismatchedTargetException {
        if (!commandInfo.permission().isEmpty() && !sender.hasPermission(commandInfo.permission())) {
            throw new InsufficientPermissionsException(commandInfo.permission());
        }

        SenderType target = commandInfo.target();
        SenderType senderType = fromSender(sender);
        if (target != SenderType.ANY && senderType != target) {
            throw new MismatchedTargetException(target, senderType);
        }
    }

    private SenderType fromSender(Object object) {
        if (object instanceof Player) {
            return SenderType.PLAYER;
        } else if (object instanceof ConsoleCommandSender) {
            return SenderType.TERMINAL;
        } else

        return SenderType.ANY;
    }
}
