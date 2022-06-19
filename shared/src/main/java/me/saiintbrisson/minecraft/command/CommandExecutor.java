package me.saiintbrisson.minecraft.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.exceptions.IncorrectUsageException;
import me.saiintbrisson.minecraft.command.handlers.CommandHandler;
import me.saiintbrisson.minecraft.command.handlers.ExceptionHandler;

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
        try {
            Execution execution = new Execution(sender, label, args);
            return execution.walk(root);
        } catch (ExecutionException e) {
            ExceptionHandler handler = frame.getGlobalExceptionHandler().get(e.getCause().getClass());
            if (handler == null) throw new RuntimeException(e.getCause());

            handler.handle(e.context, e);
            return false;
        }
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

        private boolean walk(Path current) throws ExecutionException {
            Context ctx = null;

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

                    return handler.handle(ctx);
                }

                final String arg = iterator.next();
                final String loweredArg = arg.toLowerCase();

                current = current.getNodes().stream()
                  .filter(sub -> sub.isInput()
                    || sub.getIdentifier().equals(loweredArg)
                    || sub.getAliases().contains(loweredArg))
                  .findFirst()
                  .orElseThrow(() -> new IncorrectUsageException(String.format("%s does not apply to any paths", arg)));

                if (current.isInput()) {
                    inputs.put(current.getIdentifier(), arg);
                }

                return walk(current);
            } catch (ExecutionException e) {
                handleException(current, e.context, e);
            } catch (RuntimeException e) {
                handleException(current, ctx, e);
            }

            return false;
        }

        private void handleException(Path current, Context ctx, Throwable e) throws ExecutionException {
            if (ctx == null) {
                ctx = frame.createContext(current.getInfo(), sender, label, args, inputs);
            }

            ctx.getInputs().put("exception", e);

            ExceptionHandler handler = current.getExceptionHandlers().get(e.getCause().getClass());
            if (handler == null) throw new ExecutionException(e.getCause(), ctx);

            handler.handle(ctx, e);
        }
    }


    private static class ExecutionException extends Exception {
        private final Context context;

        public ExecutionException(Throwable cause, Context context) {
            super(cause);
            this.context = context;
        }
    }
}
