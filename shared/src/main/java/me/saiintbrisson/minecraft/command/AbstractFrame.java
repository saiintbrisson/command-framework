package me.saiintbrisson.minecraft.command;

import lombok.AccessLevel;
import lombok.Getter;
import me.saiintbrisson.minecraft.command.command.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.exceptions.IncorrectUsageException;
import me.saiintbrisson.minecraft.command.exceptions.InsufficientPermissionsException;
import me.saiintbrisson.minecraft.command.exceptions.MismatchedTargetException;
import me.saiintbrisson.minecraft.command.handlers.CommandHandler;
import me.saiintbrisson.minecraft.command.handlers.ExceptionHandler;
import me.saiintbrisson.minecraft.command.handlers.reflection.MethodCommandHandler;
import me.saiintbrisson.minecraft.command.handlers.reflection.MethodExceptionHandler;
import me.saiintbrisson.minecraft.command.parameter.AdapterMap;
import me.saiintbrisson.minecraft.command.parameter.ExtractorMap;
import me.saiintbrisson.minecraft.command.parameter.Parameters;
import me.saiintbrisson.minecraft.command.path.Path;
import me.saiintbrisson.minecraft.command.path.PathInfo;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Luiz Carlos Carvalho
 */

@Getter
public abstract class AbstractFrame<P> implements CommandFrame<P> {
    private final P plugin;
    private final AdapterMap adapterMap;
    private final ExtractorMap extractorMap;
    @Getter(AccessLevel.PACKAGE)
    private final Map<Class<?>, ExceptionHandler> globalExceptionHandler;

    public AbstractFrame(P plugin) {
        this.plugin = plugin;
        this.adapterMap = new AdapterMap();
        this.extractorMap = new ExtractorMap();
        this.globalExceptionHandler = new HashMap<Class<?>, ExceptionHandler>() {{
            put(IncorrectUsageException.class, new IncorrectUsageException.Handler());
            put(InsufficientPermissionsException.class, new InsufficientPermissionsException.Handler());
            put(MismatchedTargetException.class, new MismatchedTargetException.Handler());
        }};
    }

    protected abstract Command getCommand(String name);

    protected abstract void registerToPlatform(Path path);

    protected abstract <S> Context<S> createContext(PathInfo info, S sender, String label, String[] args,
                                                    Map<String, String> inputs);

    @Override
    public void registerAll(Object... instances) {
        for (Object instance : instances) {
            me.saiintbrisson.minecraft.command.annotation.Command containerCommand = instance.getClass()
              .getAnnotation(me.saiintbrisson.minecraft.command.annotation.Command.class);
            Path container = null;

            if (containerCommand != null) {
                PathInfo containerInfo = PathInfo.ofCommand(containerCommand);
                container = resolveAndRegister(null, containerInfo.path());
            }

            registerMethods(instance, container);
        }
    }

    private void registerMethods(Object instance, Path container) {
        for (Method method : instance.getClass().getDeclaredMethods()) {
            me.saiintbrisson.minecraft.command.annotation.Command methodCommand = method.getAnnotation(
              me.saiintbrisson.minecraft.command.annotation.Command.class);
            me.saiintbrisson.minecraft.command.annotation.ExceptionHandler methodExceptionHandler = method.getAnnotation(
              me.saiintbrisson.minecraft.command.annotation.ExceptionHandler.class);

            PathInfo info;
            if (methodCommand != null) {
                info = PathInfo.ofCommand(methodCommand);
            } else if (methodExceptionHandler != null) {
                info = PathInfo.builder().path(methodExceptionHandler.value()).build();
            } else {
                continue;
            }

            Path path = container;

            if (!info.path().isEmpty()) {
                path = resolveAndRegister(container, info.path());
                if (container != null) {
                    container.addNode(path);
                }
            }

            Parameters parameters = Parameters.ofMethod(method, methodCommand == null);

            if (methodCommand != null) {
                if (path == null) {
                    throw new IllegalArgumentException("empty paths must be located within a command container class");
                }

                CommandHandler<?> commandHandler = new MethodCommandHandler<>(instance, method, parameters);
                path.setCommandHandler(commandHandler);
            } else {
                ExceptionHandler<?, ?> exceptionHandler = new MethodExceptionHandler<>(instance, method, parameters);
                Class<?> throwable = parameters
                  .getException()
                  .orElseThrow(() -> new IllegalArgumentException("missing exception parameter"));

                if (path == null) {
                    getGlobalExceptionHandler().put(throwable, exceptionHandler);
                } else {
                    path.getExceptionHandlers().put(throwable, exceptionHandler);
                }
            }
        }
    }

    @Override
    public <S> void registerCommand(PathInfo info, CommandHandler<S> handler) {
        Path path = resolveAndRegister(null, info.path());
        path.setCommandHandler(handler);
    }

    private Path resolveAndRegister(Path parent, String path) {
        Iterator<Path> resolver = Path.createPathResolver(path);
        Path head = resolver.next();
        Path tail = head;

        while (resolver.hasNext()) {
            Path temp = resolver.next();
            if (parent != null) {
                temp.setInfo(PathInfo.combine(parent.getInfo(), temp.getInfo()));
            }
            tail.addNode(tail = temp);
        }

        if (parent != null) {
            head.setInfo(PathInfo.combine(parent.getInfo(), head.getInfo()));
            parent.addNode(head);
        }

        Command command = getCommand(head.getIdentifier());
        if (command != null) {
            command.getPath().apply(head);
        } else {
            if (head.getIdentifier().isEmpty()) {
                throw new IllegalArgumentException("root command name cannot be empty");
            }

            registerToPlatform(head);
        }

        return tail;
    }
}
