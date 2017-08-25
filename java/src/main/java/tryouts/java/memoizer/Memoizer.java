package tryouts.java.memoizer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class Memoizer<T, R> {

    private Memoizer() {}

    public static <T, R> Function<T, R> memoize(final Function<T, R> function) {
        return new Memoizer<>().doMemoize(function);
    }

    private <T, R> Function<T, R> doMemoize(final Function<T, R> function) {
        Map<T, R> cache = new ConcurrentHashMap<>();
        return input -> cache.computeIfAbsent(input, function);
    }

}
