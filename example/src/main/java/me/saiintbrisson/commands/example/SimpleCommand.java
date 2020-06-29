package me.saiintbrisson.commands.n.example;

import me.saiintbrisson.commands.Execution;
import me.saiintbrisson.commands.annotations.Command;
import me.saiintbrisson.commands.annotations.CommandTarget;
import me.saiintbrisson.commands.argument.Argument;
import me.saiintbrisson.commands.exceptions.IncorrectUsageException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SimpleCommand {

    /*
        A command method must return one of the following:
        - void
        - boolean
     */

    /*
        The Execution parameter contains all the information you may need
     */

    @Command(
      name = "simple",
      aliases = { "example" },

      target = CommandTarget.BOTH // This line can be omitted, it defaults to BOTH
    )
    public void handleSimple(Execution execution, Player player, String[] message) {
        String join = String.join(" ", message);

        execution.sendMessage("you -> %s: %s", player.getName(), join);
        player.sendMessage(join);
    }

    /*
        Sub-commands are defined with '.', "simple.player"
        where "simple" is the parent command to "player",
        and the logic repeats itself, you can create as many sub-commands
        as you wish
     */

    @Command(
      name = "simple.broadcast",
      permission = "simple.broadcast"
    )
    public void handleSimpleBroadcast(String[] message) {
        Bukkit.broadcastMessage(String.join(" ", message));
    }

    /*
        Primitive types support default values but cannot be nullable
     */

    @Command(
      name = "simple.console",
      target = CommandTarget.CONSOLE,

      options = { "silent" }
    )
    public void handleSimpleConsole(Execution execution, @Argument(defaultValue = "10") int number) {
        if (!execution.hasOption("silent")) {
            execution.sendMessage("Loud!");
        }

        execution.sendMessage("Given number * 2: %s", number);
    }

    @Command(
      name = "simple.player",
      target = CommandTarget.PLAYER,

      permission = "simple.permission"
    )
    public void handleSimplePlayer(Execution execution) {
        execution.sendMessage("§aYay! You may access this command.");
    }

    /*
        Sub-commands follow their parents rules,
        the following command requires its parent's permission and target
        in order to work,

        same thing applies for aliases
     */

    /*
        You can have arrays of any type as long as they're already registered,
        arrays may be defined with common array declaration (Type[]) or as varargs (Type...)

        They support default values and can be nullable
     */

    @Command(
      name = "simple.player.sub",
      description = "Sends a message to the players"
    )
    public void handleSimplePlayerSub(Execution execution, @Argument(nullable = true) String[] message) {
        if (message == null) {
            throw new IncorrectUsageException();
        }

        execution.sendMessage(String.join(" ", message));
    }

}
