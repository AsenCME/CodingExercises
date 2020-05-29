package pgdp.filetree;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class Directory extends File {

	private final List<File> files;

	public Directory(Path path, List<File> files) {
		super(path);
		this.files = files;
	}

	@Override
	public Iterator<File> iterator() {
		Directory dir = this;

		return new Iterator<File>() {
			Iterator<File> filesIterator = dir.getAllFiles().iterator();

			@Override
			public boolean hasNext() {
				return filesIterator.hasNext();
			}

			@Override
			public File next() {
				if (hasNext()) {
					return filesIterator.next();
				} else
					throw new NoSuchElementException("Tree has been iterated over.");
			}
		};
	}

	@Override
	public int getHeight() {
		if (files.isEmpty())
			return 0;
		return 1 + files.stream().map(f -> f.getHeight()).max(Integer::compareTo).get();
	}

	@Override
	public boolean isRegularFile() {
		return false;
	}

	public List<File> getFiles() {
		return files;
	}

	public List<File> getAllFiles() {
		ArrayList<File> iteratorList = new ArrayList<File>();
		iteratorList.add(this);
		for (File file : files)
			if (file.isRegularFile() && !iteratorList.contains(file))
				iteratorList.add(file);
			else
				iteratorList.addAll(((Directory) file).getAllFiles());
		return iteratorList;
	}
}
