package com.henryfabio.commandframework;

import me.saiintbrisson.bungee.command.BungeeFrame;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

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
            permission = "op"
    )
    public void testCommand(Context<CommandSender> context) {
        context.sendMessage("§eBanana com abacate meu pau no teu tomate");
    }

    @Command(
            name = "test.test1",
            permission = "op",
            target = CommandTarget.PLAYER
    )
    public void test1Command(Context<CommandSender> context, @Optional ProxiedPlayer player, String[] message) {
        if (player == null) {
            context.sendMessage("§cEste usuário não está online.");
            return;
        }

        player.sendMessages(message);
    }

}
