package pgdp.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class Fold extends TextFileUtility {

    private final int width;
    private final static String DEFAULT_WIDTH = "80";

    public Fold(String[] args) throws InvalidCommandLineArgumentException {
        super(args);
        String w = parseOption(args, "w", DEFAULT_WIDTH);
        try {
            width = Integer.parseInt(w);
            if (width <= 0)
                throw new NumberFormatException("Width must be greater than 0");
        } catch (NumberFormatException e) {
            String msg = String.format("Invalid width argument. Reason: %s", e.getMessage());
            throw new InvalidCommandLineArgumentException(msg);
        }

    }

    private String formatLine(String line) {
        if (line.length() > width) {
            String res = "";
            String left = line;
            while (left.length() > width) {
                res += left.substring(0, width) + System.lineSeparator();
                left = left.substring(width);
            }
            return res + left;
        }
        return line;
    }

    @Override
    public String applyToFile(Path file) {
        try {
            String header = String.format("Fold on file %s:", file.toString()) + System.lineSeparator();
            return header + Files.lines(file).map(this::formatLine).collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return "";
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Not enough args. Usage: Uniq <file>, <file>, ...");
            return;
        }

        try {
            var command = new Fold(args);
            command.applyToAll();
        } catch (InvalidCommandLineArgumentException e) {
            System.err.println(e.getMessage());
        }
    }
}
