import me.saiintbrisson.minecraft.command.CommandFrame;
import me.saiintbrisson.minecraft.command.command.CommandHolder;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.exception.CommandException;
import me.saiintbrisson.minecraft.command.target.CommandTarget;

/**
 * @author SaiintBrisson (https://github.com/SaiintBrisson)
 */
public class TestContext implements Context<Object> {
    private final String[] args;

    public TestContext(String[] args) {
        this.args = args;
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
    public int argsCount() {
        return args.length;
    }

    @Override
    public String getArg(int index) {
        try {
            return args[index];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public String[] getArgs() {
        return args;
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
