package tryouts.jerseyguice.whisky;

import com.google.inject.servlet.GuiceFilter;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class WhiskyResourceTest {

    private static final Integer PORT = 8080;

    private Server server;
    private URI baseUri;
    private DefaultClientConfig defaultClientConfig;

    @Before
    public void startServer() throws Exception {
        baseUri = UriBuilder.fromUri("http://localhost").port(PORT).build();
        server = new Server(PORT);

        defaultClientConfig = new DefaultClientConfig();
        defaultClientConfig.getClasses().add(JacksonJsonProvider.class);

        ServletContextHandler contextHandler = new ServletContextHandler(server, "/api");
        contextHandler.addEventListener(new WhiskyAppInitalizer());
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
    public void testGetAll() {
        Client client = Client.create(defaultClientConfig);

        WebResource service = client.resource(baseUri);
        ClientResponse response = service
                .path("api")
                .path("whiskies")
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get(ClientResponse.class);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(2, response.getEntity(List.class).size());
    }

    @Test
    public void testGetOne() {
        Client client = Client.create(defaultClientConfig);

        WebResource service = client.resource(baseUri);
        ClientResponse response = service
                .path("api")
                .path("whiskies")
                .path("1")
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get(ClientResponse.class);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Whisky whisky = response.getEntity(Whisky.class);
        assertNotNull(whisky);
        assertEquals("1", whisky.getId());
    }

    @Test
    public void testAddOne() {
        Client client = Client.create(defaultClientConfig);

        Whisky newWhisky = new Whisky("Jameson", "Ireland");

        WebResource service = client.resource(baseUri);
        ClientResponse response = service
                .path("api")
                .path("whiskies")
                .type(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .post(ClientResponse.class, newWhisky);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Whisky whisky = response.getEntity(Whisky.class);
        assertNotNull(whisky);
        assertNotNull(whisky.getId());
        assertEquals("Jameson", whisky.getName());
        assertEquals("Ireland", whisky.getOrigin());
    }

    @Test
    public void testUpdateOne() {
        Client client = Client.create(defaultClientConfig);

        Whisky existingWhisky = new Whisky("1", "Jameson", "Ireland");

        WebResource service = client.resource(baseUri);
        ClientResponse response = service
                .path("api")
                .path("whiskies")
                .path("1")
                .type(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .put(ClientResponse.class, existingWhisky);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Whisky whisky = response.getEntity(Whisky.class);
        assertNotNull(whisky);
        assertEquals("1", whisky.getId());
        assertEquals("Jameson", whisky.getName());
        assertEquals("Ireland", whisky.getOrigin());
    }

    @Test
    public void testDeleteOne() {
        Client client = Client.create(defaultClientConfig);

        WebResource service = client.resource(baseUri);
        ClientResponse response = service
                .path("api")
                .path("whiskies")
                .path("1")
                .accept(MediaType.APPLICATION_JSON)
                .delete(ClientResponse.class);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void testGetOneNonExistent() {
        Client client = Client.create(defaultClientConfig);

        WebResource service = client.resource(baseUri);
        ClientResponse response = service
                .path("api")
                .path("whiskies")
                .path("9999")
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get(ClientResponse.class);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

}