package me.saiintbrisson.commands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Getter
@Builder
@AllArgsConstructor
public class Execution {

    private CommandSender sender;
    private String label;
    private String[] args;

    public String getArg(int index) {
        try {
            return args[index];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public int argsCount() {
        return args.length;
    }

    public boolean isPlayer() {
        return sender instanceof Player;
    }

    public Player getPlayer() {
        return isPlayer() ? (Player) sender : null;
    }

    public void sendMessage(String message) {
        sender.sendMessage(message);
    }

}
