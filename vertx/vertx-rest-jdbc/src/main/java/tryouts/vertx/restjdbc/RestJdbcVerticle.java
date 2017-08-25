package tryouts.vertx.restjdbc;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.sql.UpdateResult;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.List;
import java.util.stream.Collectors;

public class RestJdbcVerticle extends AbstractVerticle {

    private JDBCClient jdbc;

    @Override
    public void start(Future<Void> startFuture) {
        jdbc = JDBCClient.createShared(vertx, config(), "Whiskies");

        startBackend(
            connection -> createSomeData(
                connection,
                (nothing) -> startWebApp(
                    (http) -> completeStartup(http, startFuture)
                ),
                startFuture
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

    private void createSomeData(AsyncResult<SQLConnection> result, Handler<AsyncResult<Void>> next, Future<Void> future) {
        if (result.failed()) {
            future.fail(result.cause());
        } else {
            SQLConnection connection = result.result();

            connection.execute("CREATE TABLE IF NOT EXISTS Whisky (id INTEGER IDENTITY, name varchar(100), origin varchar(100))",
                    ar -> {
                        if (ar.failed()) {
                            future.fail(ar.cause());
                            connection.close();
                            return;
                        }

                        connection.query("SELECT * FROM Whisky",
                                select -> {
                                    if (select.failed()) {
                                        future.fail(select.cause());
                                        connection.close();
                                        return;
                                    }

                                    if (select.result().getNumRows() == 0) {
                                        insert(
                                                new Whisky("Bowmore 15 Years Laimrig", "Scotland, Islay"),
                                                connection,
                                                (v) -> insert(
                                                        new Whisky("Talisker 57° North", "Scotland, Island"),
                                                        connection,
                                                        (r) -> {
                                                            next.handle(Future.<Void>succeededFuture());
                                                            connection.close();
                                                        }
                                                )
                                        );
                                    } else {
                                        next.handle(Future.<Void>succeededFuture());
                                        connection.close();
                                    }
                                }
                        );
                    }
            );
        }
    }

    private void insert(Whisky whisky, SQLConnection connection, Handler<AsyncResult<Whisky>> next) {
        String sql = "INSERT INTO Whisky (name, origin) VALUES (?, ?)";

        connection.updateWithParams(
                sql,
                new JsonArray().add(whisky.getName()).add(whisky.getOrigin()),
                ar -> {
                    if (ar.failed()) {
                        next.handle(Future.failedFuture(ar.cause()));
                        return;
                    }
                    UpdateResult result = ar.result();

                    Whisky w = new Whisky(result.getKeys().getInteger(0), whisky.getName(), whisky.getOrigin());
                    next.handle(Future.succeededFuture(w));
                }
        );
    }

    private void startBackend(Handler<AsyncResult<SQLConnection>> next, Future<Void> future) {
        jdbc.getConnection(ar -> {
            if (ar.failed()) {
                future.fail(ar.cause());
            } else {
                next.handle(Future.succeededFuture(ar.result()));
            }
        });
    }

    private void updateOne(RoutingContext routingContext) {
        String id = routingContext.request().getParam("id");
        JsonObject json = routingContext.getBodyAsJson();
        if (id == null || json == null) {
            routingContext.response().setStatusCode(400).end();
        } else {
            jdbc.getConnection(ar -> {
                SQLConnection connection = ar.result();

                final Integer whiskyId = Integer.valueOf(id);
                final String sql = "UPDATE Whisky SET name = ?, origin = ? WHERE id = ?";

                connection.updateWithParams(
                    sql,
                    new JsonArray()
                        .add(json.getString("name"))
                        .add(json.getString("origin"))
                        .add(whiskyId),
                    updateResult -> {
                        if (updateResult.result().getUpdated() == 0) {
                            routingContext.response().setStatusCode(404).end();
                        } else {
                            Whisky whisky = new Whisky(whiskyId, json.getString("name"), json.getString("origin"));

                            routingContext.response()
                                .putHeader("content-type", "application/json; charset=utf-8")
                                .end(Json.encodePrettily(whisky));
                        }
                        connection.close();
                    }
                );
            });
        }
    }

    private void deleteOne(RoutingContext routingContext) {
        String id = routingContext.request().getParam("id");
        if (id == null) {
            routingContext.response().setStatusCode(400).end();
        } else {
            jdbc.getConnection(ar -> {
                SQLConnection connection = ar.result();

                final Integer whiskyId = Integer.valueOf(id);
                final String sql = "DELETE FROM Whisky WHERE id = ?";
                connection.updateWithParams(
                   sql,
                   new JsonArray().add(whiskyId),
                   updateResult -> {
                       if (updateResult.result().getUpdated() == 0) {
                           routingContext.response().setStatusCode(404).end();
                       } else {
                           routingContext.response().setStatusCode(204).end();
                       }
                       connection.close();
                   }
               );
            });
        }
    }

    private void addOne(RoutingContext routingContext) {
        final Whisky whisky = Json.decodeValue(routingContext.getBodyAsString(), Whisky.class);
        jdbc.getConnection(ar -> {
            SQLConnection connection = ar.result();

            final String sql = "INSERT INTO Whisky (name, origin) VALUES (?, ?)";
            connection.updateWithParams(
                    sql,
                    new JsonArray().add(whisky.getName()).add(whisky.getOrigin()),
                    updateResult -> {
                        Whisky w = new Whisky(updateResult.result().getKeys().getInteger(0), whisky.getName(), whisky.getOrigin());
                        routingContext.response()
                                .setStatusCode(201)
                                .putHeader("content-type", "application/json; charset=utf-8")
                                .end(Json.encodePrettily(whisky));
                        connection.close();
                    }
            );
        });
    }

    private void getOne(RoutingContext routingContext) {
        final String id = routingContext.request().getParam("id");
        if (id == null) {
            routingContext.response().setStatusCode(400).end();
        } else {
            final Integer whiskyId = Integer.valueOf(id);
            jdbc.getConnection(ar -> {
                SQLConnection connection = ar.result();
                connection.queryWithParams(
                    "SELECT * FROM Whisky WHERE id = ?",
                    new JsonArray().add(whiskyId),
                    result -> {
                        if (result.result().getNumRows() == 0) {
                            routingContext.response().setStatusCode(404).end();
                        } else {
                            Whisky whisky = result.result()
                                .getRows()
                                .stream()
                                .map(this::toWhisky)
                                .findFirst()
                                .get();

                            routingContext.response()
                                    .putHeader("content-type", "application/json; charset=utf-8")
                                    .end(Json.encodePrettily(whisky));
                        }
                        connection.close();
                    });
            });
        }
    }

    private void getAll(RoutingContext routingContext) {
        jdbc.getConnection(ar -> {
            SQLConnection connection = ar.result();

            connection.query("SELECT * FROM Whisky", result -> {
                List<Whisky> whiskies =
                        result.result()
                            .getRows()
                            .stream()
                            .map(this::toWhisky)
                            .collect(Collectors.toList());

                System.out.println(whiskies);

                routingContext.response()
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .end(Json.encodePrettily(whiskies));

                connection.close();
            });
        });
    }

    private Whisky toWhisky(JsonObject json) {
        return new Whisky(
                json.getInteger("ID"),
                json.getString("NAME"),
                json.getString("ORIGIN"));
    }
}
