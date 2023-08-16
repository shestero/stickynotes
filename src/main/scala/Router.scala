import cats.effect.IO
import org.http4s.HttpRoutes
import sttp.tapir.files.staticFilesGetServerEndpoint
import sttp.tapir._
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.swagger.bundle.SwaggerInterpreter

trait Router extends Logic {

  val app = "StickyNotes"
  val ver = "demo"

  val rootEndpoint =
    endpoint.description("List all notes")
      .get.in("")
      .out(htmlBodyUtf8)
      .serverLogic(logicAll)

  val cardEndpoint =
    endpoint.description("View specific note")
      .get.in("note" / path[String])
      .out(htmlBodyUtf8)
      .serverLogic(logic1 _)

  val staticEndpoint = staticFilesGetServerEndpoint[IO]("static")("static")

  val endpoints = rootEndpoint :: cardEndpoint :: staticEndpoint :: Nil

  val swagger = SwaggerInterpreter().fromServerEndpoints(endpoints, app, ver)
  val routes: HttpRoutes[IO] = Http4sServerInterpreter[IO]().toRoutes(endpoints ++ swagger)

}
