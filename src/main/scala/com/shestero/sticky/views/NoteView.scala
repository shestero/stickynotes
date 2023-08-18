package com.shestero.sticky.views

import com.shestero.sticky.Workspace
import com.shestero.sticky.data.{Note, StickyPlace}

case class NoteView(note: Note)(implicit w: Workspace) {

  def html(): String = {
    import note._

    val fields = note.places
      .collect { case StickyPlace(nodeId, field, name, _) if !w.classes.get(field).exists(_.urlDefined) =>
        s"""<div class="note-field-value">
           |  <span class="note-field">${ w.classes.get(field).map(_.descr) getOrElse field }</span>:
           |  <span class="note-value">${ name getOrElse nodeId }</span>
           |</div>""".stripMargin
      }

    val links = w.classes
      .map { case (code, cls) => cls.descr -> note.codeMap.get(code) }
      .collect { case (descr, Some(places)) if places.exists(_.nodeId.nonEmpty) =>
        s"""<div class="note-link-block">
           |  $descr/s:<br>
           |  ${places.map(StickyPlaceView.apply).map(_.html()).mkString("\n")}
           |</div>""".stripMargin
      }

    s"""<div class="note">
       |  <div class="note-id"><a href="/note/$id">$id</a></div>
       |  <div class="note-subj"><u>$subject</u></div>
       |  <div class="note-fields">
       |    ${fields.mkString("\n")}
       |  </div>
       |<div class="note-links">
       |    ${links.mkString("\n")}
       |  </div>
       |       |  <span class="note-text">$text</span>
       |</div>""".stripMargin
  }

  def debug(): Unit = {
    import note._
    println("NOTE: id=" + id)
    println("Subject: " + subject)
    w.classes
      .map{ case (code, cls) => cls.descr -> codeMap.get(code) }
      .collect { case (descr, Some(places))=>
        println(s"\t$descr/s:")
        places.map(StickyPlaceView.apply).foreach(_.debug())
      }
    println(text)
    println()
  }
}
