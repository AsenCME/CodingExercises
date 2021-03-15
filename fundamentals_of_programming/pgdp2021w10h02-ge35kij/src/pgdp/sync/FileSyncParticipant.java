package pgdp.sync;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

abstract class FileSyncParticipant {

	final int port;
	BufferedReader in;
	PrintWriter out;
	private final FolderSynchronizer folderSynchronizer;

	FileSyncParticipant(Path folder, int port) {
		if (Files.notExists(folder))
			throw new IllegalArgumentException("synchronization folder does not exist");
		folderSynchronizer = new FolderSynchronizer(folder);
		this.port = port;
	}

	abstract void run();

	void openIO(Socket socket) throws IOException {
		in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
		out = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8);
	}

	FileContent readFile(String file) {
		try {
			return folderSynchronizer().getFileContent(file);
		} catch (Exception e) {
			System.err.println("File read of " + file + " failed: " + e);
			return null;
		}
	}

	void sendFileContent(FileContent fileContent) {
		out.println(fileContent.getLastModifiedTime());
		out.println(fileContent.getLines().size());
		for (String line : fileContent.getLines()) {
			out.println(line);
		}
	}

	Entry<String, Instant> getFileInfo() throws IOException {
		String path = in.readLine();
		Instant lastModTime = Instant.parse(in.readLine());
		return new AbstractMap.SimpleEntry<String, Instant>(path, lastModTime);
	}

	void receiveAndUpdateFile(String file) throws IOException {
		Instant lastModTime = Instant.parse(in.readLine());
		int lineCount = Integer.parseInt(in.readLine());
		List<String> lines = new ArrayList<>();
		for (int i = 0; i < lineCount; i++)
			lines.add(in.readLine());
		FileContent fileContent = new FileContent(lastModTime, lines);
		try {
			boolean updated = folderSynchronizer().updateIfNewer(file, fileContent);
			if (updated)
				System.out.println("File " + file + " updated");
		} catch (Exception e) {
			System.err.println("File update of " + file + " failed: " + e);
		}
	}

	final FolderSynchronizer folderSynchronizer() {
		return folderSynchronizer;
	}
}
