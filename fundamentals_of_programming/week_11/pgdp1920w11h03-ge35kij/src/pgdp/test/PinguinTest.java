package pgdp.test;

// JUnit packages
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.fail;

// Other packages
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PinguinTest {
    private final int port = 25565;

    /**
     * Sends an acceptable response to the penguin <br/>
     * Checks if the penguin answers with the same
     */
    @Test
    public void echtuinShouldAnswerWithTheSameGoodbye() {
        try (Socket server = new Socket("localhost", port);
                BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
                PrintWriter out = new PrintWriter(server.getOutputStream(), true);) {

            // Acceptable exit responses
            String[] goodbyeMessages = new String[] { "Bye", "Servus", "Goodbye", "Bis dann" };

            // Get random goodbye message
            String message = goodbyeMessages[new Random(System.currentTimeMillis()).nextInt(goodbyeMessages.length)];

            // Send message
            out.write(message);

            // Get response
            assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
                String response = in.readLine();

                // Check
                assertEquals(message, response, "Echtuin should respond in the same way.");
            }, "Test timeout, response took longer than 1 second.");

        } catch (Exception e) {
            System.out.println("An error has occured: " + e.getLocalizedMessage());
            fail("Could not connect");
        }
    }

    /**
     * Sends a random command to the penguin <br/>
     * Checks if the penguin recognizes the command <br/>
     * If no - penguin is Echtuin
     */
    @Test
    public void echtuinShouldNotRecognizeCommand() {
        try (Socket server = new Socket("localhost", port);
                BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
                PrintWriter out = new PrintWriter(server.getOutputStream(), true);) {

            // Random commands
            String[] messages = new String[] { "Sup!", "How old are you?", "Are you real?", "Go away!" };

            // Get random command
            String message = messages[new Random(System.currentTimeMillis()).nextInt(Math.abs(messages.length))];

            // Expected response
            String expectedResponse = "Tut mir leid, das habe ich nicht verstanden.";

            // Write command
            out.write(message);
            out.write("These arguments should not matter.");

            // Get response
            assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
                String response = in.readLine();

                // Check
                assertEquals(expectedResponse, response, "Echtuin should not understand command.");
            }, "Test timeout, response took longer than 1 second.");

        } catch (Exception e) {
            System.out.println("An error has occured: " + e.getLocalizedMessage());
            fail("Could not connect");
        }
    }

    /**
     * Sends question about sum in correct form <br/>
     * Checks if the penguin returns a long in over 100ms <br/>
     * Makes sure the response is correct
     */
    @Test
    public void echtuinShouldCalculateSumCorrectly() {
        assertTimeoutPreemptively(Duration.ofSeconds(20), () -> {
            try (Socket server = new Socket("localhost", port);
                    BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
                    PrintWriter out = new PrintWriter(server.getOutputStream(), true);) {

                // Get random numbers
                NumbersDTO randomNumbers = getRandomNumbers();
                String question = "Was ergibt die folgende Summe?";
                String questionArgs = randomNumbers.message;
                long correctSum = randomNumbers.sum;

                assertTimeoutPreemptively(Duration.ofMillis(1100), () -> {
                    // Get time to calculate
                    long start = System.currentTimeMillis();
                    out.write(question);
                    out.write(questionArgs);
                    String response = in.readLine();
                    long end = System.currentTimeMillis();

                    // Responded too quickly
                    if (end - start < 100)
                        fail("Penguin responded too quickly.");

                    try {
                        // Try parse value as long
                        long penguinSum = Long.parseLong(response);

                        // Check if sum is correct
                        assertEquals(correctSum, penguinSum, "Echtuin should calculate sum correctly.");
                    } catch (Exception e) {
                        fail("Echtuin should return number of type long.");
                    }
                }, "Test timeout, response took longer than 1 second.");

            } catch (Exception e) {
                System.out.println("An error has occured: " + e.getLocalizedMessage());
                fail("Could not connect");
            }
        });
    }

    /**
     * Sends question about sum in incorrect form <br/>
     * Checks if the penguin responds in under 50ms <br/>
     * Makes sure the response is correct
     */
    @Test
    public void echtuinShouldRecognizeIncorrectSumArguments() {
        try (Socket server = new Socket("localhost", port);
                BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
                PrintWriter out = new PrintWriter(server.getOutputStream(), true);) {

            // Question
            String question = "Was ergibt die folgende Summe?";
            // Arguments
            String questionArgs = "1 + asbsad + c+as";

            // Get response
            assertTimeoutPreemptively(Duration.ofMillis(1050), () -> {
                // Get time to respond
                long start = System.currentTimeMillis();
                out.write(question);
                out.write(questionArgs);
                String response = in.readLine();
                long end = System.currentTimeMillis();

                // Did not respond in time
                if (end - start > 50)
                    fail("Penguin failed to respond in time.");

                // Check
                assertEquals("Das ist keine Summe!", response, "Echtuin should recognize incorrect arguments.");
            }, "Test timeout, response took longer than 1 second.");

        } catch (Exception e) {
            System.out.println("An error has occured: " + e.getLocalizedMessage());
            fail("Could not connect");
        }
    }

    /**
     * Checks if the penguin counts fish correctly <br/>
     * Message is always valid and consists of 0 to 100 valid words
     */
    @Test
    public void echtuinShouldCountFishCorrectly() {
        try (Socket server = new Socket("localhost", port);
                BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
                PrintWriter out = new PrintWriter(server.getOutputStream(), true);) {

            // Question
            String question = "Zähle die Fische in";

            // Generate correct message
            FishDTO randomFishCommand = getRandomFishMessage();
            int count = randomFishCommand.count;
            String messageArgs = randomFishCommand.message;

            // Write message
            out.write(question);
            out.write(messageArgs);

            // Get response
            assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
                String response = in.readLine();

                try {
                    // Try parse as int
                    int penguinCount = Integer.parseInt(response);

                    // Check if count is correct
                    assertEquals(count, penguinCount, "Echtuin should count fish correctly");
                } catch (Exception e) {
                    fail("Echtuin should return number of type int.");
                }
            }, "Test timeout, response took longer than 1 second.");

        } catch (Exception e) {
            System.out.println("An error has occured: " + e.getLocalizedMessage());
            fail("Could not connect");
        }
    }

    /**
     * Sends a random question, valid or invalid <br/>
     * Checks if the awaits second line with arguments
     */
    @Test
    public void echtuinShouldWaitForArguments() {
        assertTimeoutPreemptively(Duration.ofSeconds(20), () -> {
            try (Socket server = new Socket("localhost", port);
                    BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
                    PrintWriter out = new PrintWriter(server.getOutputStream(), true);) {

                // Valid + invalid questions
                String[] commands = new String[] { "Was ergibt die folgende Summe?", "Zähle die Fische in",
                        "How are you?" };

                // Get random comamnd
                String command = commands[new Random(System.currentTimeMillis()).nextInt(commands.length)];

                // Send command
                out.write(command);

                // Get response
                assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
                    String response = in.readLine();
                    // If readLine is not null, Echtuin has responded - fail
                    assertEquals(null, response, "Echtuin should wait for arguments.");
                });

            } catch (Exception e) {
                System.out.println("An error has occured: " + e.getLocalizedMessage());
                fail("Could not connect");
            }
        });
    }

    /**
     * Helper method <br/>
     * Generates a string of random numbers in correct form <br/>
     * Returns the correct sum as well
     */
    private NumbersDTO getRandomNumbers() {
        Random random = new Random(System.currentTimeMillis());
        List<Integer> randomNumbers = IntStream.range(0, 100).boxed().map(i -> random.nextInt(1000))
                .collect(Collectors.toList());
        long correctSum = randomNumbers.stream().mapToLong(i -> i).sum();
        String message = randomNumbers.stream().map(Long::toString).collect(Collectors.joining(" + "));

        return new NumbersDTO(correctSum, message);
    }

    /**
     * Helper method <br/>
     * Generates a a string of valid and invlaid fishWords <br/>
     * Returns the correct count
     */
    private FishDTO getRandomFishMessage() {
        String[] validFishWords = new String[] { "Fisch", "fisch", "PGfischDP", "ScoobyFischDoobyfischDo",
                "Fischyfisch" };
        Random random = new Random(System.currentTimeMillis());
        int fishCount = random.nextInt(100);
        String message = " ".repeat(random.nextInt(10)) + " invalidWord " + IntStream.range(0, fishCount).boxed()
                .map(i -> validFishWords[random.nextInt(validFishWords.length)]).collect(Collectors.joining("    "))
                + " invalid " + " ".repeat(random.nextInt(10));
        return new FishDTO(fishCount, message);
    }
}

class NumbersDTO {
    final long sum;
    final String message;

    NumbersDTO(long _sum, String _message) {
        sum = _sum;
        message = _message;
    }
}

class FishDTO {
    final int count;
    final String message;

    FishDTO(int _count, String _message) {
        count = _count;
        message = _message;
    }
}