package me.saiintbrisson.commands.n.example;

import me.saiintbrisson.commands.CommandFrame;
import org.bukkit.plugin.java.JavaPlugin;

public class ExamplePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        /**
         * If `defaultValue = false` is omitted,
         * all default adapters will be registered
         */

        CommandFrame frame = new CommandFrame(this);

        /**
         * This will override the default adapter for `int`
         */

        frame.registerType(Integer.TYPE, argument -> Integer.parseInt(argument) * 2);

        frame.register(new SimpleCommand());
    }

}
