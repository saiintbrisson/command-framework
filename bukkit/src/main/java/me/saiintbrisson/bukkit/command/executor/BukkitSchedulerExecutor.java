package me.saiintbrisson.bukkit.command.executor;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

/**
 * @author SaiintBrisson
 */
public class BukkitSchedulerExecutor implements Executor {

    private final Plugin plugin;
    private final BukkitScheduler scheduler;

    public BukkitSchedulerExecutor(Plugin plugin) {
        this.plugin = plugin;
        this.scheduler = plugin.getServer().getScheduler();
    }

    @Override
    public void execute(@NotNull Runnable command) {
        scheduler.runTaskAsynchronously(plugin, command);
    }

}
