package tryouts.vertx.kotlin

import com.google.gson.Gson
import io.vertx.core.Future
import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler
import java.util.concurrent.atomic.AtomicInteger
import kotlin.reflect.KClass

data class Whisky(val id: String, val name: String, val origin: String)

interface WhiskyService {
    fun getWhisky(id: String): Future<Whisky>
    fun addWhisky(whisky: Whisky): Future<Whisky>
    fun delWhisky(id: String): Future<Unit>
}

class MemoryWhiskyService(): WhiskyService {
    val whiskies = HashMap<String, Whisky>()
    val idGen = AtomicInteger(0)

    init {
        addWhisky(Whisky("0", "Talisker 57° North", "Scotland, Island"))
        addWhisky(Whisky("1", "Bowmore 15 Years Laimrig", "Scotland, Islay"))
    }

    override fun getWhisky(id: String): Future<Whisky> {
        return if (whiskies.containsKey(id)) Future.succeededFuture(whiskies[id])
            else Future.failedFuture(IllegalAccessException("No whisky found by $id"))
    }

    override fun addWhisky(whisky: Whisky): Future<Whisky> {
        val newWhisky = Whisky(idGen.getAndIncrement().toString(), whisky.name, whisky.origin)
        whiskies[newWhisky.id] = newWhisky
        return Future.succeededFuture(newWhisky)
    }

    override fun delWhisky(id: String): Future<Unit> {
        whiskies.remove(id);
        return Future.succeededFuture()
    }

}

object Main {
    val gson = Gson()

    @JvmStatic fun main(args: Array<String>) {
        val whiskyService = MemoryWhiskyService()

        val port = 8080;
        val vertx = Vertx.vertx()
        val server = vertx.createHttpServer()

        val router = Router.router(vertx)
        router.route("/api/whiskies/*").handler(BodyHandler.create())

        router.get("/:whiskyId").handler { ctx ->
            val whiskyId = ctx.request().getParam("whiskyId")
            jsonResponse(ctx, whiskyService.getWhisky(whiskyId))
        }

        router.post("/").handler { ctx ->
            val whisky = jsonRequest<Whisky>(ctx, Whisky::class)
            jsonResponse(ctx, whiskyService.addWhisky(whisky))
        }

        router.delete("/:whiskyId").handler { ctx ->
            val whiskyId = ctx.request().getParam("whiskyId")
            jsonResponse(ctx, whiskyService.delWhisky(whiskyId))
        }

        server.requestHandler() { router.accept(it) }.listen(port) {
            if (it.succeeded()) println("Server listening at $port")
            else println(it.cause())
        }
    }

    fun <T> jsonRequest(ctx: RoutingContext, clazz: KClass<out Any>): T =
        gson.fromJson(ctx.bodyAsString, clazz.java) as T


    fun <T> jsonResponse(ctx: RoutingContext, future: Future<T>) {
        future.setHandler {
            if (it.succeeded()) {
                if (it.result() == null) "" else gson.toJson(it.result())
            } else {
                ctx.response().setStatusCode(500).end(it.cause().toString())
            }
        }
    }
}

