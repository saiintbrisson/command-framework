package me.saiintbrisson.bungee.command.accessor;

import lombok.Getter;
import lombok.experimental.Accessors;
import net.md_5.bungee.api.plugin.Command;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author Henry Fábio
 * Github: https://github.com/HenryFabio
 */
@Accessors(fluent = true)
@Getter
public final class CommandAccessor {

    @Getter @Accessors(fluent = true) private static final CommandAccessor accessor = new CommandAccessor();

    private Field
            permissionField,
            aliasesField;

    public CommandAccessor() {
        try {
            Class<Command> commandClass = Command.class;
            this.permissionField = commandClass.getDeclaredField("permission");
            permissionField.setAccessible(true);
            removeFinalModifier(permissionField);

            this.aliasesField = commandClass.getDeclaredField("aliases");
            aliasesField.setAccessible(true);
            removeFinalModifier(aliasesField);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private void removeFinalModifier(Field field) {
        try {
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);

            modifiersField.set(
                    field,
                    field.getModifiers() & ~Modifier.FINAL
            );
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

}
