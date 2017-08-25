package tryouts.java.functions;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public class TryConsumers {

    /**
     * A specialized consumer to print ints to stdout
     */
    class StdoutConsumer implements Consumer<Integer> {
        @Override
        public void accept(Integer integer) {
            System.out.println("[StdoutConsumer] value: " + integer);
        }
    }

    @Test
    public void testSpecializedConsumer() {

        List<Integer> integers = new ArrayList<>();
        integers.add(0);
        integers.add(1);
        integers.add(2);

        integers.forEach(new StdoutConsumer());
        // or
        integers.forEach(val -> System.out.println("[lambda] val: " + val));
    }

    /**
     * Use consumer for your class
     */
    class Age {
        final Integer age;

        public Age(Integer age) {
            this.age = age;
        }

        void doWhatever(IntConsumer consumer) {
            consumer.accept(this.age);
        }
    }

    @Test
    public void testAgeConsumer() {

        IntConsumer ageConsumer = (v) -> {
            if (v <= 10) {
                System.out.println("Child");
            } else if (v > 10 && v < 18) {
                System.out.println("Teenager");
            } else if (v >= 18 && v < 26) {
                System.out.println("Adolescent");
            } else {
                System.out.println("Old");
            }
        };

        final Age age1 = new Age(2);
        age1.doWhatever(ageConsumer);

        final Age age2 = new Age(11);
        age2.doWhatever(ageConsumer);

        final Age age3 = new Age(19);
        age3.doWhatever(ageConsumer);

        final Age age4 = new Age(39);
        age4.doWhatever(ageConsumer);
    }


}
