package com.folksdev.mediastream.core.source

import com.folksdev.mediastream.core.model.Source

trait SourceService {
  def sources : List[Source]
}

class CsvSourceService(resourceName: String) extends SourceService{

  override lazy val sources: List[Source] = {
    val res = io.Source.fromResource(resourceName)
    val sources = res.getLines()
      .map(_.split(",").map(_.trim))
      .map(cols => Source(cols(0).toInt, cols(1), cols(2), cols(3), cols(4)))
      .toList


    for (line <- res.getLines) {
      val cols = line.split(",").map(_.trim)
      // do whatever you want with the columns here
      println(s"${cols(0)}|${cols(1)}|${cols(2)}|${cols(3)}")
    }
    res.close()
    sources
  }
}

