package com.shestero.sticky.data

import cats.syntax.traverse._

import scala.io.Source
import scala.util.Try

case class Note(
               id: String,
               subject: String,
               places: Seq[StickyPlace],
               text: String
               ) {
  val codeMap: Map[String, Seq[StickyPlace]] = places.groupBy(_.classCode)
}

object Note {
  def fromSource(codes: Set[String])(id: String, source: Source): Try[Note] = {
    val lines = source.getLines()
    val subject = lines.next()
    for {
      places <- lines.takeWhile(_.trim.nonEmpty).toSeq.traverse(StickyPlace.createFromLine(codes))
      text = lines.mkString("\n")
    } yield Note(id, subject, places, text)
  }
}