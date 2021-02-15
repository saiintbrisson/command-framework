import me.saiintbrisson.minecraft.command.CommandFrame;
import me.saiintbrisson.minecraft.command.argument.AdapterMap;
import me.saiintbrisson.minecraft.command.argument.TypeAdapter;
import me.saiintbrisson.minecraft.command.command.CommandHolder;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.exception.CommandException;
import me.saiintbrisson.minecraft.command.target.CommandTarget;

import java.lang.reflect.Array;

/**
 * @author SaiintBrisson (https://github.com/SaiintBrisson)
 */
public class TestContext implements Context<Object> {
    private final String[] args;
    private final AdapterMap adapterMap;

    public TestContext(String[] args, AdapterMap adapterMap) {
        this.args = args;
        this.adapterMap = adapterMap;
    }

    @Override
    public String getLabel() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getSender() {
        throw new UnsupportedOperationException();
    }

    @Override
    public CommandTarget getTarget() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String[] getArgs() {
        return args;
    }

    @Override
    public <T> T getArg(int index, Class<T> type) {
        return (T) adapterMap.get(type).convertNonNull(getArg(index));
    }

    public <T> T[] getArgs(int from, int to, Class<T> type) {
        try {
            final TypeAdapter<?> adapter = adapterMap.get(type);
            final T[] instance = (T[]) Array.newInstance(type,  to - from);

            for (int i = from; i < to; i++) {
                instance[i - from] = (T) adapter.convertNonNull(getArg(i));
            }

            return instance;
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public void sendMessage(String message) {

    }

    @Override
    public void sendMessage(String[] messages) {

    }

    @Override
    public boolean testPermission(String permission, boolean silent) throws CommandException {
        return false;
    }

    @Override
    public boolean testTarget(CommandTarget target, boolean silent) throws CommandException {
        return false;
    }

    @Override
    public CommandFrame<?, ?, ?> getCommandFrame() {
        throw new UnsupportedOperationException();
    }

    @Override
    public CommandHolder<?, ?> getCommandHolder() {
        throw new UnsupportedOperationException();
    }
}
