package me.saiintbrisson.bukkit.command;

import me.saiintbrisson.minecraft.command.CommandExecutor;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.exceptions.DefaultExceptionHandlers;
import me.saiintbrisson.minecraft.command.handlers.CompleterHandler;
import me.saiintbrisson.minecraft.command.parameter.AdapterMap;
import me.saiintbrisson.minecraft.command.AbstractFrame;
import me.saiintbrisson.minecraft.command.parameter.ExtractorMap;
import me.saiintbrisson.minecraft.command.Path;
import org.bukkit.Server;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Luiz Carlos Carvalho
 */
public final class BukkitFrame extends AbstractFrame<Plugin> {
    private final CommandMap commandMap;
    private final CommandExecutor<Plugin, CommandSender> executor;

    public BukkitFrame(@NotNull Plugin plugin) {
        super(plugin, new AdapterMap(), new ExtractorMap(), new LinkedHashMap<>());
        registerAll(new DefaultExceptionHandlers());

        Server server = plugin.getServer();
        try {
            Method getCommandMap = server.getClass().getDeclaredMethod("getCommandMap");
            this.commandMap = (CommandMap) getCommandMap.invoke(server);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        this.executor = new CommandExecutor<>(this, new BukkitExecutionValidator());

        registerAdapter(Player.class, raw -> plugin.getServer().getPlayer(raw));
    }

    @Override
    protected BukkitCommand getCommand(String name) {
        return (BukkitCommand) this.commandMap.getCommand(name);
    }

    @Override
    protected void registerPath(Path path, CommandInfo info) {
        BukkitCommand command = new BukkitCommand(path, this.executor);
        this.commandMap.register(getPlugin().getName(), command);
    }

    @Override
    protected <S> Context<S> createContext(CommandInfo info, S sender, String label, String[] args, Map<String, String> inputs) {
        return (Context<S>) new BukkitContext(this, info, label, (CommandSender) sender, args, inputs);
    }

    @Override
    public <S> void registerCompleter(String name, CompleterHandler<S> executor) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean unregisterCommand(String name) {
        throw new UnsupportedOperationException();
    }
}
