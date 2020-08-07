package me.saiintbrisson.minecraft.command.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.saiintbrisson.minecraft.command.command.CommandHolder;

/**
 * @author SaiintBrisson
 */

@Getter
@AllArgsConstructor
public enum MessageType {

    ERROR("{error}",
      "§cAn error has been thrown: §f{error}§c.") {
        @Override
        public String getDefault(CommandHolder<?, ?> commandHolder) {
            return "";
        }
    },
    NO_PERMISSION("{permission}",
      "§cRequired permission: §f{permission}§c.") {
        @Override
        public String getDefault(CommandHolder<?, ?> commandHolder) {
            return commandHolder.getPermission();
        }
    },
    INCORRECT_USAGE("{usage}",
      "§cCorrect usage: §e/{usage}§c.") {
        @Override
        public String getDefault(CommandHolder<?, ?> commandHolder) {
            return commandHolder.getUsage();
        }
    },
    INCORRECT_TARGET("{target}",
      "§cYou cannot execute this command. Targeted to: §f{target}§c.") {
        @Override
        public String getDefault(CommandHolder<?, ?> commandHolder) {
            return commandHolder.getCommandInfo().getTarget().name();
        }
    };

    private final String placeHolder;
    private final String defMessage;

    public abstract String getDefault(CommandHolder<?, ?> commandHolder);

}
