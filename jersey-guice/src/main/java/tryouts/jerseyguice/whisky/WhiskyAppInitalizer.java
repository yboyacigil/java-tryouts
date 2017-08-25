package tryouts.jerseyguice.whisky;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import java.util.HashMap;
import java.util.Map;

public class WhiskyAppInitalizer extends GuiceServletContextListener {

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new JerseyServletModule() {

            @Override
            protected void configureServlets() {
                install(new WhiskyModule());

                bind(WhiskyResource.class).in(Singleton.class);
                bind(JacksonJsonProvider.class).in(Singleton.class);

                Map<String, String> params = new HashMap<>();
                params.put("com.sun.jersey.spi.container.ContainerRequestFilters", "com.sun.jersey.api.container.filter.LoggingFilter");
                params.put("com.sun.jersey.spi.container.ContainerResponseFilters", "com.sun.jersey.api.container.filter.LoggingFilter");
                serve("/*").with(GuiceContainer.class, params);
            }
        });
    }
}
