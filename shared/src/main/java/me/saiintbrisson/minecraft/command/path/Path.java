package me.saiintbrisson.minecraft.command.path;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.handlers.CommandHandler;
import me.saiintbrisson.minecraft.command.handlers.CompleterHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * @author Luiz Carlos Carvalho
 */
@Getter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public final class Path {
    @ToString.Include
    @EqualsAndHashCode.Include
    private final @NotNull String identifier;
    @ToString.Include
    private final boolean input;

    @ToString.Include
    private final Set<Path> nodes;
    @ToString.Include
    private final Set<String> aliases;

    @Setter
    private CommandHandler commandHandler;
    @Setter
    private CompleterHandler completerHandler;

    @Setter
    private CommandInfo info;

    public Path(@NotNull String identifier, boolean input, @NotNull Set<String> aliases) {
        this.identifier = identifier;
        this.input = input;

        if (input && !aliases.isEmpty()) {
            throw new IllegalArgumentException("input paths must not have aliases");
        }

        this.aliases = aliases;

        this.nodes = new TreeSet<>((o1, o2) ->
          o1 == o2 ? 0 : o1.isInput() ? 1 : -1
        );
    }

    public boolean isLeaf() {
        return !this.nodes.isEmpty();
    }

    public Optional<Path> getNode(String name) {
        final String loweredName = name.toLowerCase();

        return this.nodes.stream()
          .filter(node -> node.getIdentifier().equals(loweredName) || node.getAliases().contains(loweredName))
          .findFirst();
    }

    public void apply(Path head) {
        if (!this.identifier.equals(head.getIdentifier())) {
            throw new IllegalArgumentException(String.format(
              "tried combining diverging paths %s and %s",
              getIdentifier(), head.getIdentifier()
            ));
        }

        addAliases(head.getAliases());
        head.getNodes().forEach(this::addNode);
        setInfo(head.getInfo());
    }

    public void addNode(Path node) {
        if (node.getIdentifier().isEmpty()) {
            apply(node);
            return;
        }

        Optional<Path> optional = getNode(node.getIdentifier());
        if (optional.isPresent()) {
            optional.get().apply(node);
        } else {
            this.nodes.add(node);
        }
    }

    public void addAliases(@NotNull Collection<String> aliases) {
        aliases.stream()
          .filter(alias -> !identifier.equals(alias))
          .map(String::toLowerCase)
          .forEach(this.aliases::add);
    }

    /**
     * Parses the giving string into a tree of nodes.
     *
     * @param info the command info path to parse
     * @return an entry where the first key is the head path,
     * and the value is the tail.
     */
    public static Map.Entry<Path, Path> ofCommandInfo(CommandInfo info) {
        if (info.path().isEmpty()) {
            Path path = new Path("", false, new HashSet<>());
            path.setInfo(info);

            return new AbstractMap.SimpleImmutableEntry<>(path, path);
        }

        AbstractMap.SimpleImmutableEntry<Path, Path> path = createPath(info.path());
        if (path == null) {
            throw new IllegalArgumentException("command name must not be empty");
        }

        path.getValue().setInfo(info);
        return path;
    }

    @Nullable
    private static AbstractMap.SimpleImmutableEntry<Path, Path> createPath(String string) {
        String[] parts = string.split(" ", 2);
        if (parts.length == 0) return null;

        List<String> names = new ArrayList<>(Arrays.asList(parts[0].split("\\|")));
        if (names.stream().anyMatch(String::isEmpty)) {
            throw new IllegalArgumentException("aliases must not be empty");
        }

        String identifier = names.remove(0);
        HashSet<String> aliases = new HashSet<>(names);

        boolean isInput = identifier.startsWith(":");
        Path path = new Path(identifier.substring(isInput ? 1 : 0), isInput, aliases);

        Path current = path;
        if (parts.length > 1) {
            Map.Entry<Path, Path> next = createPath(parts[1]);
            if (next != null) {
                current.addNode(next.getKey());
                current = next.getValue();
            }
        }

        return new AbstractMap.SimpleImmutableEntry<>(path, current);
    }
}
