package me.saiintbrisson.minecraft.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.saiintbrisson.minecraft.command.command.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.handlers.CommandHandler;
import me.saiintbrisson.minecraft.command.path.Path;

import java.util.*;

/**
 * @author Luiz Carlos Carvalho
 */
@Getter
@RequiredArgsConstructor
public final class CommandExecutor<P, S> {
    private final AbstractFrame<P> frame;
    private final ExecutionValidator<S> validator;

    public boolean execute(S sender, String label, String[] args, Command root) {
        this.validator.validate(root.getInfo(), sender);

        Iterator<String> iterator = Arrays.asList(args).iterator();
        Map<String, String> inputs = new LinkedHashMap<>();

        Path current = root.getPath();
        while (iterator.hasNext()) {
            final String arg = iterator.next();
            final String loweredArg = arg.toLowerCase();

            current = current.getNodes().stream()
              .filter(sub -> sub.isInput()
                || sub.getIdentifier().equals(loweredArg)
                || sub.getAliases().contains(loweredArg))
              .findFirst()
              .orElseThrow(() -> new IllegalArgumentException(String.format("%s does not apply to any paths", arg)));

            validator.validate(current.getInfo(), sender);

            if (current.isInput()) {
                inputs.put(current.getIdentifier(), arg);
            }
        }

        CommandHandler handler = current.getCommandHandler();
        if (handler == null) {
            throw new IllegalArgumentException(String.format("path %s is unhandled", current.getIdentifier()));
        }

        Context<S> context = frame.createContext(sender, label, args, inputs);

        return handler.handle(context);
    }
}
