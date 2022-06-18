package me.saiintbrisson.minecraft.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.saiintbrisson.minecraft.command.command.Command;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.handlers.CommandHandler;
import me.saiintbrisson.minecraft.command.handlers.reflection.MethodCommandHandler;
import me.saiintbrisson.minecraft.command.parameter.AdapterMap;
import me.saiintbrisson.minecraft.command.parameter.ExtractorMap;
import me.saiintbrisson.minecraft.command.parameter.Parameters;
import me.saiintbrisson.minecraft.command.path.Path;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author Luiz Carlos Carvalho
 */

@Getter
@RequiredArgsConstructor
public abstract class AbstractFrame<P> implements CommandFrame<P> {
    private final P plugin;
    private final AdapterMap adapterMap;
    private final ExtractorMap extractorMap;

    protected abstract Command getCommand(String name);

    protected abstract void createCommand(Path path, CommandInfo info);

    protected abstract <S> Context<S> createContext(S sender, String label, String[] args, Map<String, String> inputs);

    @Override
    public void registerAll(Object... instances) {
        for (Object instance : instances) {
            me.saiintbrisson.minecraft.command.annotation.Command objectCommand = instance.getClass()
              .getAnnotation(me.saiintbrisson.minecraft.command.annotation.Command.class);
            Path root = null;

            if (objectCommand != null) {
                CommandInfo objectInfo = CommandInfo.ofCommand(objectCommand);
                Map.Entry<Path, Path> entry = Path.ofCommandInfo(objectInfo);
                entry.getValue().setInfo(objectInfo);

                registerCommand(entry.getValue(), objectInfo);
                root = entry.getKey();
            }

            registerMethods(instance, root);
        }
    }

    private void registerMethods(Object instance, Path rootNode) {
        for (Method method : instance.getClass().getDeclaredMethods()) {
            me.saiintbrisson.minecraft.command.annotation.Command methodCommand = method.getAnnotation(
              me.saiintbrisson.minecraft.command.annotation.Command.class);
            if (methodCommand == null) continue;

            CommandInfo methodInfo = CommandInfo.ofCommand(methodCommand);
            Map.Entry<Path, Path> entry = Path.ofCommandInfo(methodInfo);
            Path node = entry.getValue();

            if (rootNode == null) {
                registerCommand(entry.getKey(), methodInfo);
            } else {
                if (node.getIdentifier().isEmpty()) {
                    node = rootNode;
                } else {
                    rootNode.addNode(entry.getKey());
                }
            }

            CommandHandler<?> executor = new MethodCommandHandler<>(instance, method, Parameters.ofMethod(method));
            node.setCommandHandler(executor);
        }
    }

    @Override
    public <S> void registerCommand(CommandInfo commandInfo, CommandHandler<S> handler) {
        Map.Entry<Path, Path> entry = Path.ofCommandInfo(commandInfo);

        registerCommand(entry.getKey(), commandInfo);
        entry.getValue().setCommandHandler(handler);
    }

    private void registerCommand(Path path, CommandInfo info) {
        Command command = getCommand(path.getIdentifier());
        if (command != null) {
            command.getPath().apply(path);
            return;
        }

        if (path.getIdentifier().isEmpty()) {
            throw new IllegalArgumentException("root command name cannot be empty");
        }

        createCommand(path, info);
    }
}
