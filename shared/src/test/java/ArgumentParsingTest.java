import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.argument.AdapterMap;
import me.saiintbrisson.minecraft.command.argument.Argument;
import me.saiintbrisson.minecraft.command.argument.eval.ArgumentEvaluator;
import me.saiintbrisson.minecraft.command.argument.eval.MethodEvaluator;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.exception.CommandException;
import me.saiintbrisson.minecraft.command.message.MessageType;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author SaiintBrisson (https://github.com/SaiintBrisson)
 */
public class ArgumentParsingTest {
    @Test
    void parseMethod() throws NoSuchMethodException {
        final List<Argument<?>> list = getArguments();

        assertEquals(Context.class, list.get(0).getType());
        assertEquals(String.class, list.get(1).getType());
        final Argument<?> arrayArgument = list.get(2);
        assertEquals(arrayArgument.getType(), String.class);
        assertTrue(arrayArgument.isArray());
        assertTrue(arrayArgument.isNullable());
        assertArrayEquals(new String[]{"Hello", "World!"}, (String[]) arrayArgument.getDefaultValue());
    }

    @Test
    void parseArguments() throws NoSuchMethodException {
        final List<Argument<?>> list = getArguments();

        assertArrayEquals(
          new Object[]{"first", new String[] {"Hello", "World!"}},
          getArguments(list, "first")
        );

        assertArrayEquals(
          new Object[]{"first", new String[] {"second"}},
          getArguments(list, "first", "second")
        );

        assertArrayEquals(
          new Object[]{"first", new String[] {"second", "third"}},
          getArguments(list, "first", "second", "third")
        );
    }

    @Test
    void invokeCommand() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final AdapterMap adapterMap = new AdapterMap(true);
        final MethodEvaluator evaluator = new MethodEvaluator(adapterMap);

        final Method method = getClass().getMethod("testCommand", Context.class, String.class, String[].class);
        final List<Argument<?>> list = evaluator.evaluateMethod(method);
        final ArgumentEvaluator<Object> argumentEvaluator = new ArgumentEvaluator<>(list);
        final TestContext context = new TestContext(new String[]{"1.0", "1.1", "1.2", "1.3"}, adapterMap);

        invokeCommand(method, argumentEvaluator, context);
    }

    public void testCommand(Context<?> context,
                            String firstArgument,
                            @Optional(def = {"Hello", "World!"}) String[] secondArgument) {
        context.getArg(0, Double.class);
        assertArrayEquals(new Double[] {1.1, 1.2, 1.3}, context.getArgs(1, 4, Double.class));
    }

    private List<Argument<?>> getArguments() throws NoSuchMethodException {
        final AdapterMap adapterMap = new AdapterMap(true);
        final MethodEvaluator evaluator = new MethodEvaluator(adapterMap);

        return evaluator.evaluateMethod(
          getClass().getMethod("testCommand", Context.class, String.class, String[].class)
        );
    }

    private Object[] getArguments(List<Argument<?>> list, String... arguments) {
        final ArgumentEvaluator<Object> argumentEvaluator = new ArgumentEvaluator<>(list);
        final TestContext context = new TestContext(arguments, null);
        final Object[] objects = argumentEvaluator.parseArguments(context);
        return Arrays.copyOfRange(objects, 1, objects.length);
    }

    private Object invokeCommand(Method method, ArgumentEvaluator<Object> evaluator, Context<Object> context)
      throws InvocationTargetException, IllegalAccessException {
        if (evaluator.getArgumentList().size() == 0) {
            return method.invoke(this);
        }

        final Object[] parameters;

        try {
            parameters = evaluator.parseArguments(context);
        } catch (Exception e) {
            throw new InvocationTargetException(new CommandException(MessageType.INCORRECT_USAGE, null));
        }

        return method.invoke(this, parameters);
    }
}
