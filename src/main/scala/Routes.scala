import cats.effect.IO
import org.http4s.HttpRoutes
import sttp.tapir.files.staticFilesGetServerEndpoint
import sttp.tapir._
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.swagger.bundle.SwaggerInterpreter

object Routes extends Logic {

  val app = "StickyNotes"
  val ver = "demo"

  private val rootEndpoint =
    endpoint.description("List all notes")
      .get.in("")
      .out(htmlBodyUtf8)
      .serverLogic(logicAll)

  private val cardEndpoint =
    endpoint.description("View specific note")
      .get.in("note" / path[String])
      .out(htmlBodyUtf8)
      .serverLogic(logic1 _)

  private val staticEndpoint = staticFilesGetServerEndpoint[IO]("static")("static")

  private val endpoints = rootEndpoint :: cardEndpoint :: staticEndpoint :: Nil

  private val swagger = SwaggerInterpreter().fromServerEndpoints(endpoints, app, ver)

  val routes: HttpRoutes[IO] = Http4sServerInterpreter[IO]().toRoutes(endpoints ++ swagger)
}
