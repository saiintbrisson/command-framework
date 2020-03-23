package me.saiintbrisson.commands.example;

import me.saiintbrisson.commands.annotations.Command;
import me.saiintbrisson.commands.CommandFrame;
import me.saiintbrisson.commands.Execution;
import me.saiintbrisson.commands.result.ResultType;
import org.bukkit.plugin.java.JavaPlugin;

public class ExamplePlugin extends JavaPlugin {

    private CommandFrame frame;

    @Override
    public void onEnable() {
        frame = new CommandFrame(this);

        frame.setLackPermMessage("§cYou do not have access to this command.");
        frame.setInGameOnlyMessage("§cThis command is only available to in-game players.");
        frame.setUsageMessage("§cCorrect use: §e/{usage}§c.");

        frame.registerCommands(this);
        frame.registerCompleters(this);
    }

    @Command(name = "test",
            aliases = {"tst", "teste"},
            usage = "test msg <message>"
    )
    public ResultType test(Execution execution) {
        return ResultType.INCORRECT_USAGE;
    }

    @Command(name = "test.msg",
            aliases = {"tstmsg", "testemsg"},
            description = "test msg",
            usage = "test msg <message>",
            async = true,
            inGameOnly = true)
    public ResultType testMsg(Execution execution) {
        String msg = execution.getArg(0);
        if(msg == null) return ResultType.INCORRECT_USAGE;

        msg = String.join(" ", execution.getArgs());

        execution.sendMessage("msg " + msg);

        return ResultType.NONE;
    }

}
