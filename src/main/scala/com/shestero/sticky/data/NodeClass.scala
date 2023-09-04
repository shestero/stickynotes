package com.shestero.sticky.data

import cats.syntax.option._

import scala.util.Try

case class NodeClass(
                      code: String,
                      title: String,
                      format: Option[String] = None,
                      icon: Option[String] = None
                    ) {
  val urlDefined: Boolean =
    format.filter(_.contains("$")).isDefined
  def url(id: String): Option[String] =
    format.map(_.replace("$", id))
}


object NodeClass {
  def createFromLine(line: String): Try[NodeClass] =
    Try {
      line.split("\t") match {
        case Array(name, descr) => NodeClass(name.trim, descr.trim)
        case Array(name, descr, url) if url.trim.isEmpty => NodeClass(name.trim, descr.trim)
        case Array(name, descr, url) if url contains "$" => NodeClass(name.trim, descr.trim, url.trim.some)
        case a => throw new Exception(s"Cannot parse StickyPlace line: ${a.mkString(":")}")
      }
    }
}