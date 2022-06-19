package me.saiintbrisson.minecraft.command.path;

import lombok.Getter;
import lombok.Setter;
import me.saiintbrisson.minecraft.command.handlers.CommandHandler;
import me.saiintbrisson.minecraft.command.handlers.CompleterHandler;
import me.saiintbrisson.minecraft.command.handlers.ExceptionHandler;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author Luiz Carlos Carvalho
 */
@Getter
public final class Path {
    private final String identifier;
    @Setter
    private PathInfo info;

    private final boolean input;

    private final Set<Path> nodes;
    private final Set<String> aliases;

    @Setter
    private CommandHandler commandHandler;
    @Setter
    private CompleterHandler completerHandler;
    private final Map<Class<?>, ExceptionHandler> exceptionHandlers;

    public Path(@NotNull String identifier, PathInfo info, boolean input, @NotNull Set<String> aliases) {
        this.identifier = identifier;
        this.info = info;

        this.input = input;

        if (input && !aliases.isEmpty()) {
            throw new IllegalArgumentException("input paths must not have aliases");
        }

        this.aliases = aliases;

        this.nodes = new TreeSet<>((o1, o2) ->
          o1 == o2 ? 0 : o1.isInput() ? 1 : -1
        );

        this.exceptionHandlers = new HashMap<>();
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
     * @param path the path to follow.
     * @return an iterator that goes through every path
     * until it reaches the tail.
     */
    public static Iterator<Path> createPathResolver(String path) {
        return new PathResolver(path);
    }

    @Override
    public String toString() {
        return "Path{" +
          "identifier='" + identifier + '\'' +
          ", info=" + info +
          ", nodes=" + nodes +
          '}';
    }

    private static class PathResolver implements Iterator<Path> {
        private final String concatenatedPath;
        private final String[] pathSegments;
        private int currentIdx;

        public PathResolver(String concatenatedPath) {
            this.concatenatedPath = concatenatedPath;
            this.pathSegments = concatenatedPath.split(" ");
            this.currentIdx = 0;
        }

        @Override
        public boolean hasNext() {
            return currentIdx < pathSegments.length;
        }

        @Override
        public Path next() {
            int pathI = ordinalIndexOf(concatenatedPath, " ", currentIdx + 1);
            if (pathI == -1) pathI = concatenatedPath.length();

            PathInfo info = new PathInfo(concatenatedPath.substring(0, pathI));
            String path = pathSegments[currentIdx++];
            boolean isInput = path.startsWith(":");

            List<String> names = new ArrayList<>(Arrays.asList(path.split("\\|")));
            if (names.stream().anyMatch(String::isEmpty)) {
                throw new IllegalArgumentException("alias must not be empty");
            }

            String identifier = names.remove(0);
            Set<String> aliases = new HashSet<>(names);

            return new Path(identifier.substring(isInput ? 1 : 0), info, isInput, aliases);
        }

        private static int ordinalIndexOf(String str, String subStr, int n) {
            int pos = str.indexOf(subStr);
            while (--n > 0 && pos != -1)
                pos = str.indexOf(subStr, pos + 1);
            return pos;
        }
    }
}
