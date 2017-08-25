package tryouts.jerseyguice.hello;

public class HelloService {

    public Hello hello() {
        return new Hello("Hello world!");
    }
}
