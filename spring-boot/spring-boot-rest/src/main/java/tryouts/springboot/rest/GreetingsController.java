package tryouts.springboot.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GreetingsController {

    private final Logger logger = LoggerFactory.getLogger(GreetingsController.class);

    @Autowired
    private GreetingsConfig greetingsConfig;

    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        logger.info("Greeting for this name: '{}' responded", name);
        return new Greeting(counter.incrementAndGet(), String.format(greetingsConfig.getTemplate(), name));
    }

}
