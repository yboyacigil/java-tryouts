package tryouts.guice.rest;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import java.util.HashMap;

public class GreetingServiceContextListener extends GuiceServletContextListener {

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new JerseyServletModule() {

            @Override
            protected void configureServlets() {
                bind(GreetingService.class);
                bind(GreetingRenderer.class).to(SimpleGreetingRenderer.class).asEagerSingleton();
                bind(MessageBodyReader.class).to(JacksonJsonProvider.class);
                bind(MessageBodyWriter.class).to(JacksonJsonProvider.class);

                HashMap<String, String> options = new HashMap<>();
                options.put("com.sun.jersey.api.json.POJOMappingFeature", "true");
                options.put("com.sun.jersey.config.property.packages", "tryouts.guice.rest");

                serve("/*").with(GuiceContainer.class, options);
            }
        });
    }
}
