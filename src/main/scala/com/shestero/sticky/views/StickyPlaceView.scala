package com.shestero.sticky.views

import com.shestero.sticky.Workplace
import com.shestero.sticky.data.{NodeClass, StickyPlace}

import scala.util.chaining._

case class StickyPlaceView(place: StickyPlace)(implicit w: Workplace) {

  def html(): String = {
    import place._

    val cls: Option[NodeClass] = w.classes.get(classCode)

    val link0 = status match {
      case "+" => s"""<input type="checkbox" checked> $nodeId"""
      case "-" => s"""<input type="checkbox"> <strike>$nodeId</strike>"""
      case _ => s"""<input type="checkbox"> $nodeId"""
    }

    val link = cls.flatMap(_.url(nodeId)) match {
      case Some(url) => s"""<a href="$url">$link0</a>"""
      case None => link0
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
