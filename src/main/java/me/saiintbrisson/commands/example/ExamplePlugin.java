package me.saiintbrisson.commands.example;

import me.saiintbrisson.commands.annotations.Command;
import me.saiintbrisson.commands.CommandFrame;
import me.saiintbrisson.commands.Execution;
import me.saiintbrisson.commands.annotations.Completer;
import me.saiintbrisson.commands.result.ResultType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
            description = "test",
            permission = "test.1",
            usage = "test <letter>")
    public ResultType test(Execution execution) {
        String letter = execution.getArg(0);

        if(letter == null) return ResultType.INCORRECT_USAGE;
        execution.sendMessage(letter);

        return ResultType.NONE;
    }

    @Command(name = "test.msg",
            aliases = {"tstmsg", "testemsg"},
            description = "test msg",
            usage = "test msg <letter>",
            async = true,
            inGameOnly = true)
    public ResultType testMsg(Execution execution) {
        String letter = execution.getArg(0);

        if(letter == null) return ResultType.INCORRECT_USAGE;
        execution.sendMessage("msg " + letter);

        return ResultType.NONE;
    }

}
