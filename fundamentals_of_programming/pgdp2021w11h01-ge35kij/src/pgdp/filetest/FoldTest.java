package pgdp.filetest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pgdp.file.*;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class FoldTest {
    public static FoldImplementation fold = DummyFold::main;
    private static final String out = "-o=sandbox/out.txt";
    private static final String hello = "sandbox/hello.txt";
    private static final String pingu = "sandbox/pingu.txt";
    private static final String newLine = System.lineSeparator();

    // ! notes:
    // don't test with more than 4 args
    // test with 3 different widths
    // don't test folders
    // at least one input file exists
    // fold accept width with default of 80
    // fold functions independently of order of -o and -w
    // fold functions when no input (file ignored)
    // fold doesn't do anything with incorrect

    @BeforeEach
    public void prepSandbox() {
        deleteAllInSandbox();
        copyAllFromVaultToSandbox();
    }

    // roughly translated into functions from artemis page
    // !note: how to write tests
    // make string arr of args
    // out is always out.txt
    // run main with args
    // check if file is correct

    @Test
    public void testSingleFile() {
        // test single file
        // default width is wider than any line in hello

        String[] args = { hello, out };
        String initial = collectLines(hello);
        fold.main(args);
        assertEquals(initial, getOut());
    }

    @Test
    public void testFileOrder() {
        // check for correct processing order
        // both files have lines shorter than 80

        String[] args = { hello, out, pingu };
        fold.main(args);
        String helloInitial = collectLines(hello);
        String pinguInitial = collectLines(pingu);
        String whole = helloInitial + newLine + pinguInitial;
        assertEquals(whole, getOut());
    }

    @Test
    public void testWidth5() {
        // check hello file
        // width set to 5

        String[] args = new String[] { out, hello, "-w=5" };
        fold.main(args);
        String expected = "Fold on file sandbox/hello.txt:" + newLine + //
                "hello" + newLine + //
                "world" + newLine + //
                "!" + newLine + //
                "hello" + newLine + //
                "files" + newLine + //
                "!" + newLine + //
                "test" + newLine + //
                "test" + newLine + //
                "I lov" + newLine + //
                "e" + newLine + //
                "test" + newLine + //
                "pengu" + newLine + //
                "ins";
        assertEquals(expected, getOut());
    }

    // How the output for pingu.txt with width = 10 should look like
    private static final String pinguW10 = //
            "Fold on file sandbox/pingu.txt:" + newLine + //
                    "Ich liebe " + newLine + //
                    "Pinguine!" + newLine + //
                    "Wir würden" + newLine + //
                    " gerne in " + newLine + //
                    "die Antark" + newLine + //
                    "tis ziehen" + newLine + //
                    "!";

    @Test
    public void testWidth10() {
        // test file pingu
        // test for width = 10

        String[] args = { out, pingu, "-w=10" };
        fold.main(args);
        assertEquals(pinguW10, getOut());
    }

    @Test
    public void testInputFileNotExists() {
        // test when file does not exist
        // out file should be empty

        String[] args = { out, "sandbox/404.txt" };
        fold.main(args);
        assertEquals("", getOut());
    }

    @Test
    public void testWidthBeforeOut() {
        // test width 10 already confirmed that out before width works
        // now test width before out

        String[] args = { pingu, "-w=10", out };
        fold.main(args);
        assertEquals(pinguW10, getOut());
    }

    @Test
    public void testInvalidWidth0() {
        // test when width is 0

        String[] args = { out, pingu, "-w=0" };
        fold.main(args);
        assertTrue(Files.notExists(Path.of("sandbox/out.txt")));
    }

    @Test
    public void testInvalidWidthPingu() {
        // test when width is not a number

        String[] args = { out, pingu, "-w=Pingu" };
        fold.main(args);
        assertTrue(Files.notExists(Path.of("sandbox/out.txt")));
    }

    @Test
    public void testInvalidWidthMinus10() {
        // test when width is smaller than 0

        String[] args = { out, pingu, "-w=-10" };
        fold.main(args);
        assertTrue(Files.notExists(Path.of("sandbox/out.txt")));
    }

    private static String collectLines(String file) {
        try {
            return "Fold on file " + file + ":" + newLine
                    + Files.lines(Path.of(file)).collect(Collectors.joining(System.lineSeparator()));
        } catch (Exception e) {
            return null;
        }
    }

    private static String getOut() {
        try {
            return Files.lines(Path.of("sandbox/out.txt")).collect(Collectors.joining(System.lineSeparator()));
        } catch (Exception e) {
            return null;
        }
    }

    // Helper Methods
    // this methods recursively deletes everything in the directory
    private static void deleteAllInSandbox() {
        // traverse the sandbox directory in reverse order (to delete deepest first)
        try (Stream<Path> stream = Files.walk(Path.of("sandbox"))) {
            stream.skip(1) // don't delete the sandbox folder itself
                    .sorted(Comparator.reverseOrder()).forEach(file -> {
                        try {
                            Files.delete(file);
                        } catch (IOException e) {
                            throw new TestSetupFailedException("Error trying to delete test file: " + e);
                        }
                    });
        } catch (IOException | UncheckedIOException e) {
            throw new TestSetupFailedException("Error trying to clean up the sandbox: " + e);
        }
    }

    // this method copies everything inside the file_vault into the sandbox
    // recursively
    private static void copyAllFromVaultToSandbox() {
        Path source = Path.of("file_vault"), destination = Path.of("sandbox");
        // traverse the directory and copy every file and folder within it
        try (Stream<Path> stream = Files.walk(source)) {
            stream.skip(1) // don't copy the files folder itself
                    .forEach(file -> {
                        try {
                            Files.copy(file, destination.resolve(source.relativize(file)));
                        } catch (IOException e) {
                            throw new TestSetupFailedException("Error trying to copy test file: " + e);
                        }
                    });
        } catch (IOException | UncheckedIOException e) {
            throw new TestSetupFailedException("Error trying to walk over test files: " + e);
        }
    }
}
