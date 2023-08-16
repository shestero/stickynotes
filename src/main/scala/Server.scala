import cats.effect._
import org.http4s.server.Router
import org.http4s.blaze.server.BlazeServerBuilder

object Server extends IOApp with Router {

  override def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO]
      .bindHttp(8800, "0.0.0.0")
      .withHttpApp(Router("/" -> routes).orNotFound)
      .resource
      .useForever
      .as(ExitCode.Success)
}
