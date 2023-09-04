package com.shestero.sticky

import com.shestero.sticky.data.{NodeClass, Note}
import cats.syntax.either._
import cats.syntax.traverse._
import io.circe.generic.auto._
import io.circe._
import io.circe.yaml

import java.io.{File, FileReader}
import scala.io.Source
import scala.util.chaining._

class Workspace(classesFile: String = "data/classes.yaml", notesDir: String = "data/notes")
{
  private def fromTSV: Seq[NodeClass] =
    Source
      .fromFile(classesFile)
      .getLines()
      .flatMap(line => NodeClass.createFromLine(line).tap(_.failed.foreach(System.err.println)).toOption)
      .toSeq

  private def fromYaml: Seq[NodeClass] =
    yaml.parser
      .parse(new FileReader(classesFile))
      .leftMap(err => err: Error)
      .map(_ \\ "classes")
      .tap(_.map("JSON: " + _).foreach(println))
      .flatMap(_.flatTraverse(_.as[List[NodeClass]]))
      .valueOr(throw _)


  // Read classes:
  val classes: Map[String, NodeClass] =
    fromYaml
    .groupBy(_.code)
    .collect {
      case (code, Seq(nodeClass)) =>
        (code, nodeClass)
      case (code, sq@Seq(nodeClass, _*)) =>
        System.err.println(s"Warning: non-unique by class code '$code' in $classesFile : $sq")
        (code, nodeClass)
    }

  // Read notes:
  private def getListOfFiles(dir: String): Seq[File] = getListOfFiles(new File(dir))

  private def getListOfFiles(d: File): Seq[File] =
    Option.when(d.exists && d.isDirectory) {
      d.listFiles
        .filter(_.isFile)
        .filterNot(_.getName.startsWith("_"))
        .filter(_.getName.endsWith(".txt"))
    }.toSeq.flatten

  val notes: Seq[Note] =
    getListOfFiles(notesDir)
      .map(file => Note.fromSource(classes.keySet)(file.getName, Source.fromFile(file)))
      .flatMap(_.tap(_.failed.foreach(System.err.println)).toOption)
      // .groupBy(_.id)
}
