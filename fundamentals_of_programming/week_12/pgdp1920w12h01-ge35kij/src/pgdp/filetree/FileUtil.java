package pgdp.filetree;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtil {

	public static File toFileRepresentation(Path path) throws IOException {
		if (Files.isRegularFile(path))
			return new RegularFile(path);
		return new Directory(path, getFiles(path));
	}

	private static List<File> getFiles(Path path) {
		try {
			return Files.list(path).map(file -> {
				if (Files.isRegularFile(file))
					return new RegularFile(file);
				else
					return new Directory(file, getFiles(file));
			}).collect(Collectors.toList());
		} catch (Exception e) {
			System.out.println("Problem with reading file tree: " + e.getLocalizedMessage());
			return null;
		}
	}

	public static void main(String[] args) {
		try {
			Directory tree = (Directory) FileUtil.toFileRepresentation(Paths.get("C:/pgdp_test"));
			for (File file : tree)
				System.out.println(file);
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		}
	}
}
