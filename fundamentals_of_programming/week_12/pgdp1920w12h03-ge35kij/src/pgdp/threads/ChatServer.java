package pgdp.threads;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {

    private static int port = 3000;
    private static ServerSocket ss;
    private static Socket server;
    private static boolean acceptingUsers = false;
    private static ArrayList<ClientHandler> userHandlers = new ArrayList<ClientHandler>();
    private static int capacity = 50;

    public static void main(String[] args) {
        if (args.length > 0)
            port = Integer.parseInt(args[0]);
        start();
    }

    static void start() {
        try {
            ss = new ServerSocket(port);
            System.out.println("Starting server on port " + port);
            acceptUsers();

        } catch (Exception e) {
            System.out.print("Failed to start server. Reason: " + e.getLocalizedMessage());
        }
    }

    static void shutdown() {
        try {
            acceptingUsers = false;
            ss.close();
        } catch (Exception e) {
            System.out.println("Error on server shutdown.");
        }
    }

    private static void acceptUsers() {
        acceptingUsers = true;
        new Thread("Accepting users thread") {
            @Override
            public void run() {
                while (acceptingUsers) {
                    try {
                        server = ss.accept();
                        System.out.println("Accepting connection: " + server);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(server.getInputStream()));
                        PrintWriter writer = new PrintWriter(server.getOutputStream(), true);

                        if (userHandlers.size() == capacity) {
                            writer.println("Server full");
                            server.close();
                            reader.close();
                            writer.close();
                            break;
                        }

                        writer.println("Connection allowed");
                        ClientHandler user = new ClientHandler(reader, writer, userHandlers);
                        userHandlers.add(user);
                        user.start();

                    } catch (Exception e) {
                        System.out.println("Failed to accept user on the server. Reason: " + e.getLocalizedMessage());
                    }
                }
            }

        }.start();
    }
}
