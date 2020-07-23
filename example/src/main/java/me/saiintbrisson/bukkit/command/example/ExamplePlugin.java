package me.saiintbrisson.bukkit.command.example;

import me.saiintbrisson.bukkit.command.BukkitFrame;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import org.bukkit.plugin.java.JavaPlugin;

public class ExamplePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        /*
         * If `defaultValue = false` is omitted,
         * all default adapters will be registered
         */

        BukkitFrame frame = new BukkitFrame(this, true);

        /*
         * This will override the default adapter for `int`
         */

        frame.registerAdapter(Integer.TYPE, argument -> Integer.parseInt(argument) * 2);

        frame.getMessageHolder().loadFromResources("command");

        frame.registerCommand(CommandInfo.builder()
          .name("test.sub")
          .aliases(new String[]{
            "abroba"
          })
          .build(), context -> {
            context.sendMessage("hey");
            return false;
        });

        frame.registerCommands(new SimpleCommand());
    }

}
