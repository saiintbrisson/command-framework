package com.henryfabio.commandframework;

import com.google.common.collect.Lists;
import me.saiintbrisson.bungee.command.BungeeFrame;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.annotation.Completer;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.Arrays;
import java.util.List;

/**
 * @author Henry Fábio
 * Github: https://github.com/HenryFabio
 */
public final class MainTest extends Plugin {

    @Override
    public void onEnable() {
        BungeeFrame bungeeFrame = new BungeeFrame(this);

        bungeeFrame.registerCommands(this);
    }

    @Command(
            name = "test",
            aliases = {"testeseila"},
            permission = "op",
            target = CommandTarget.PLAYER
    )
    public void testCommand() {
        System.out.println("213sduahsudh");
    }

    @Completer(
            name = "test"
    )
    public List<String> testCommandCompleter(Context<CommandSender> context) {
        if (context.argsCount() == 1) {
            return Lists.newArrayList(
                    "test1",
                    "test2",
                    "test3"
            );
        }

        return Lists.newArrayList();
    }

}
