package tryouts.dagger2.helloworld;

import dagger.Component;
import tryouts.dagger2.helloworld.DaggerHelloWorldApp_HelloWorld;

import javax.inject.Singleton;

public class HelloWorldApp {

    @Singleton
    @Component(modules = { HelloWorldModule.class })
    public interface HelloWorld {
        HelloWorldService service();
    }

    public static void main(String[] args) {
        HelloWorld  helloWorld = DaggerHelloWorldApp_HelloWorld.builder().build();

        helloWorld.service().sayHello();
    }
}
