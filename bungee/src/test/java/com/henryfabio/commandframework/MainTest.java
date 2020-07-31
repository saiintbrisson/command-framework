package com.henryfabio.commandframework;

import me.saiintbrisson.bungee.command.BungeeFrame;
import me.saiintbrisson.minecraft.command.annotation.Command;
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
            aliases = {"testeseila"}
    )
    public void testCommand() {
        System.out.println("213sduahsudh");
    }

}
