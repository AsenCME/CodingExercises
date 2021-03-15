package pgdp.function;

import java.util.function.BiFunction;
import java.util.function.Function;

public class FunctionCache {
    private static final int DEFAULT_CACHE_SIZE = 10_000;

    private FunctionCache() {
    }

    public static <T, R> Function<T, R> cached(Function<T, R> function, int maximalCacheSize) {
        return new Function<T, R>() {
            private final Cache<T, R> cache = new Cache<T, R>(maximalCacheSize);

            @Override
            public R apply(T t) {
                if (cache.containsKey(t))
                    return cache.get(t);
                R result = function.apply(t);
                cache.put(t, result);
                return result;
            }
        };
    }

    public static <T, R> Function<T, R> cached(Function<T, R> function) {
        return new Function<T, R>() {
            private final Cache<T, R> cache = new Cache<T, R>(DEFAULT_CACHE_SIZE);

            @Override
            public R apply(T t) {
                if (cache.containsKey(t))
                    return cache.get(t);
                R result = function.apply(t);
                cache.put(t, result);
                return result;
            }
        };
    }

    static class Pair<T, U> {
        private final T t;
        private final U u;

        Pair(T t, U u) {
            this.t = t;
            this.u = u;
        }

        @Override
        public int hashCode() {
            return t.hashCode() + u.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Pair<?, ?>) {
                Pair<?, ?> cast = (Pair<?, ?>) obj;
                if (cast.t == null || cast.u == null)
                    return false;
                var isValidT = this.t.getClass().isAssignableFrom(cast.t.getClass());
                var isValidU = this.u.getClass().isAssignableFrom(cast.u.getClass());
                if (!isValidT || !isValidU)
                    return false;
                var pair = ((Pair<T, U>) cast);
                return pair.t.equals(this.t) && pair.u.equals(this.u);
            } else
                return false;
        }

    }

    private static <T, U, R> R applyToBi(T t, U u, Cache<Pair<T, U>, R> cache, BiFunction<T, U, R> biFunction) {
        var pair = new Pair<T, U>(t, u);
        if (cache.containsKey(pair))
            return cache.get(pair);
        R result = biFunction.apply(t, u);
        cache.put(new Pair<T, U>(t, u), result);
        return result;
    }

    public static <T, U, R> BiFunction<T, U, R> cached(BiFunction<T, U, R> biFunction, int maximalCacheSize) {
        return new BiFunction<T, U, R>() {
            private final Cache<Pair<T, U>, R> cache = new Cache<>(maximalCacheSize);

            @Override
            public R apply(T t, U u) {
                return applyToBi(t, u, cache, biFunction);
            }
        };
    }

    public static <T, U, R> BiFunction<T, U, R> cached(BiFunction<T, U, R> biFunction) {
        return new BiFunction<T, U, R>() {
            private final Cache<Pair<T, U>, R> cache = new Cache<>(DEFAULT_CACHE_SIZE);

            @Override
            public R apply(T t, U u) {
                return applyToBi(t, u, cache, biFunction);
            }
        };
    }

    public static <T, R> Function<T, R> cachedRecursive(BiFunction<T, Function<T, R>, R> function,
            int maximalCacheSize) {
        return new Function<T, R>() {
            private final Cache<T, R> cache = new Cache<>(maximalCacheSize);

            @Override
            public R apply(T t) {
                if (cache.containsKey(t))
                    return cache.get(t);

                R result = function.apply(t, this);
                cache.put(t, result);
                return result;
            }
        };
    }

    public static <T, R> Function<T, R> cachedRecursive(BiFunction<T, Function<T, R>, R> function) {
        return new Function<T, R>() {
            private final Cache<T, R> cache = new Cache<>(DEFAULT_CACHE_SIZE);

            @Override
            public R apply(T t) {
                if (cache.containsKey(t))
                    return cache.get(t);

                R result = function.apply(t, this);
                cache.put(t, result);
                return result;
            }
        };
    }
}
