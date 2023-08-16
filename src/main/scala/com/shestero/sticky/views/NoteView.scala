package com.shestero.sticky.views

import com.shestero.sticky.Workplace
import com.shestero.sticky.data.Note

case class NoteView(note: Note)(implicit w: Workplace) {

  def html(): String = {
    import note._

    val links = w.classes
      .map { case (code, cls) => cls.descr -> note.codeMap.get(code) }
      .collect { case (descr, Some(places)) =>
        s"""<div class="note-link-block">
           |  $descr/s:<br>
           |  ${places.map(StickyPlaceView.apply).map(_.html()).mkString("\n")}
           |</div>""".stripMargin
      }

    s"""<div class="note">
       |  <div class="note-id"><a href="/note/$id">$id</a></div>
       |  <div class="note-subj">Subj: $subject</div>
       |  <div class="note-links">
       |    ${links.mkString("\n")}
       |  </div>
       |  <div class="note-text">$text</div>
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
