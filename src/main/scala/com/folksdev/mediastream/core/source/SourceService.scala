package com.folksdev.mediastream.core.source

import com.folksdev.mediastream.core.model.Source

import scala.io.BufferedSource

trait SourceService {
  def sources : List[Source]
}

class CsvSourceService(bufferedSource: BufferedSource) extends SourceService{

  override lazy val sources: List[Source] = bufferedSource.getLines()
      .map(_.split(",").map(_.trim))
      .map(cols => Source(cols(0).toInt, cols(1), cols(2), cols(3), cols(4)))
      .toList
}

