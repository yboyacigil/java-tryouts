package tryouts.vertx.restjdbc;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(VertxUnitRunner.class)
public class RestJdbcVerticleTest {

    private Vertx vertx;

    @Before
    public void setUp(TestContext context) {
        vertx = Vertx.vertx();
        DeploymentOptions options = new DeploymentOptions()
            .setConfig(new JsonObject()
                    .put("http.port", 8080)
                    .put("url", "jdbc:hsqldb:mem:test?shutdown=true")
                    .put("driver_class", "org.hsqldb.jdbcDriver")
            );
        vertx.deployVerticle(RestJdbcVerticle.class.getName(), options, context.asyncAssertSuccess());
    }

    @After
    public void tearDown(TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }


    @Test
    public void testWeCanGetAll(TestContext context) {
        Async async = context.async();

        vertx.createHttpClient().get(8080, "localhost", "/api/whiskies")
            .handler(response -> {
                context.assertEquals(response.statusCode(), 200);
                context.assertTrue(response.headers().get("content-type").contains("application/json"));

                response.bodyHandler(body -> {
                    JsonArray array = body.toJsonArray();
                    context.assertTrue(array.size() > 0);
                    async.complete();
                });
            })
            .end();
    }

    @Test
    public void testThatWeCanAddOne(TestContext context) {
        Async async = context.async();

        final String json = Json.encodePrettily(new Whisky("Jameson", "Ireland"));
        final String length = Integer.toString(json.length());

        vertx.createHttpClient().post(8080, "localhost", "/api/whiskies")
            .putHeader("content-type", "application/json; charset=utf-8")
            .putHeader("content-length", length)
            .handler(response -> {
                context.assertEquals(response.statusCode(), 201);
                context.assertTrue(response.headers().get("content-type").contains("application/json"));

                response.bodyHandler(body -> {
                    final Whisky whisky = Json.decodeValue(body.toString(), Whisky.class);
                    context.assertEquals(whisky.getName(), "Jameson");
                    context.assertEquals(whisky.getOrigin(), "Ireland");
                    context.assertNotNull(whisky.getId());

                    async.complete();
                });
            })
            .write(json)
            .end();
    }

}