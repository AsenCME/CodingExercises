package pgdp.threads;

import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.util.function.BinaryOperator;
import java.util.function.Function;

public class ParallelCompute {

	public static BigInteger[] parallelComputeFunctions(BigInteger[] data, Function<BigInteger, BigInteger>[] functions,
			int threadCount) throws InterruptedException {
		if (data == null || functions == null)
			throw new NullPointerException("Data/Functions not provided");
		else if (data.length == 0 || functions.length == 0)
			throw new IllegalArgumentException("Data/Functions not provided");
		else if (data.length != functions.length)
			throw new IllegalArgumentException("Data and functions are of different lengths");
		else if (threadCount < 1)
			throw new IllegalArgumentException("Must create at least one thread");

		int length = data.length;
		if (threadCount > length)
			threadCount = length;
		int blockSize = length / threadCount;
		int remainder = length % threadCount;

		BigInteger[] result = new BigInteger[data.length];
		Thread[] threads = new Thread[threadCount];

		for (int blockIndex = 0; blockIndex < threadCount; blockIndex++) {
			int blockRangeFrom = blockIndex * blockSize;
			int blockRangeTo = blockRangeFrom + blockSize;
			if (blockIndex == threadCount - 1)
				blockRangeTo += remainder;

			threads[blockIndex] = new Thread(
					new ParallelComputation(data, functions, result, blockRangeFrom, blockRangeTo));
		}

		for (Thread t : threads)
			t.start();
		for (Thread t : threads)
			t.join();

		return result;
	}

	public static BigInteger parallelReduceArray(BigInteger[] data, BinaryOperator<BigInteger> binOp, int threadCount)
			throws InterruptedException {
		if (data == null || binOp == null)
			throw new NullPointerException("Data/Function not provided");
		else if (data.length == 0)
			throw new IllegalArgumentException("Data not provided");
		else if (threadCount < 1)
			throw new IllegalArgumentException("Must create at least one thread");

		int length = data.length;
		if (threadCount > length)
			threadCount = length;
		int blockSize = length / threadCount;
		int remainder = length % threadCount;

		BigInteger[] results = new BigInteger[threadCount];
		Thread[] threads = new Thread[threadCount];

		for (int blockIndex = 0; blockIndex < threadCount; blockIndex++) {
			int blockRangeFrom = blockIndex * blockSize;
			int blockRangeTo = blockRangeFrom + blockSize;
			if (blockIndex == threadCount - 1)
				blockRangeTo += remainder;
			threads[blockIndex] = new Thread(
					new ParallelReductor(data, binOp, blockRangeFrom, blockRangeTo, results, blockIndex));
		}

		for (Thread t : threads)
			t.start();
		for (Thread t : threads)
			t.join();

		BigInteger finalResult = results[0];
		for (int i = 1; i < results.length; i++)
			finalResult = binOp.apply(finalResult, results[i]);
		return finalResult;
	}

}

class ParallelComputation implements Runnable {
	BigInteger[] data;
	Function<BigInteger, BigInteger>[] functions;
	BigInteger[] result;
	int rangeFrom;
	int rangeTo;

	ParallelComputation(BigInteger[] _data, Function<BigInteger, BigInteger>[] _functions, BigInteger[] _result,
			int _from, int _to) {
		data = _data;
		functions = _functions;
		result = _result;
		rangeFrom = _from;
		rangeTo = _to;
	}

	@Override
	public void run() {
		for (int i = rangeFrom; i < rangeTo; i++)
			result[i] = functions[i].apply(data[i]);
	}
}

class ParallelReductor implements Runnable {
	BigInteger[] data;
	BinaryOperator<BigInteger> operator;
	int rangeFrom;
	int rangeTo;
	BigInteger[] results;
	int index;

	ParallelReductor(BigInteger[] _data, BinaryOperator<BigInteger> _operator, int _from, int _to,
			BigInteger[] _results, int _index) {
		data = _data;
		operator = _operator;
		rangeFrom = _from;
		rangeTo = _to;
		results = _results;
		index = _index;
	}

	@Override
	public void run() {
		BigInteger result = data[rangeFrom++];
		for (int i = rangeFrom; i < rangeTo; i++)
			result = operator.apply(result, data[i]);
		results[index] = result;
	}
}