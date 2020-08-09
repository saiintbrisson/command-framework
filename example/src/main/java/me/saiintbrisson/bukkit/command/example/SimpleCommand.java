package me.saiintbrisson.bukkit.command.example;

import me.saiintbrisson.bukkit.command.command.BukkitContext;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class SimpleCommand {

    @Command(
      name = "tester"
    )
    public void handleSimple(BukkitContext context, String oi, String[] name) {
        context.sendMessage("resultado picakkkkkkk:");
        context.sendMessage(oi);
        context.sendMessage(Arrays.toString(name));
    }

    /*
        A command method must return one of the following:
        - void
        - boolean
     */

    /*
        The BukkitContext parameter contains all the information you may need
     */

    @Command(
      name = "simple",
      aliases = {"example"},

      target = CommandTarget.ALL // This line can be omitted, it defaults to ALL
    )
    public void handleSimple(BukkitContext context, Player player, String[] message) {
        String join = String.join(" ", message);

        context.sendMessage("you -> %s: %s", player.getName(), join);
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
        Primitive types are supported but cannot be nullable
     */

    @Command(
      name = "simple.console",
      target = CommandTarget.CONSOLE
    )
    public void handleSimpleConsole(BukkitContext context, int number) {
        context.sendMessage("Given number * 2: %s", number);
    }

    @Command(
      name = "simple.player",
      target = CommandTarget.PLAYER,

      permission = "simple.permission"
    )
    public void handleSimplePlayer(BukkitContext context) {
        context.sendMessage("§aYay! You may access this command.");
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
    public void handleSimplePlayerSub(Context<CommandSender> context, @Optional Player[] players) {
        final CommandSender sender = context.getSender();

        if (players == null) {
            context.sendMessage("Remember to provide some players! %s", sender.getName());
            return;
        }

        for (Player player : players) {
            player.sendMessage("You look nice!");
        }
    }

}
