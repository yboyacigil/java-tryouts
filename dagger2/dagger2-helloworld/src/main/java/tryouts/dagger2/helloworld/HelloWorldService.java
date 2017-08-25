package tryouts.dagger2.helloworld;

import javax.inject.Inject;

public class HelloWorldService {

    private final HelloWorldPrinter helloWorldPrinter;

    @Inject
    public HelloWorldService(HelloWorldPrinter helloWorldPrinter) {
        this.helloWorldPrinter = helloWorldPrinter;
    }

    public void sayHello() {
        helloWorldPrinter.print();
    }
}
