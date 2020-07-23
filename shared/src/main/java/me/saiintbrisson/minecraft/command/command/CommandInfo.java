package me.saiintbrisson.minecraft.command.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.target.CommandTarget;

/**
 * @author SaiintBrisson
 */

@Getter
@Builder
@AllArgsConstructor
public class CommandInfo {

    /**
     * Defines the command name, sub-commands are split with dots
     * <p><p>
     * <b>Example:</b><p>
     * parentcommand<p>
     * parentcommand.subcommand<p>
     *
     * @return the command name
     */
    private String name;

    /**
     * @return the command aliases
     */
    private String[] aliases;

    /**
     * @return the command description
     */
    @Setter
    private String description;

    /**
     * @return the command usage example
     */
    @Setter
    private String usage;

    /**
     * @return the required permission to execute the command
     */
    @Setter
    private String permission;

    /**
     * @return the command target
     */
    @Setter
    private CommandTarget target;

    public CommandInfo(Command command) {
        this(
          command.name(),
          command.aliases(),
          command.description(),
          command.usage(),
          command.permission(),
          command.target()
        );
    }

}
