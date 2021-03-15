package pgdp.sync;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.util.Map.Entry;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class FileSyncServer extends FileSyncParticipant {
    FileSyncServer(Path folder, int port) {
        super(folder, port);
    }

    private boolean getStatus() throws IOException {
        if (!in.readLine().equals(Status.SUCCESS.toString())) {
            System.err.println("Could not get client status");
            return false;
        }
        return true;
    }

    private Map<String, Instant> scanLocal() {
        Map<String, Instant> localFiles;
        try {
            localFiles = folderSynchronizer().scan();
        } catch (Exception e) {
            System.err.println("Could not scan local files");
            return null;
        }
        return localFiles;
    }

    private void synchornize() throws IOException {
        out.println(ServerCommand.LIST.toString());
        int count;
        try {
            if (!getStatus())
                return;
            count = Integer.parseInt(in.readLine());
        } catch (IOException e) {
            System.err.println("Could not receive status");
            return;
        } catch (NumberFormatException e) {
            System.err.println("File count could not be parsed");
            return;
        }

        var remoteFiles = new HashMap<String, Instant>();
        for (int i = 0; i < count; i++) {
            try {
                var entry = getFileInfo();
                remoteFiles.putIfAbsent(entry.getKey(), entry.getValue());
            } catch (Exception e) {
                System.err.println("Could not get the file at index " + i);
            }
        }

        // update stuff
        Map<String, Instant> localFiles;
        if ((localFiles = scanLocal()) == null)
            return;

        var newerRemote = difference(remoteFiles, localFiles);
        for (var entry : newerRemote.entrySet())
            getFileFromClient(entry.getKey());

        if ((localFiles = scanLocal()) == null)
            return;

        var newerLocal = difference(localFiles, remoteFiles);
        for (var entry : newerLocal.entrySet())
            sendFileToClient(entry);
    }

    private void getFileFromClient(String file) {
        try {
            out.println(ServerCommand.GET.toString());
            out.println(file);
            if (!getStatus())
                return;
            receiveAndUpdateFile(file);
        } catch (Exception e) {
            System.err.println("Could not get file " + file);
        }
    }

    private void sendFileToClient(Entry<String, Instant> entry) {
        String file = entry.getKey();

        // Make sure file can be read
        var content = readFile(file);
        if (content == null)
            return;

        // Send file contents
        out.println(ServerCommand.UPDATE.toString());
        out.println(file);
        sendFileContent(content);
    }

    private Map<String, Instant> difference(Map<String, Instant> a, Map<String, Instant> b) {
        return a.entrySet().stream().filter(x -> {
            boolean exists = b.containsKey(x.getKey());
            if (!exists)
                return true;
            var date = b.get(x.getKey());
            return date.compareTo(x.getValue()) < 0;
        }).collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }

    @Override
    void run() {
        try (ServerSocket server = new ServerSocket(port)) {
            while (true) {
                try (Socket client = server.accept()) {
                    openIO(client);
                    while (!client.isClosed()) {
                        synchornize();
                        FileSyncUtil.sleepFiveSeconds();
                    }
                    in.close();
                    out.close();
                } catch (Exception e) {
                    System.err.println("Client connection error. " + e);
                }
            }
        } catch (Exception e) {
            System.err.println("Server connection error. " + e);
        }
    }

    public static void main(String[] args) {
        if (args.length != 2)
            throw new IllegalArgumentException("Invalid number of arguments");
        Path folder = Path.of(args[0]);
        int port = Integer.parseInt(args[1]);
        new FileSyncServer(folder, port).run();
    }
}
