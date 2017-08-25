package tryouts.vertx.restmongo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.List;
import java.util.stream.Collectors;

public class RestMongoVerticle extends AbstractVerticle {

    private static final String COLLECTION = "Whiskies";
    private MongoClient mongoClient;

    @Override
    public void start(Future<Void> startFuture) {

        mongoClient = MongoClient.createShared(vertx, config());

        createSomeData(
                (nothing) -> startWebApp(
                        (http) -> completeStartup(http, startFuture)
                ),
                startFuture
        );
    }

    private void completeStartup(AsyncResult<HttpServer> http, Future<Void> future) {
        if (http.succeeded()) {
            future.complete();
        } else {
            future.fail(http.cause());
        }
    }

    private void startWebApp(Handler<AsyncResult<HttpServer>> next) {
        Router router = Router.router(vertx);

        router.route("/api/whiskies*").handler(BodyHandler.create());
        router.get("/api/whiskies").handler(this::getAll);
        router.get("/api/whiskies/:id").handler(this::getOne);
        router.post("/api/whiskies").handler(this::addOne);
        router.put("/api/whiskies/:id").handler(this::updateOne);
        router.delete("/api/whiskies/:id").handler(this::deleteOne);

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(
                        config().getInteger("http.port", 8080),
                        next::handle
                );
    }

    private void getAll(RoutingContext routingContext) {
        mongoClient.find(COLLECTION, new JsonObject(), results -> {
            List<JsonObject> objects = results.result();
            List<Whisky> whiskies = objects.stream().map(Whisky::new).collect(Collectors.toList());
            routingContext.response()
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(Json.encodePrettily(whiskies));
        });
    }

    private void getOne(RoutingContext routingContext) {
        final String id = routingContext.request().getParam("id");
        if (id == null) {
            routingContext.response().setStatusCode(400).end();
        } else {
            mongoClient.findOne(COLLECTION, new JsonObject().put("_id", id), null, ar -> {
                if (ar.succeeded()) {
                    if (ar.result() == null) {
                        routingContext.response().setStatusCode(404).end();
                        return;
                    }
                    Whisky whisky = new Whisky(ar.result());
                    routingContext.response()
                            .setStatusCode(200)
                            .putHeader("content-type", "application/json; charset=utf-8")
                            .end(Json.encodePrettily(whisky));
                } else {
                    routingContext.response().setStatusCode(404).end();
                }
            });
        }
    }

    private void addOne(RoutingContext routingContext) {
        final Whisky whisky = Json.decodeValue(routingContext.getBodyAsString(),
                Whisky.class);

        mongoClient.insert(COLLECTION, whisky.toJson(), r ->
                routingContext.response()
                        .setStatusCode(201)
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .end(Json.encodePrettily(whisky.setId(r.result()))));
    }

    private void deleteOne(RoutingContext routingContext) {
        String id = routingContext.request().getParam("id");
        if (id == null) {
            routingContext.response().setStatusCode(400).end();
        } else {
            mongoClient.removeDocument(COLLECTION, new JsonObject().put("_id", id),
                    ar -> routingContext.response().setStatusCode(204).end());
        }
    }

    private void updateOne(RoutingContext routingContext) {
        final String id = routingContext.request().getParam("id");
        JsonObject json = routingContext.getBodyAsJson();
        if (id == null || json == null) {
            routingContext.response().setStatusCode(400).end();
        } else {
            mongoClient.updateCollection(COLLECTION,
                    new JsonObject().put("_id", id), // Select a unique document
                    // The update syntax: {$set, the json object containing the fields to update}
                    new JsonObject()
                            .put("$set", json),
                    v -> {
                        if (v.failed()) {
                            routingContext.response().setStatusCode(404).end();
                        } else {
                            routingContext.response()
                                    .putHeader("content-type", "application/json; charset=utf-8")
                                    .end(Json.encodePrettily(
                                            new Whisky(id, json.getString("name"),
                                                    json.getString("origin"))));
                        }
                    });
        }
    }

    private void createSomeData(Handler<AsyncResult<Void>> next, Future<Void> future) {
        Whisky bowmore = new Whisky("Bowmore 15 Years Laimrig", "Scotland, Islay");
        Whisky talisker = new Whisky("Talisker 57° North", "Scotland, Island");

        mongoClient.count(COLLECTION, new JsonObject(), count -> {
            if (count.succeeded()) {
                if (count.result() == 0) {
                    mongoClient.insert(COLLECTION, bowmore.toJson(), ar -> {
                       if (ar.failed()) {
                           future.fail(ar.cause());
                       } else {
                           mongoClient.insert(COLLECTION, talisker.toJson(), ar2 -> {
                              if (ar2.failed()) {
                                  future.fail(ar2.cause());
                              } else {
                                  next.handle(Future.<Void>succeededFuture());
                              }
                           });
                       }
                    });
                } else {
                    next.handle(Future.<Void>succeededFuture());
                }
            } else {
                future.fail(count.cause());
            }
        });
    }
}