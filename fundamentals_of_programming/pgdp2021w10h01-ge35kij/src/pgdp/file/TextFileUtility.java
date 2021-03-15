package pgdp.file;

import java.util.List;
import java.util.Optional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.stream.Stream;
import java.util.stream.Collectors;

//! notes:
// starts with "-" -> is a command
// otherwise 
// -> ends with extension => is file
// -> no extension => is directory
// save locally before printing or saving to file

//! Errors:
// files open/write/read independently (if one fails the others are still attempted)
// if args are wrong -> program does not run
// catch UncheckedIOException (paths), InvalidPathException (paths) and NumberFormatException (parsing ints)

public abstract class TextFileUtility {
    private final List<Path> inputPaths;
    private final Path outputFile;

    public TextFileUtility(String[] args) throws InvalidCommandLineArgumentException {
        // Parse in paths
        try {
            inputPaths = Stream.of(args).filter(x -> !x.startsWith("-")).map(Path::of).filter(x -> x != null)
                    .collect(Collectors.toList());
        } catch (InvalidPathException e) {
            throw new InvalidCommandLineArgumentException(e.getMessage());
        }

        // Parse out file
        String outPath = parseOption(args, "o", null);
        try {
            outputFile = outPath == null ? null : Path.of(outPath);
        } catch (InvalidPathException e) {
            throw new InvalidCommandLineArgumentException(e.getMessage());
        }
    }

    public void output(String str) {
        if (outputFile == null) {
            System.out.println(str);
            return;
        }

        try {
            if (Files.notExists(outputFile)) {
                if (outputFile.getParent() != null)
                    Files.createDirectories(outputFile.getParent());
                Files.createFile(outputFile);
            }
            Files.writeString(outputFile, str + System.lineSeparator(), StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception e) {
            System.err.println(String.format("Could not write string to %s", outputFile.toString()));
        }
    }

    /**
     * Make sure to use the right line separator. Make sure to handle exceptions
     * with opening and reading files.
     * 
     * @param file
     * @return string to be written to out file or empty string if failed
     */
    public abstract String applyToFile(Path file);

    private String callApply(Path path) {
        String res = "";
        if (Files.isDirectory(path))
            try {
                res = Files.walk(path).filter(x -> Files.isRegularFile(x)).map(this::applyToFile).filter(x -> x != null)
                        .collect(Collectors.joining(System.lineSeparator()));
            } catch (IOException e) {
                String msg = String.format("Could not go through files in folder %s. Reason: ", path, e.getMessage());
                System.err.println(msg);
            }
        else if (Files.isRegularFile(path))
            res = applyToFile(path);

        if (res.equals(""))
            return null;
        return res;
    }

    public void applyToAll() {
        String res = inputPaths.stream().map(this::callApply).filter(x -> x != null)
                .collect(Collectors.joining(System.lineSeparator()));
        output(res);
    }

    public static String parseOption(String[] args, String option, String defaultValue) {
        String optionFormat = String.format("-%s", option);
        Optional<String> found = Stream.of(args).filter(x -> x.startsWith(optionFormat)).findFirst();
        if (!found.isPresent())
            return defaultValue;
        String[] parts = found.get().split("=", 2);
        if (parts.length != 2)
            return defaultValue;
        return parts[1];
    }
}
