package com.shestero.sticky.views

import com.shestero.sticky.Workspace
import com.shestero.sticky.data.{NodeClass, StickyPlace}

import scala.util.chaining._

case class StickyPlaceView(place: StickyPlace)(implicit w: Workspace) {

  def html(): String = {
    import place._

    val cls: Option[NodeClass] = w.classes.get(classCode)

    val title: String = name getOrElse nodeId
    val a: String = cls.flatMap(_.url(nodeId)) match {
      case Some(url) => s"""<a href="$url">$title</a>"""
      case None => title
    }
    val link: String = status match {
      case "+" => s"""<input type="checkbox" checked> $a"""
      case "-" => s""" <strike>$a</strike>"""
      case "*" => s"""<input type="checkbox"> $a"""
      case _ => s" $a"
    }

    s"""\t<div class="note-link">
       |\t\t$link
       |\t</div>""".stripMargin
  }

  def debug(): Unit = {
    import place._
    val cls: Option[NodeClass] = w.classes.get(classCode)
    val tpe: String = cls.map(_.descr) getOrElse "<???>"
    val ico: String = status.pipe(_ match { case "" => " "; case c => c } )
    println(s"\t\t[$ico] $tpe $nodeId " + cls.flatMap(_.url(nodeId)).getOrElse(""))
  }
}
