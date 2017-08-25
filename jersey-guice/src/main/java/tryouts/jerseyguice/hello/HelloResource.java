package tryouts.jerseyguice.hello;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("hello")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
public class HelloResource {

    @Inject
    private HelloService helloService;

    @GET
    public Response call() {
        Hello hello = helloService.hello();
        return Response.status(Response.Status.OK).entity(hello).build();
    }
}
