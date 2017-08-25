package tryouts.java.memoizer;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class Main {

    public static void main(String[] args) {

        Function<Integer, Integer> f = x -> {
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(1));
            } catch (InterruptedException ignored) {
            }
            return x * 2;
        };
        Function<Integer, Integer> g = Memoizer.memoize(f);

        long start = System.currentTimeMillis();
        Integer g1stResult = g.apply(2);
        long g1stDuration = System.currentTimeMillis() - start;

        start = System.currentTimeMillis();
        Integer g2ndResult = g.apply(2);
        long g2ndDuration = System.currentTimeMillis() - start;

        System.out.println("g(2) first  call => result: " + g1stResult + ", duration: " + g1stDuration);
        System.out.println("g(2) second call => result: " + g2ndResult + ", duration: " + g2ndDuration);
    }

}
