package pgdp.threads;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
    // Server info
    private static int port = 3000;
    private static String serverIp = "localhost";
    private static Socket server;

    // Server communication
    private static BufferedReader serverReader;
    private static PrintWriter serverWriter;

    // Local handlers
    private static String username;
    private static boolean running = false;
    private static Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        start(args);
    }

    public static void start(String[] args) {
        checkArguments(args);
        connect();
    }

    private static void checkArguments(String[] args) {
        try {
            if (args.length > 0) {
                port = Integer.parseInt(args[0]);
                serverIp = args[1];
            }
        } catch (Exception e) {
            print("Incorrectly entered arguments.");
        }
    }

    private static void connect() {
        setupServerCommuncation();
        awaitUsername();
        listen();
    }

    private static void setupServerCommuncation() {
        try {
            server = new Socket(serverIp, port);
            serverReader = new BufferedReader(new InputStreamReader(server.getInputStream()));
            serverWriter = new PrintWriter(server.getOutputStream(), true);
            String response = serverReader.readLine().trim();
            while (response == null || response.isEmpty()) {
                response = serverReader.readLine().trim();
            }
            if (!response.equals("Connection allowed")) {
                System.out.println("Server is full sowwy! >.<");
                shutdown();
            }
        } catch (Exception e) {
            System.out.println("Error occured during communcation setup");
            shutdown();
        }
    }

    private static void awaitUsername() {
        try {
            String response = serverReader.readLine();
            while (response.equals("Username already exists. Enter username: ")
                    || response.equals("Enter username: ")) {
                System.out.print(response);
                username = in.nextLine().trim();

                while (username == null || username.isEmpty()) {
                    System.out.print(response);
                    username = in.nextLine().trim();
                }

                serverWriter.println(username);
                response = serverReader.readLine();
            }
        } catch (Exception e) {
            print("Error occured while getting username.");
            running = false;
            return;
        }
        greetUser();
        running = true;
    }

    private static void greetUser() {
        print("Welcome to CraftersParadise!\nRules:\n 1. No griefing.\n 2. No foul language (this will not be enforced).\n 3. Have fun!");
        print("Available commands:\n - @username message: Sends DM\n - whois: list users\n - logout: exit chatroom\n - pengu: send users a random fact");
    }

    private static void listen() {
        sendMessages();
        receiveMessages();
    }

    private static void receiveMessages() {
        new Thread("Message reciever for " + username) {
            @Override
            public void run() {
                while (running) {
                    try {
                        String message = serverReader.readLine().trim();
                        while (message == null || message.isEmpty())
                            message = serverReader.readLine().trim();

                        if (message.toLowerCase().equals("logout")) {
                            break;
                        }
                        print(message);
                    } catch (Exception e) {
                        running = false;
                        print("Error occured. Reason: " + e.getLocalizedMessage());
                    }
                }
                shutdown();
            }
        }.start();
    }

    private static void sendMessages() {
        new Thread("Message sender for " + username) {
            @Override
            public void run() {
                while (running) {
                    try {
                        String line = in.nextLine().trim();
                        while (line == null || line.isEmpty())
                            line = in.nextLine().trim();
                        broadcast(line);
                    } catch (Exception e) {
                        running = false;
                        print("Error occured. Reason: " + e.getLocalizedMessage());
                    }
                }
            }
        }.start();
    }

    private static void shutdown() {
        running = false;
        print("*** Goodbye! See you soon. ***");
        try {
            server.close();
            serverReader.close();
            serverWriter.close();
            System.exit(0);
        } catch (Exception e) {
            System.out.println("Error during logout");
        }
    }

    private static void print(String s) {
        System.out.println(s);
    }

    private static void broadcast(String s) {
        if (s.isEmpty())
            return;
        serverWriter.println(s);
    }

}
