package me.saiintbrisson.bukkit.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.saiintbrisson.minecraft.command.CommandFrame;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.exceptions.InsufficientPermissionsException;
import me.saiintbrisson.minecraft.command.SenderType;
import me.saiintbrisson.minecraft.command.path.PathInfo;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * @author Luiz Carlos Carvalho
 */

@Getter
@AllArgsConstructor
final class BukkitContext implements Context<CommandSender> {
    private final CommandFrame<?> commandFrame;
    private final PathInfo pathInfo;

    private final String label;
    private final CommandSender sender;
    private final String[] args;
    private final Map<String, String> inputs;

    @Override
    public SenderType getSenderType() {
        if (sender instanceof Player) {
            return SenderType.PLAYER;
        }

        return SenderType.TERMINAL;
    }

    @Override
    public void send(String message) {
        sender.sendMessage(message);
    }

    @Override
    public void send(String[] messages) {
        sender.sendMessage(messages);
    }

    @Override
    public void send(BaseComponent component) {
        sender.spigot().sendMessage(component);
    }

    @Override
    public void send(BaseComponent[] components) {
        sender.spigot().sendMessage(components);
    }

    @Override
    public boolean checkPermission(String permission, boolean silent) throws InsufficientPermissionsException {
        boolean hasPermission = getSender().hasPermission(permission);
        if (!silent && !hasPermission) throw new InsufficientPermissionsException(permission);

        return hasPermission;
    }
}
