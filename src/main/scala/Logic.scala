import cats.effect.IO
import scala.util.chaining._

import com.shestero.sticky.Workplace
import com.shestero.sticky.views.NoteView
trait Logic {

  implicit val w = new Workplace

  w.notes.map(NoteView.apply).foreach(_.debug())
  w.notes.map(NoteView.apply).map(_.html()).foreach(println)

  def html(body: String): String =
    s"""<html>
       |<head>
       |  <title>Sticky Notes Demo</title>
       |  <link rel="stylesheet" href="/static/style.css">
       |</head>
       |<body>
       |$body
       |</body>
       |</html>""".stripMargin

  val logicAll: Unit => IO[Either[Unit, String]] = _ =>
    IO.pure(Right[Unit, String](html(w.notes.map(NoteView.apply).map(_.html()).mkString("\n"))))

  // return one card by global id
  def logic1(id: String): IO[Either[Unit, String]] =
    IO.pure(Right[Unit, String](html(w.notes.filter(_.id==id).map(NoteView.apply).map(_.html()).mkString("\n"))))

}
