package tryouts.jerseyguice.hello;

import com.google.inject.servlet.GuiceFilter;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

import static org.junit.Assert.assertEquals;

public class HelloResourceTest {

    private static final Integer PORT = 8080;

    private Server server;
    private URI baseUri;

    @Before
    public void startServer() throws Exception {
        baseUri = UriBuilder.fromUri("http://localhost").port(PORT).build();
        server = new Server(PORT);

        ServletContextHandler contextHandler = new ServletContextHandler(server, "/api");
        contextHandler.addEventListener(new HelloAppInitializer());
        contextHandler.addFilter(GuiceFilter.class, "/*", null);
        contextHandler.addServlet(DefaultServlet.class, "/");

        server.start();
    }

    @After
    public void stopServer() throws Exception {
        if (server != null) {
            server.stop();
        }
    }


    @Test
    public void testHelloResource() {
        Client client = Client.create(new DefaultClientConfig());

        WebResource service = client.resource(baseUri);
        ClientResponse response = service
                .path("api")
                .path("hello")
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get(ClientResponse.class);


        assertEquals(200, response.getStatus());
        assertEquals("{\"msg\":\"Hello world!\"}", response.getEntity(String.class));
    }

}