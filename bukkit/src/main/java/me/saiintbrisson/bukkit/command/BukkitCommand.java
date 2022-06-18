package me.saiintbrisson.bukkit.command;

import lombok.Getter;
import me.saiintbrisson.minecraft.command.CommandExecutor;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.command.Command;
import me.saiintbrisson.minecraft.command.path.Path;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Luiz Carlos Carvalho
 */

@Getter
final class BukkitCommand extends org.bukkit.command.Command implements Command {
    private final Path path;
    private final CommandInfo info;
    private final CommandExecutor<Plugin, CommandSender> executor;

    public BukkitCommand(@NotNull Path path, @NotNull CommandInfo info, CommandExecutor<Plugin, CommandSender> executor) {
        super(path.getIdentifier());

        this.path = path;
        this.info = info;
        this.executor = executor;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        return this.executor.execute(sender, commandLabel, args, this);
    }

    @Nullable
    @Override
    public String getPermission() {
        return getInfo().permission();
    }

    @NotNull
    @Override
    public List<String> getAliases() {
        return new ArrayList<>(getPath().getAliases());
    }

    @NotNull
    @Override
    public String getDescription() {
        return getInfo().description();
    }

    @NotNull
    @Override
    public String getUsage() {
        return getInfo().usage();
    }
}
