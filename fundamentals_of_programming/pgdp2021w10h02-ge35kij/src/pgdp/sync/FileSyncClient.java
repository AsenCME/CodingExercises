package pgdp.sync;

import java.io.IOException;
import java.net.Socket;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Map;
import java.util.Map.Entry;

public class FileSyncClient extends FileSyncParticipant {
	private final String host;
	private boolean connected = false;

	public FileSyncClient(Path folder, String host, int port) {
		super(folder, port);
		this.host = host;
	}

	private void executeNextCommand() throws IOException {
		String line = null;
		try {
			line = in.readLine();
		} catch (IOException e) {
			System.err.print("Could not read command");
			connected = false;
			return;
		}

		if (line == null) { // indicates closed connection
			System.err.print("Server closed");
			connected = false;
			return;
		}

		try {
			var command = ServerCommand.valueOf(line);
			switch (command) {
				case LIST -> sendFileListToServer();
				case GET -> sendFileContentToServer();
				case UPDATE -> updateFileFromServer();
				default -> System.err.print("Unknown command from server: " + line);
			}
		} catch (Exception e) {
			System.err.print("Unknown command from server: " + line);
		}
	}

	@Override
	void run() {
		try (Socket client = new Socket(host, port)) {
			openIO(client);
			connected = true;
			while (connected)
				executeNextCommand();
			client.close();
			in.close();
			out.close();
		} catch (Exception e) {
			System.err.println("Connection error. " + e);
		}
	}

	private void sendFileListToServer() {
		Map<String, Instant> files;
		try {
			files = folderSynchronizer().scan();
		} catch (Exception e) {
			System.err.println("Folder scan failed: " + e);
			out.println(Status.FAILED);
			return;
		}
		out.println(Status.SUCCESS);
		out.println(files.size());
		for (Entry<String, Instant> file : files.entrySet()) {
			out.println(file.getKey());
			out.println(file.getValue());
		}
	}

	private void sendFileContentToServer() throws IOException {
		FileContent fileContent = readFile(in.readLine());
		if (fileContent == null) {
			out.println(Status.FAILED);
			return;
		}
		out.println(Status.SUCCESS);
		sendFileContent(fileContent);
	}

	private void updateFileFromServer() throws IOException {
		String file = in.readLine();
		receiveAndUpdateFile(file);
	}

	public static void main(String[] args) {
		if (args.length != 3)
			throw new IllegalArgumentException("Invalid number of arguments");
		Path folder = Path.of(args[0]);
		String host = args[1];
		int port = Integer.parseInt(args[2]);
		new FileSyncClient(folder, host, port).run();
	}

}
