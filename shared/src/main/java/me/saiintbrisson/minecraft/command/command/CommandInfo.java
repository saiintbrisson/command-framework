package me.saiintbrisson.minecraft.command.command;

import lombok.*;
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
    @NonNull
    private String name;

    /**
     * @return the command aliases
     */
    @NonNull
    @Builder.Default
    private String[] aliases = new String[0];

    /**
     * @return the command description
     */
    @Setter
    @Builder.Default
    private String description = "";

    /**
     * @return the command usage example
     */
    @Setter
    @Builder.Default
    private String usage = "";

    /**
     * @return the required permission to execute the command
     */
    @Setter
    @Builder.Default
    private String permission = "";

    /**
     * @return the command target
     */
    @Setter
    @NonNull
    @Builder.Default
    private CommandTarget target = CommandTarget.ALL;

    /**
     * Tells the executor how to run the command,
     * some implementations might ignore this option as they are async by default.
     * This option requires an executor.
     * @return whether the command should be ran asynchronously
     */
    @Setter
    @NonNull
    @Builder.Default
    private boolean async = false;

    public CommandInfo(Command command) {
        this(
          command.name(),
          command.aliases(),
          command.description(),
          command.usage(),
          command.permission(),
          command.target(),
          command.async()
        );
    }

}
