package me.saiintbrisson.bungee.command.command;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import me.saiintbrisson.bungee.command.BungeeFrame;
import me.saiintbrisson.bungee.command.accessor.CommandAccessor;
import me.saiintbrisson.bungee.command.executor.BungeeCommandExecutor;
import me.saiintbrisson.bungee.command.target.BungeeTargetValidator;
import me.saiintbrisson.bungee.command.util.StringUtil;
import me.saiintbrisson.minecraft.command.command.CommandHolder;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.executor.CommandExecutor;
import me.saiintbrisson.minecraft.command.executor.CompleterExecutor;
import me.saiintbrisson.minecraft.command.message.MessageHolder;
import me.saiintbrisson.minecraft.command.message.MessageType;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Henry Fábio
 * Github: https://github.com/HenryFabio
 */
@Getter
public class BungeeCommand extends Command implements CommandHolder<CommandSender, BungeeChildCommand>, TabExecutor {

    private final BungeeFrame frame;
    private final MessageHolder messageHolder;

    private CommandInfo commandInfo;

    private final int position;

    private CommandExecutor<CommandSender> commandExecutor;
    private CompleterExecutor<CommandSender> completerExecutor;

    private final List<BungeeChildCommand> childCommandList = new LinkedList<>();

    @Setter
    private String usage;
    private final String description = "Not provided";

    public BungeeCommand(BungeeFrame frame, String name, int position) {
        super(name);
        this.frame = frame;
        this.position = position;
        this.messageHolder = frame.getMessageHolder();
    }

    public final void initCommand(CommandInfo commandInfo, CommandExecutor<CommandSender> commandExecutor) {
        if (this.commandInfo != null) {
            throw new IllegalStateException("Command already initialized");
        }

        this.commandInfo = commandInfo;
        this.commandExecutor = commandExecutor;

        try {
            CommandAccessor accessor = CommandAccessor.accessor();

            accessor.aliasesField().set(
                    this,
                    commandInfo.getAliases()
            );

            String permission = commandInfo.getPermission();
            if (!StringUtil.isEmpty(permission)) {
                accessor.permissionField().set(
                        this,
                        permission
                );
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }

        final String usage = commandInfo.getUsage();
        if (!StringUtil.isEmpty(usage)) {
            setUsage(usage);
        } else if (commandExecutor instanceof BungeeCommandExecutor) {
            setUsage(((BungeeCommandExecutor) commandExecutor).getParser().buildUsage(getFancyName()));
        }

        if (commandExecutor instanceof BungeeCommandExecutor) {
            ((BungeeCommandExecutor) commandExecutor).setCommand(this);
        }
    }

    public final void initCompleter(CompleterExecutor<CommandSender> completerExecutor) {
        if (this.completerExecutor != null) {
            throw new IllegalStateException("Completer already initialized");
        }

        this.completerExecutor = completerExecutor;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!testPermissionSilent(sender)) {
            sender.sendMessage(
                    messageHolder.getReplacing(MessageType.NO_PERMISSION, getPermission())
            );
            return;
        }

        if (!BungeeTargetValidator.INSTANCE.validate(commandInfo.getTarget(), sender)) {
            sender.sendMessage(frame.getMessageHolder().getReplacing(
                    MessageType.INCORRECT_TARGET,
                    commandInfo.getTarget().name()
            ));
            return;
        }

        if (args.length > 0) {
            BungeeChildCommand command = getChildCommand(args[0]);
            if (command != null) {
                command.execute(sender, Arrays.copyOfRange(args, 1, args.length));
                return;
            }
        }

        if (commandExecutor == null) {
            return;
        }

        commandExecutor.execute(new BungeeContext(
                sender,
                BungeeTargetValidator.INSTANCE.fromSender(sender),
                args
        ));
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (completerExecutor != null) {
            return completerExecutor.execute(new BungeeContext(
                    sender,
                    BungeeTargetValidator.INSTANCE.fromSender(sender),
                    args
            ));
        }

        if (childCommandList.size() != 0 && args.length != 0) {
            List<String> matchedChildCommands = new ArrayList<>();

            for (BungeeChildCommand command : childCommandList) {
                if (StringUtils.startsWithIgnoreCase(command.getName(), args[args.length - 1])) {
                    matchedChildCommands.add(command.getName());
                }
            }

            if (matchedChildCommands.size() != 0) {
                matchedChildCommands.sort(String.CASE_INSENSITIVE_ORDER);
                return matchedChildCommands;
            }
        }

        return Lists.newArrayList();
    }

    @Override
    public BungeeChildCommand getChildCommand(String name) {
        for (BungeeChildCommand childCommand : childCommandList) {
            if (childCommand.equals(name)) return childCommand;
        }

        return null;
    }

    public BungeeCommand createRecursive(String name) {
        int position = getPosition() + StringUtil.countMatches(name, ".");
        if (position == getPosition()) {
            return this;
        }

        String subName = name.substring(Math.max(name.indexOf('.') + 1, 0));

        int index = subName.indexOf('.');
        String nextSubCommand = subName;
        if (index != -1) {
            nextSubCommand = subName.substring(0, index);
        }

        BungeeChildCommand childCommand = getChildCommand(nextSubCommand);

        if (childCommand == null) {
            childCommand = new BungeeChildCommand(frame, nextSubCommand, this);
            getChildCommandList().add(childCommand);
        }

        return childCommand.createRecursive(subName);
    }

    @Override
    public List<String> getAliasesList() {
        return Arrays.asList(getAliases());
    }

    @Override
    public String getFancyName() {
        return getName();
    }

    private boolean testPermissionSilent(@NotNull CommandSender target) {
        String permission = getPermission();
        if ((permission == null) || (permission.length() == 0)) {
            return true;
        }

        for (String p : permission.split(";")) {
            if (target.hasPermission(p)) {
                return true;
            }
        }

        return false;
    }

}
