package me.saiintbrisson.bukkit.command;

import me.saiintbrisson.minecraft.command.ExecutionValidator;
import me.saiintbrisson.minecraft.command.exceptions.InsufficientPermissionsException;
import me.saiintbrisson.minecraft.command.exceptions.MismatchedTargetException;
import me.saiintbrisson.minecraft.command.SenderType;
import me.saiintbrisson.minecraft.command.path.PathInfo;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 * @author Luiz Carlos Carvalho
 */
final class BukkitExecutionValidator implements ExecutionValidator<CommandSender> {
    @Override
    public void validate(PathInfo pathInfo, CommandSender sender)
      throws InsufficientPermissionsException, MismatchedTargetException {
        if (!pathInfo.permission().isEmpty() && !sender.hasPermission(pathInfo.permission())) {
            throw new InsufficientPermissionsException(pathInfo.permission());
        }

        SenderType target = pathInfo.target();
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
