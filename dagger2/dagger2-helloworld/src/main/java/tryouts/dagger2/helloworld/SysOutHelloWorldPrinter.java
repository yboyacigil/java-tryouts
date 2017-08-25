package tryouts.dagger2.helloworld;

public class SysOutHelloWorldPrinter implements HelloWorldPrinter {

    @Override
    public void print() {
        System.out.println("Hello world!");
    }
}
