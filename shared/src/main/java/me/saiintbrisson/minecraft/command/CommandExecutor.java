package me.saiintbrisson.minecraft.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.exceptions.IncorrectUsageException;
import me.saiintbrisson.minecraft.command.handlers.CommandHandler;
import me.saiintbrisson.minecraft.command.handlers.ExceptionHandler;
import me.saiintbrisson.minecraft.command.path.Path;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Luiz Carlos Carvalho
 */
@Getter
@RequiredArgsConstructor
public final class CommandExecutor<P, S> {
    private final AbstractFrame<P> frame;
    private final ExecutionValidator<S> validator;

    public boolean execute(S sender, String label, String[] args, Path root) {
        Execution execution = new Execution(sender, label, args);
        ExecutionResponse response = execution.walk(root);
        if (response.handled) {
            return response.response.isSuccess();
        }

        Throwable throwable = response.response.getThrowable();
        ExceptionHandler handler = frame.getGlobalExceptionHandler().get(throwable.getClass());
        if (handler == null) throw new RuntimeException(throwable);

        handler.handle(response.context, throwable);
        return false;
    }

    private class Execution {
        private final S sender;
        private final String label;
        private final String[] args;
        private final Iterator<String> iterator;
        private final Map<String, String> inputs;

        public Execution(S sender, String label, String[] args) {
            this.sender = sender;
            this.label = label;
            this.args = args;

            this.iterator = Arrays.asList(args).iterator();
            this.inputs = new LinkedHashMap<>();
        }

        private ExecutionResponse walk(Path current) {
            Context<?> ctx = null;

            try {
                if (current.getInfo() != null) {
                    validator.validate(current.getInfo(), sender);
                }

                if (!iterator.hasNext()) {
                    CommandHandler handler = current.getCommandHandler();
                    ctx = frame.createContext(current.getInfo(), sender, label, args, inputs);

                    if (handler == null) {
                        throw new IncorrectUsageException(String.format("path %s is unhandled", current.getIdentifier()));
                    }

                    return new ExecutionResponse(ctx, handler.handle(ctx), true);
                }

                final String arg = iterator.next();
                final String loweredArg = arg.toLowerCase();

                current = current
                  .getNodes()
                  .stream()
                  .filter(sub -> sub.isInput() || sub.getIdentifier().equals(loweredArg) || sub
                    .getAliases()
                    .contains(loweredArg))
                  .findFirst()
                  .orElseThrow(() -> new IncorrectUsageException(String.format("%s does not apply to any paths", arg)));

                if (current.isInput()) {
                    inputs.put(current.getIdentifier(), arg);
                }

                ExecutionResponse response = walk(current);
                if (!response.handled) {
                    response = handleException(current, response.context, response.response.getThrowable());
                }

                return response;
            } catch (RuntimeException e) {
                return handleException(current, ctx, e);
            }
        }

        private ExecutionResponse handleException(Path current, Context ctx, Throwable e) {
            if (ctx == null) {
                ctx = frame.createContext(current.getInfo(), sender, label, args, inputs);
            }

            ExceptionHandler handler = current.getExceptionHandlers().get(e.getClass());
            if (handler == null) return new ExecutionResponse(ctx, CommandHandler.HandlerResponse.error(e));

            handler.handle(ctx, e);
            return new ExecutionResponse(ctx, CommandHandler.HandlerResponse.error(e), true);
        }
    }

    @AllArgsConstructor
    @RequiredArgsConstructor
    private class ExecutionResponse {
        private final Context<?> context;
        private final CommandHandler.HandlerResponse response;
        private boolean handled;
    }
}
