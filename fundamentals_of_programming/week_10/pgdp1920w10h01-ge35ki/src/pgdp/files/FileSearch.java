package pgdp.files;

// NIO Imports
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

// Util Imports
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FileSearch {

    public static Result searchFile(Path file, String[] searched) {
        Result result = new Result(file);
        try {
            List<String> lines = Files.lines(file).collect(Collectors.toList());
            for (int i = 0; i < lines.size(); i++) {
                String currentLine = lines.get(i);
                for (String s : searched) {
                    boolean hasMatch = false;
                    if (currentLine.contains(s)) {
                        result.addMatch(new Match(i + 1, currentLine));
                        hasMatch = true;
                        break;
                    }
                    if (hasMatch)
                        break;
                }
            }

        } catch (java.io.IOException e) {
            return null;
        }
        return result;
    }

    public static Set<Result> searchDirectory(Path directory, String searched[]) {
        Set<Result> results = new HashSet<Result>();
        try {
            Files.walk(directory).filter(p -> !Files.isSymbolicLink(p) && Files.isRegularFile(p))
                    .forEach(dir -> results.add(searchFile(dir, searched)));
        } catch (java.io.IOException e) {
            System.out.println("The provided path does not exist.");
            return null;
        }
        return results;
    }

    public static List<Result> listResults(String directory, String searched[]) {
        return searchDirectory(Paths.get(directory), searched).stream()
                .sorted(Comparator.comparingInt((Result r) -> r.getMatches().size()).reversed())
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        String path = args[0];
        String[] searched = new String[args.length - 1];
        for (int i = 1; i < args.length; i++)
            searched[i - 1] = args[i];
        listResults(path, searched).forEach(result -> System.out.println(result));
    }

}
