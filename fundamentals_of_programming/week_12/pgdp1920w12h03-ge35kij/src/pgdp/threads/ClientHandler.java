package pgdp.threads;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ClientHandler extends Thread {
    private final BufferedReader reader;
    private final PrintWriter writer;
    private ArrayList<ClientHandler> users;

    private final LocalTime loggedInAt;
    private String username;
    private boolean isActive = false;

    public ClientHandler(BufferedReader _reader, PrintWriter _writer, ArrayList<ClientHandler> _users) {
        reader = _reader;
        writer = _writer;
        users = _users;

        loggedInAt = LocalTime.now();
        isActive = true;

        try {
            writer.println("Enter username: ");
            username = reader.readLine().trim();
            while (username.isEmpty()) {
                log("Attempted login, username was empty");
                writer.println("Enter username: ");
                username = reader.readLine().trim();
            }
            while (users.stream().map(u -> u.getUsername()).collect(Collectors.toList()).contains(username)) {
                log("Attempted login, username exists");
                writer.println("Username already exists. Enter username: ");
                username = reader.readLine().trim();
                while (username.isEmpty()) {
                    log("Attempted login, username was empty");
                    username = reader.readLine().trim();
                }
            }
            writer.println("Success");
        } catch (Exception e) {
            System.out.println("Error occured on login.");
        }

        announceLogin();
        log(username + " logged in");
    }

    @Override
    public void run() {
        while (isActive) {
            try {
                String line = reader.readLine().trim();
                while (line == null || line.isEmpty())
                    line = reader.readLine().trim();
                if (line.startsWith("@")) {
                    String[] dm = line.split("\\s");
                    String otherUser = dm[0].substring(1);
                    String message = Arrays.asList(dm).stream().skip(1).collect(Collectors.joining(" "));
                    dmUser(otherUser, message);
                } else if (line.toLowerCase().equals("logout")) {
                    logout();
                    break;
                } else if (line.toLowerCase().equals("whois")) {
                    whois();
                } else if (line.toLowerCase().equals("pengu")) {
                    randomFact();
                } else {
                    broadcastToAll(line);
                }
            } catch (Exception e) {
                writer.println("Error occured. Wumpus is sad.");
            }
        }
        try {
            writer.println("Goodbye!");
            reader.close();
            writer.close();
            users.remove(this);
        } catch (Exception e) {
            log("Error occured on user logout.");
        }
    }

    // Announce login
    private void announceLogin() {
        for (ClientHandler user : users)
            user.writer.println(
                    String.format("[%s]: %s joined the server.", loggedInAt.toString().substring(0, 5), username));
    }

    // Send message to all users
    private void broadcastToAll(String message) {
        String time = getCurrentTime();
        for (ClientHandler user : users)
            user.writer.println(String.format("[%s] %s: %s", time, username, message));

        log(username + " sent a message: " + message);
    }

    // DM a specific user
    private void dmUser(String targetUsername, String message) {
        if (targetUsername.equals(username)) {
            writer.println("You (from a different timeline) to (present) you: " + message);
            return;
        }
        boolean dmSent = false;
        String time = getCurrentTime();
        for (ClientHandler user : users) {
            if (user.username.equals(targetUsername)) {
                writer.println(String.format("[%s] You to %s: %s", time, targetUsername, message));
                user.writer.println(String.format("[%s] %s whispers to you: %s", time, username, message));
                dmSent = true;
                log(String.format("%s whispers to %s: %s", username, targetUsername, message));
                break;
            }
        }
        // If we get here -> user does not exist
        if (!dmSent) {
            writer.println(targetUsername + " could not be found on the server.");
            log(String.format("%s tried to DM %s - %s", username, targetUsername, message));
        }
    }

    // Logout user
    private void logout() {
        isActive = false;
        for (ClientHandler user : users) {
            if (user.username.equals(username))
                user.writer.println("logout");
            else
                user.writer.println(username + " left the chat");
        }

        log(username + " logged out");
    }

    // List all users currently active
    private void whois() {
        String allUsers = "\nAll currently active users:\n";
        for (ClientHandler user : users)
            allUsers += String.format(" -> %s (since %s)\n", user.username, user.loggedInAt);
        writer.println(allUsers);

        log(username + " requested a list of all users");
    }

    // Send random fact from API
    private void randomFact() {
        String uri = "https://uselessfacts.jsph.pl/random.json?language=en";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(uri)).build();

        String fact = "";
        try {
            String response = client.send(request, BodyHandlers.ofString()).body();
            Matcher m = Pattern.compile("\"text\":\"(.*)\",\"source\"").matcher(response);
            if (m.find())
                fact = m.group(1);
        } catch (Exception e) {
            log("Error getting random fact.");
        }

        broadcastToAll(fact);
        log(username + " sent a random fact");
    }

    // Get current time
    private String getCurrentTime() {
        return LocalTime.now().toString().substring(0, 5);
    }

    // Username getter
    public String getUsername() {
        return username;
    }

    // Log to server
    private void log(String message) {
        System.out.println(String.format("_-* |SERVER| *-_ (%s) %s", getCurrentTime(), message));
    }

}