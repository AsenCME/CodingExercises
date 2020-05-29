package pgdp.threads;

import java.math.BigInteger;

public class Factorial {

	public static BigInteger facSequential(int n) {
		if (n < 0)
			throw new IllegalArgumentException("The input must be non-negative.");

		BigInteger factorial = BigInteger.ONE;
		for (; n >= 2; n--)
			factorial = factorial.multiply(BigInteger.valueOf(n));
		return factorial;
	}

	public static BigInteger facParallel(int n, int threadCount) throws InterruptedException {
		if (n < 0)
			throw new IllegalArgumentException("The input must be non-negative");
		if (threadCount <= 0)
			throw new IllegalArgumentException("Method must create at least 1 thread");

		if (threadCount > n)
			threadCount = n;
		int portion = n / threadCount;
		int remainder = n % threadCount;

		BigInteger[] results = new BigInteger[threadCount];
		Thread[] threads = new Thread[threadCount];
		for (int i = 0; i < threadCount; i++) {
			int from = i * portion + 1;
			int to = from + portion;
			if (i == threadCount - 1)
				to += remainder;
			if (to > n)
				to = n;
			ParallelResult result = new ParallelResult(from, to, results, i);
			threads[i] = new Thread(result);
		}

		for (Thread t : threads)
			t.start();
		for (Thread t : threads)
			t.join();

		BigInteger finalResult = BigInteger.ONE;
		for (BigInteger b : results)
			finalResult = finalResult.multiply(b);
		return finalResult;
	}

}

class ParallelResult implements Runnable {
	private int from;
	private int to;
	private BigInteger[] results;
	private int index;

	ParallelResult(int _from, int _to, BigInteger[] _results, int _index) {
		from = _from;
		to = _to;
		results = _results;
		index = _index;
	}

	@Override
	public void run() {
		BigInteger localResult = BigInteger.ONE;
		for (int i = from + 1; i <= to; i++)
			localResult = localResult.multiply(BigInteger.valueOf(i));
		results[index] = localResult;
	}

}
