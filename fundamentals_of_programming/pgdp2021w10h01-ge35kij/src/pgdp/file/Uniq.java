package pgdp.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class Uniq extends TextFileUtility {

    public Uniq(String[] args) throws InvalidCommandLineArgumentException {
        super(args);
    }

    @Override
    public String applyToFile(Path file) {
        try {
            String header = String.format("Uniq on file %s:", file.toString()) + System.lineSeparator();
            return header + Files.lines(file).distinct().collect(Collectors.joining(System.lineSeparator()));
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
            var command = new Uniq(args);
            command.applyToAll();
        } catch (InvalidCommandLineArgumentException e) {
            System.err.println(e.getMessage());
        }
    }
}
