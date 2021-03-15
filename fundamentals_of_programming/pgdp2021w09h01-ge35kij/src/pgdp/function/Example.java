package pgdp.function;

import java.math.BigInteger;
import java.util.function.Function;

/**
 * You can modify the code in here, this will not be tested.
 */
public class Example {

	public static void main(String[] args) {
		Function<BigInteger, BigInteger> fib = FunctionCache
				.cachedRecursive((n, f) -> n.compareTo(BigInteger.TWO) <= 0 ? BigInteger.ONE
						: f.apply(n.subtract(BigInteger.ONE)).add(f.apply(n.subtract(BigInteger.TWO))));

		var start = System.currentTimeMillis();
		System.out.println(fibNormal(BigInteger.valueOf(20L)));
		var end = System.currentTimeMillis();
		System.out.println("No-cache fib took " + (end - start) + " ms");
		start = System.currentTimeMillis();
		System.out.println(fib.apply(BigInteger.valueOf(3L)));
		end = System.currentTimeMillis();
		System.out.println("Cache fib took " + (end - start) + " ms");
	}

	public static BigInteger fibNormal(BigInteger n) {
		return n.compareTo(BigInteger.TWO) <= 0 ? BigInteger.ONE
				: fibNormal(n.subtract(BigInteger.ONE)).add(fibNormal(n.subtract(BigInteger.TWO)));
	}
}
