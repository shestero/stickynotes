package com.shestero.sticky.data

import cats.syntax.option._
import scala.util.Try

case class StickyPlace(
                        nodeId: String,
                        classCode: String,
                        name: Option[String],
                        status: String = ""
                      )

object StickyPlace {
  def createFromLine(codes: Set[String])(line: String): Try[StickyPlace] =
    Try {
      val (first, name) = line.trim.split(" ").toList match {
        case List(one) => one -> none
        case h :: t => h -> t.mkString(" ").some
        case _ => throw new Exception("StickyPlace.createFromLine empty line")
      }
      first match {
        // TODO
        case s"$code:$id" if codes contains code => StickyPlace(id.trim, code, name)
        case s"-$code:$id" if codes contains code => StickyPlace(id.trim, code, name, "-")
        case s"+$code:$id" if codes contains code => StickyPlace(id.trim, code, name, "+")
        case s"*$code:$id" if codes contains code => StickyPlace(id.trim, code, name, "*")
        case s => throw new Exception(s"Cannot parse StickyPlace line: $s")
      }
    }
}
