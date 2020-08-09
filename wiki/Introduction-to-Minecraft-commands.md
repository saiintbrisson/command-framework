The three important classes introduced by this framework are: [CommandFrame][CommandFrame], [Command][Command], and [Context][Context].

### CommandFrame

This class has a different implementation for each platform supported, we will work with [BukkitFrame][BukkitFrame] for now.

CommandFrame is our main class, it's used to **register your commands, completers, and adapters**. There are many other functionalities such as setting the executor to be used in async commands and creating your own [custom messages][Custom-messages].

> Creating a CommandFrame:
```java
import me.saiintbrisson.bukkit.command.BukkitFrame;

public class YourPlugin extends JavaPlugin {

    public void onEnable() {
        BukkitFrame frame = new BukkitFrame(plugin); // Creates a BukkitFrame with the default adapters
        
        BukkitFrame frame = new BukkitFrame(plugin, false); // Creates a BukkitFrame without the default adapters
            
        BukkitFrame frame = new BukkitFrame(plugin, adapterMap); // Creates a BukkitFrame with the adapters registered in your AdapterMap
    }

}
```

### Adapters

Adapters are used to convert the received argument string into the type defined in your method. 

The framework has support for all primitive types (and their wrappers) built-in, but this is not enough in most cases, the CommandFrame class provides a way to register custom adapters or overwrite existing ones. You can create default adapter maps with [AdapterMap][AdapterMap].

> Registering a custom adapter
```java
BukkitFrame frame = new BukkitFrame(plugin);

frame.registerAdapter(Integer.TYPE, argument -> Integer.parseInt(argument) * 2);
frame.registerAdapter(YourObject.class, YourObject::new);
```

### The Command Annotation

The [Command][Command] annotation is how the framework detects and registers your command's methods. It stores the command's information, such as name, aliases, target and other metadata.

> Annotation overhaul
```java
@Command(
    name = "supercommand", // Name is the only required field
    aliases = {"hipercommand"}, // Aliases are "synonyms" to your name

    description = "This command is awesome!", // Description is usually a short message specifying what the command does
    usage = "supercommand <message>", // If omitted, the framework will create an usage message from the method definition
    permission = "super.command", // Defines the permission required to run the command, this is tested automatically

    target = CommandTarget.PLAYER, // Who can execute the command, defaults to ALL
    async = true // Whether this command should be ran using the defined executor
)
```

Async commands require an executor in the CommandFrame. Bukkit has a native executor implementation called [BukkitSchedulerExecution][BukkitSchedulerExecution].

> Setting an executor
```java
BukkitFrame frame = new BukkitFrame(plugin);

frame.setExecutor(new BukkitSchedulerExecutor(plugin));
```

### Context

[Context][Context] is the information created during execution, it stores the sender, label and arguments. It contains a lot of useful commands. `S` is the generic type of the sender.

> General purpose use
```java
@Command(name = "command", target = CommandTarget.PLAYER)
public void handleCommand(Context<Player> context) {
    Player sender = context.getSender();

    sender.setFlying(!sender.isFlying());
    context.sendMessage("Flying mode set to %s", sender.isFlying());
}
```

<!--Variables-->

[CommandFrame]: https://github.com/SaiintBrisson/command-framework/blob/master/shared/src/main/java/me/saiintbrisson/minecraft/command/CommandFrame.java
[Command]: https://github.com/SaiintBrisson/command-framework/blob/master/shared/src/main/java/me/saiintbrisson/minecraft/command/annotation/Command.java
[Context]: https://github.com/SaiintBrisson/command-framework/blob/master/shared/src/main/java/me/saiintbrisson/minecraft/command/command/Context.java

[BukkitFrame]: https://github.com/SaiintBrisson/command-framework/blob/master/bukkit/src/main/java/me/saiintbrisson/bukkit/command/BukkitFrame.java
[Custom-messages]: https://github.com/SaiintBrisson/command-framework/wiki/Custom-messages

[AdapterMap]: https://github.com/SaiintBrisson/command-framework/blob/master/shared/src/main/java/me/saiintbrisson/minecraft/command/argument/AdapterMap.java

[BukkitSchedulerExecution]: https://github.com/SaiintBrisson/command-framework/blob/master/bukkit/src/main/java/me/saiintbrisson/bukkit/command/executor/BukkitSchedulerExecutor.java