package pgdp.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class Cat extends TextFileUtility {

    public Cat(String[] args) throws InvalidCommandLineArgumentException {
        super(args);
    }

    @Override
    public String applyToFile(Path file) {
        try {
            return Files.lines(file).collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return "";
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Not enough args. Usage: Cat <file>, <file>, ...");
            return;
        }

        try {
            var command = new Cat(args);
            command.applyToAll();
        } catch (InvalidCommandLineArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

}
