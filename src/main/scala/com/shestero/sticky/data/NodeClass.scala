package com.shestero.sticky.data

import cats.syntax.option._

import scala.util.Try

case class NodeClass(
                      code: String,
                      descr: String,
                      urlPattern: Option[String] = None
                    ) {
  val urlDefined: Boolean =
    urlPattern.filter(_.contains("$")).isDefined
  def url(id: String): Option[String] =
    urlPattern.map(_.replace("$", id))
}


object NodeClass {
  def createFromLine(line: String): Try[NodeClass] =
    Try {
      line.split("\t") match {
        case Array(name, descr) => NodeClass(name.trim, descr.trim)
        case Array(name, descr, urlp) if urlp contains "$" => NodeClass(name.trim, descr.trim, urlp.trim.some)
        case a => throw new Exception(s"Cannot parse StickyPlace line: ${a.mkString(":")}")
      }
    }
}