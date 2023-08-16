package com.shestero.sticky.data

import scala.util.Try

case class StickyPlace(
               nodeId: String,
               classCode: String,
               status: String = ""
               )

object StickyPlace {
  def createFromLine(codes: Set[String])(line: String): Try[StickyPlace] =
    Try {
      line match {
        // TODO
        case s"$code:$id" if codes contains code => StickyPlace(id.trim, code)
        case s"-$code:$id" if codes contains code => StickyPlace(id.trim, code, "-")
        case s"+$code:$id" if codes contains code => StickyPlace(id.trim, code, "+")
        case s"*$code:$id" if codes contains code => StickyPlace(id.trim, code, "*")
        case s => throw new Exception(s"Cannot parse StickyPlace line: $s")
      }
    }
}
