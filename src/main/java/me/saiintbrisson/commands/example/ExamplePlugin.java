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
    }

    @Command(name = "test", aliases = {"tst", "teste"}, permission = "test.1", usage = "test <letter>")
    public ResultType test(Execution execution) {
        String letter = execution.getArg(0);

        if(letter == null) return ResultType.INCORRECT_USAGE;
        execution.sendMessage(letter);

        return ResultType.NONE;
    }

    @Command(name = "test.msg", aliases = {"tstmsg", "testemsg"}, usage = "test msg <letter>")
    public ResultType testMsg2(Execution execution) {
        String letter = execution.getArg(0);

        if(letter == null) return ResultType.INCORRECT_USAGE;
        execution.sendMessage("msg " + letter);

        return ResultType.NONE;
    }

}
