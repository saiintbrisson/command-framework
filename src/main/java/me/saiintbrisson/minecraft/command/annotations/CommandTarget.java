package me.saiintbrisson.minecraft.command.annotations;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public enum CommandTarget {

    BOTH {
        @Override
        public boolean validate(CommandSender sender) {
            return true;
        }
    },
    PLAYER {
        @Override
        public boolean validate(CommandSender sender) {
            return sender instanceof Player;
        }
    },
    CONSOLE {
        @Override
        public boolean validate(CommandSender sender) {
            return sender instanceof ConsoleCommandSender;
        }
    };

    public abstract boolean validate(CommandSender sender);

}
