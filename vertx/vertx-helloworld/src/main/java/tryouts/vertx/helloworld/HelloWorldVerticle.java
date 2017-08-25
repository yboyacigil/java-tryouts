package tryouts.vertx.helloworld;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class HelloWorldVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) {
        vertx
            .createHttpServer()
            .requestHandler(request -> {
                request.response().end("Hello world, from Vert.x application!");
            })
            .listen(
                    config().getInteger("http.port", 8080),
                    result -> {
                        if (result.succeeded()) {
                            startFuture.complete();
                        } else {
                            startFuture.fail(result.cause());
                        }
                    });
    }
}
