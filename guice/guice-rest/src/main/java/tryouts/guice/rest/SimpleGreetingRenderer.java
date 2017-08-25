package tryouts.guice.rest;

import com.google.common.base.Strings;

import java.util.concurrent.atomic.AtomicLong;

public class SimpleGreetingRenderer implements GreetingRenderer {

    private final AtomicLong counter = new AtomicLong();

    @Override
    public Greeting render(String name) {
        String who = name;
        if (Strings.isNullOrEmpty(name)) {
            who = "World";
        }
        return new Greeting(counter.incrementAndGet(), String.format("Hello, %s!", who));
    }
}
