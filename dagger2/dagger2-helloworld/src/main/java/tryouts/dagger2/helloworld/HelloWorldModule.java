package tryouts.dagger2.helloworld;

import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class HelloWorldModule {

    @Provides
    @Singleton
    HelloWorldPrinter providePrinter() {
        return new SysOutHelloWorldPrinter();
    }
}
