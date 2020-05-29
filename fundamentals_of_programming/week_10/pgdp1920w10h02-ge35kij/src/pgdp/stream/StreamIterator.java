package pgdp.stream;

import java.util.Collection;
import java.util.OptionalLong;

interface StreamIterator<T> {
	abstract boolean hasNext();

	abstract StreamElement<T> next();

	abstract OptionalLong getSize();

	static <T> StreamIterator<T> of(Collection<T> collection) {
		return new StreamIterator<T>() {
			@Override
			public boolean hasNext() {
				return collection.iterator().hasNext();
			}

			@Override
			public StreamElement<T> next() {
				return new StreamElement<T>(collection.iterator().next());
			}

			@Override
			public OptionalLong getSize() {
				long size = 0;
				while (collection.iterator().hasNext())
					size++;
				return OptionalLong.of(size);
			}

		};
	}

	static <T> StreamIterator<T> of(Iterable<T> iterable) {
		return new StreamIterator<T>() {

			@Override
			public boolean hasNext() {
				return iterable.iterator().hasNext();
			}

			@Override
			public StreamElement<T> next() {
				return new StreamElement<T>(iterable.iterator().next());
			}

			@Override
			public OptionalLong getSize() {
				long size = 0;
				while (iterable.iterator().hasNext())
					size++;
				return OptionalLong.of(size);
			}
		};
	};

	static <T> StreamIterator<T> of(T[] arr) {
		return new StreamIterator<T>() {
			int index = 0;

			@Override
			public boolean hasNext() {
				if (index >= arr.length)
					return false;
				return true;
			}

			@Override
			public StreamElement<T> next() {
				return new StreamElement<T>(arr[index++]);
			}

			@Override
			public OptionalLong getSize() {
				return OptionalLong.of(arr.length);
			}
		};
	};

	static <T> StreamIterator<T> of(java.util.stream.Stream<T> stream) {
		return new StreamIterator<T>() {

			@Override
			public boolean hasNext() {
				return stream.iterator().hasNext();
			}

			@Override
			public StreamElement<T> next() {
				return new StreamElement<T>(stream.iterator().next());
			}

			@Override
			public OptionalLong getSize() {
				long size = 0;
				while (stream.iterator().hasNext()) {
					size++;
				}
				return OptionalLong.of(size);
			}
		};
	};
}