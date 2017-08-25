package tryouts.guice.rest;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("greeting")
@Singleton
public class GreetingService {

    private final GreetingRenderer renderer;

    @Inject
    GreetingService(GreetingRenderer renderer) {
        this.renderer = renderer;
    }

    @GET
    @Produces(APPLICATION_JSON)
    public Greeting greeting(@QueryParam("name") String name) {
        return this.renderer.render(name);
    }


}
