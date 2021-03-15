package pgdp.sync;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.time.Instant;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileTime;
import java.nio.file.Files;
import java.io.IOException;

public class FolderSynchronizer {
	private final Path folder;

	public FolderSynchronizer(Path folder) {
		this.folder = folder;
	}

	public Map<String, Instant> scan() throws IOException {
		var map = new HashMap<String, Instant>();
		Files.walk(folder).filter(x -> Files.isRegularFile(x)).forEach(x -> {
			try {
				var key = FileSyncUtil.pathToRelativeUri(folder, x);
				var value = Files.getLastModifiedTime(x).toInstant();
				map.putIfAbsent(key, value);
			} catch (Exception e) {
			}
		});
		return map;
	}

	private void writeContent(Path path, FileContent content) throws IOException {
		Files.writeString(path, content.getLines().stream().collect(Collectors.joining(System.lineSeparator()))
				+ System.lineSeparator(), StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
		Files.setLastModifiedTime(path, FileTime.from(content.getLastModifiedTime()));
	}

	public boolean updateIfNewer(String fileInFolderRelativeUri, FileContent fileContent) throws IOException {
		var path = FileSyncUtil.relativeUriToPath(folder, fileInFolderRelativeUri);
		if (Files.notExists(path)) { // does not exist
			if (path.getParent() != null)
				Files.createDirectories(path.getParent());
			Files.createFile(path);
			writeContent(path, fileContent);
		} else if (Files.getLastModifiedTime(path).compareTo(FileTime.from(fileContent.getLastModifiedTime())) < 0) {
			// local is before remote
			writeContent(path, fileContent);
		} else
			return false;
		return true;
	}

	public FileContent getFileContent(String fileInFolderRelativeUri) throws IOException {
		try {
			var path = FileSyncUtil.relativeUriToPath(folder, fileInFolderRelativeUri);
			var date = Files.getLastModifiedTime(path).toInstant();
			var lines = Files.lines(path).collect(Collectors.toList());
			return new FileContent(date, lines);
		} catch (Exception e) {
			throw new IOException(String.format("Could not obtain file content for %s", fileInFolderRelativeUri));
		}
	}
}
