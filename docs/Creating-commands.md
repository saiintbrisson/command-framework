There are two ways to create a command: by methods annotated with a [Command][Command] or by registering a [PathInfo][PathInfo] with a [CommandExecutor][CommandExecutor] using `CommandFrame#registerCommand(PathInfo, CommandExecutor)`.

##### Subcommands

Subcommands are defined with dots between the command name, e.g: `@Command(name = "parent.child")`. They inherit both target and permissions from their parents, if a parent requires the sender to be a player or have a specific permission, the subcommand will require it too.

### Via the Command annotation

Methods annotated with `@Command` will be automatically registered. Parameters defined in the method will be used as arguments and are automatically converted into the parameter type, they are order dependent. Parameters are required by default, use the [Optional][Optional] annotation to make it not required, you can set default values too.

You can create multiple commands per class.

> Creating a simple command
```java
public class Command {

    @Command(name = "command", aliases = {"nice"}, target = CommandTarget.CONSOLE)
    public void handleCommand(Context<ConsoleCommandSender> context, 
                                Player target, 
                                @Optional(def = {"Hello,", "World!"}) String[] message) {
    
        target.sendMessage("Console sent you: %s", String.join(" ", message));
    }
    
    @Command(name = "command.child")
    public void handleCommandChild(Context<ConsoleCommandSender> context) {
        // ...
    }

}
```

The command above can only be ran by the console (`target = CommandTarget.CONSOLE`), the message parameter is optional (`@Optional(def = {"Hello,", "World!"}) String[] message`), so you can run it with: `/command SaiintBrisson` or `/command SaiintBrisson hey friend, how are you?`. Optionals without a default value will be null.

> Registering your command class
```java
BukkitFrame frame = new BukkitFrame(plugin);

frame.registerCommands(new Command());
```

### Dynamic commands

The framework also provides a way to create dynamic commands by using the `CommandFrame#registerCommand` method.

[PathInfo][PathInfo] has the same fields as [Command][Command] and [CommandExecutor][CommandExecutor] is a functional interface that receives `Context<CommandSender>`.

```java
BukkitFrame frame = new BukkitFrame(plugin);

PathInfo command = PathInfo.builder()
  .path("money :username")
  .build();
frame.registerCommand(command, context -> {
  String username = context.getInput("username");
  context.send(username + "'s balance: $0");

  return false;
});
```

<!--Variables-->

[Command]: https://github.com/SaiintBrisson/command-framework/blob/master/shared/src/main/java/me/saiintbrisson/minecraft/command/annotation/Command.java
[PathInfo]: https://github.com/SaiintBrisson/command-framework/blob/master/shared/src/main/java/me/saiintbrisson/minecraft/command/command/PathInfo.java
[CommandExecutor]: https://github.com/SaiintBrisson/command-framework/blob/master/shared/src/main/java/me/saiintbrisson/minecraft/command/executor/CommandExecutor.java

[Context]: https://github.com/SaiintBrisson/command-framework/blob/master/shared/src/main/java/me/saiintbrisson/minecraft/command/command/Context.java

[Optional]: https://github.com/SaiintBrisson/command-framework/blob/master/shared/src/main/java/me/saiintbrisson/minecraft/command/annotation/Optional.java