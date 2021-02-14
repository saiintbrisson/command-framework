import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.argument.AdapterMap;
import me.saiintbrisson.minecraft.command.argument.Argument;
import me.saiintbrisson.minecraft.command.argument.eval.ArgumentEvaluator;
import me.saiintbrisson.minecraft.command.argument.eval.MethodEvaluator;
import me.saiintbrisson.minecraft.command.command.Context;
import org.junit.jupiter.api.Test;

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

    public void testCommand(Context<?> context,
                            String firstArgument,
                            @Optional(def = {"Hello", "World!"}) String[] secondArgument) {
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
        final TestContext context = new TestContext(arguments);
        final Object[] objects = argumentEvaluator.parseArguments(context);
        return Arrays.copyOfRange(objects, 1, objects.length);
    }
}
