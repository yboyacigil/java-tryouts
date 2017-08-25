package tryouts.vertx.restmongo;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.*;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(VertxUnitRunner.class)
public class RestMongoVerticleTest {

    private static MongodProcess Mongo;
    private static int MongoPort = 12345;

    private Vertx vertx;

    @BeforeClass
    public static void initialize() throws IOException {

        MongodStarter starter = MongodStarter.getDefaultInstance();
        IMongodConfig mongodConfig = new MongodConfigBuilder()
                .version(Version.Main.PRODUCTION)
                .net(new Net(MongoPort, Network.localhostIsIPv6()))
                .build();

        MongodExecutable mongodExecutable = starter.prepare(mongodConfig);
        Mongo = mongodExecutable.start();
    }

    @AfterClass
    public static void shutdown() {
        Mongo.stop();
    }

    @Before
    public void setUp(TestContext context) {
        vertx = Vertx.vertx();
        DeploymentOptions options = new DeploymentOptions()
                .setConfig(new JsonObject()
                        .put("http.port", 8080)
                        .put("db_name", "whiskies-test")
                        .put("connection_string", "mongodb://localhost:" + MongoPort)
                );
        vertx.deployVerticle(RestMongoVerticle.class.getName(), options, context.asyncAssertSuccess());
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


}