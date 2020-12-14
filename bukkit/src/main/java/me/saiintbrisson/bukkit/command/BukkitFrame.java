/*
 * Copyright 2020 Luiz Carlos Mourão Paes de Carvalho
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package me.saiintbrisson.bukkit.command;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.saiintbrisson.bukkit.command.command.BukkitCommand;
import me.saiintbrisson.bukkit.command.executor.BukkitCommandExecutor;
import me.saiintbrisson.bukkit.command.executor.BukkitCompleterExecutor;
import me.saiintbrisson.minecraft.command.CommandFrame;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.annotation.Completer;
import me.saiintbrisson.minecraft.command.argument.AdapterMap;
import me.saiintbrisson.minecraft.command.argument.eval.MethodEvaluator;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.exception.CommandException;
import me.saiintbrisson.minecraft.command.executor.CommandExecutor;
import me.saiintbrisson.minecraft.command.executor.CompleterExecutor;
import me.saiintbrisson.minecraft.command.message.MessageHolder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * @author SaiintBrisson (https://github.com/SaiintBrisson)
 */

@Getter
public class BukkitFrame implements CommandFrame<Plugin, CommandSender, BukkitCommand> {
    private final Plugin plugin;
    private final AdapterMap adapterMap;
    private final MessageHolder messageHolder;

    private final Map<String, BukkitCommand> commandMap;
    private final MethodEvaluator methodEvaluator;

    @Getter(AccessLevel.PRIVATE)
    private final CommandMap bukkitCommandMap;

    @Setter
    private Executor executor;

    public BukkitFrame(@NonNull @NotNull Plugin plugin, @NonNull @NotNull AdapterMap adapterMap) {
        this.plugin = plugin;

        this.adapterMap = adapterMap;
        this.messageHolder = new MessageHolder();

        this.commandMap = new HashMap<>();
        this.methodEvaluator = new MethodEvaluator(adapterMap);

        try {
            final Server server = Bukkit.getServer();
            final Method mapMethod = server.getClass().getMethod("getCommandMap");

            this.bukkitCommandMap = (CommandMap) mapMethod.invoke(server);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new CommandException(e);
        }
    }

    public BukkitFrame(@NonNull @NotNull Plugin plugin, boolean registerDefault) {
        this(plugin, new AdapterMap(registerDefault));

        if (registerDefault) {
            registerAdapter(Player.class, Bukkit::getPlayer);
            registerAdapter(OfflinePlayer.class, Bukkit::getOfflinePlayer);
        }
    }

    public BukkitFrame(Plugin plugin) {
        this(plugin, true);
    }

    @Override
    public BukkitCommand getCommand(String name) {
        int index = name.indexOf('.');
        String nextSubCommand = name;
        if (index != -1) {
            nextSubCommand = name.substring(0, index);
        }

        BukkitCommand subCommand = commandMap.get(nextSubCommand);
        if (subCommand == null) {
            subCommand = new BukkitCommand(this, nextSubCommand, 0);
            commandMap.put(nextSubCommand, subCommand);
        }

        return subCommand.createRecursive(name);
    }

    @Override
    public void registerCommands(Object... objects) {
        for (Object object : objects) {
            for (Method method : object.getClass().getDeclaredMethods()) {
                Command command = method.getAnnotation(Command.class);
                if (command != null) {
                    registerCommand(new CommandInfo(command), new BukkitCommandExecutor(this, method, object));
                    continue;
                }

                Completer completer = method.getAnnotation(Completer.class);
                if (completer != null) {
                    registerCompleter(completer.name(), new BukkitCompleterExecutor(method, object));
                }
            }
        }
    }

    @Override
    public void registerCommand(CommandInfo commandInfo, CommandExecutor<CommandSender> commandExecutor) {
        BukkitCommand recursive = getCommand(commandInfo.getName());
        if (recursive == null) {
            return;
        }

        recursive.initCommand(commandInfo, commandExecutor);

        if (recursive.getPosition() == 0) {
            bukkitCommandMap.register(plugin.getName(), recursive);
        }
    }

    @Override
    public void registerCompleter(String name, CompleterExecutor<CommandSender> completerExecutor) {
        BukkitCommand recursive = getCommand(name);
        if (recursive == null) {
            return;
        }

        recursive.initCompleter(completerExecutor);
    }

    @Override
    public boolean unregisterCommand(String name) {
        final BukkitCommand command = commandMap.remove(name);
        return command != null && command.unregister(bukkitCommandMap);
    }
}
