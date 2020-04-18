package me.saiintbrisson.commands.example;

import me.saiintbrisson.commands.CommandFrame;
import me.saiintbrisson.commands.Execution;
import me.saiintbrisson.commands.annotations.Command;
import me.saiintbrisson.commands.argument.Argument;
import me.saiintbrisson.commands.result.ResultType;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

/*


    About Argument annotations:
    primitive types may not be null,
    the default value must be parsable from string

    Types must be registered *before commands


 */
public class ExamplePlugin extends JavaPlugin {

    private CommandFrame frame;

    @Override
    public void onEnable() {
        frame = new CommandFrame(this);

        frame.setLackPermMessage("§cYou do not have access to this command.");
        frame.setInGameOnlyMessage("§cThis command is only available to in-game players.");
        frame.setUsageMessage("§cCorrect use: §e/{usage}§c.");

        // Registers a type
        frame.registerType(OfflinePlayer.class, Bukkit::getOfflinePlayer);

        // Registers a command class
        frame.register(this);
    }

    @Command(
        name = "test",
        aliases = "testcommand",

        description = "A command that accepts a message as argument",
        usage = "test <message>",

        inGameOnly = true
    )
    public void test(Execution execution, String message) {
        execution.sendMessage("Message: " + message);
    }

    @Command(
      name = "test.default",
      aliases = "testcommand",

      description = "A command that accepts a message as argument",
      usage = "test <message>",

      inGameOnly = true
    )
    public void testDefault(Execution execution, @Argument(defaultValue = "no message provided") String message) {
        execution.sendMessage("Message: " + message);
    }

    @Command(
      name = "test.multidefault",
      aliases = "testcommand",

      description = "A command that accepts a message as argument",
      usage = "test <message>",

      inGameOnly = true
    )
    public void testMultipleDefault(Execution execution,
                                    @Argument(defaultValue = "no message provided") String message,
                                    @Argument(defaultValue = "no message provided either") String secondMessage) {
        execution.sendMessage("Message: " + message + " " + secondMessage);
    }

    @Command(
      name = "test.nullable",
      aliases = "testcommand",

      description = "A command that accepts a message as argument",
      usage = "test <message>",

      inGameOnly = true
    )
    public void testNullable(Execution execution, @Argument(nullable = true) String message) {
        execution.sendMessage("May be null: " + message);
    }

    @Command(
      name = "test.multinullable",
      aliases = "testcommand",

      description = "A command that accepts a message as argument",
      usage = "test <message>",

      inGameOnly = true
    )
    public void testMultipleNullable(Execution execution,
                                     @Argument(nullable = true) String message,
                                     @Argument(nullable = true) String secondMessage) {
        execution.sendMessage("May be null: " + message + " " + secondMessage);
    }

    /*
        This command will only be available in game as his parent has inGameOnly
        Same thing applies for permissions
     */
    @Command(
        name = "test.sub",
        aliases = "",

        description = "Test subcommand",

        async = true // asynchronous command
    )
    public boolean testSub(Execution execution) {
        /*
            some huge task here
         */

        return false;
    }

    @Command(
        name = "customargument",
        usage = "customargument SaiintBrisson"
    )
    public ResultType customArgument(Execution execution, OfflinePlayer player) {
        if(!player.getName().equals("SaiintBrisson")) {
            return ResultType.INCORRECT_USAGE;
        }

        if(!player.isOnline()) {
            execution.sendMessage("§c%s is offline :(", player.getName());
            return ResultType.NONE;
        }

        execution.sendMessage(new String[] {
            "§fSaiintBrisson §ais online!",
            "§aHis UUID is: §f" + player.getUniqueId() + "§a."
        });
        return ResultType.NONE;
    }

}
