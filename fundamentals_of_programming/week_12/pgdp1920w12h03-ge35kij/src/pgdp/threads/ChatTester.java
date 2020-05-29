package pgdp.threads;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

public class ChatTester {

    @Test
    public void shouldRecognizePenguAndWhoisCommands() {
        ChatServer.start();
        testWithIO((in, out) -> {
            // Log in
            out.println("tester");
            String allowedResponse = in.readLine();
            String enterUserNameResponse = in.readLine();
            String successResponse = in.readLine();

            // Send command whois
            out.println("whois");
            String line1 = in.readLine(); // empty
            String line2 = in.readLine(); // header
            String line3 = in.readLine(); // tester is present
            String line4 = in.readLine(); // empty

            assertTrue(line3.startsWith(" -> tester"));

            // Send command pengu
            out.println("pengu");
            String fact = in.readLine(); // random fact
            assertTrue(fact.startsWith(String.format("[%s] tester", getCurrentTime())));
        });
        ChatServer.shutdown();
    }

    @Test
    public void dmWorksAsExpected() {
        ChatServer.start();
        testWithIO((in, out) -> {
            out.println("tester");
            String allowedResponse = in.readLine();
            String enterUserNameResponse = in.readLine();
            String successResponse = in.readLine();
        });
        testWithIO((in, out) -> {
            out.println("tester2");
            String allowedResponse = in.readLine();
            String enterUserNameResponse = in.readLine();
            String successResponse = in.readLine();

            // Send dm to tester
            out.println("@tester hey");
            String expectedResponse = String.format("[%s] You to tester: hey", getCurrentTime());
            String response = in.readLine();
            assertEquals(expectedResponse, response);

            // Send dm to non-existing user
            out.println("@random hey");
            expectedResponse = "random could not be found on the server";
            response = in.readLine();
            assertEquals(expectedResponse, response);
        });
        ChatServer.shutdown();
    }

    @Test
    public void logoutWorks() {
        ChatServer.start();
        testWithIO((in, out) -> {
            out.println("tester");
            String allowedResponse = in.readLine();
            String enterUserNameResponse = in.readLine();
            String successResponse = in.readLine();

            out.println("logout");
            String expectedResponse = "Goodbye!";
            String logoutResponse = in.readLine();
            String responseGoodbye = in.readLine();
            assertEquals(expectedResponse, responseGoodbye);
        });
        ChatServer.shutdown();
    }

    @Test
    public void shouldHandleEmptyOrExistingUsername() {
        ChatServer.start();
        testWithIO((in, out) -> {
            out.println("tester");
            String allowedResponse = in.readLine();
            String enterUserNameResponse = in.readLine();
            String successResponse = in.readLine();
        });
        testWithIO((in, out) -> {
            out.println("");
            String allowedResponse = in.readLine();
            String enterUserNameResponse = in.readLine();
            String failureResponse = in.readLine();
            String expectedResponse = "Enter username: ";
            assertEquals(expectedResponse, failureResponse);

            out.println("tester");
            expectedResponse = "Username already exists. Enter username: ";
            String usernameExistsResponse = in.readLine();
            assertEquals(expectedResponse, usernameExistsResponse);
        });
        ChatServer.shutdown();
    }

    static String getCurrentTime() {
        return LocalTime.now().toString().substring(0, 5);
    }

    static String expectedFormat(String message) {
        return String.format("_-* |SERVER| *-_ (%s) %s", getCurrentTime(), message);
    }

    static void testWithIO(IOBasedTest<BufferedReader, PrintWriter> testAction) {
        try (Socket s = new Socket("localhost", 3000)) {
            PrintWriter out = new PrintWriter(s.getOutputStream(), true, StandardCharsets.UTF_8);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8));
            testAction.runTest(in, out);
        } catch (IOException e) {
            fail("Network error");
        }
    }

    @FunctionalInterface
    public interface IOBasedTest<T, U> {
        void runTest(T t, U u) throws IOException;
    }
}