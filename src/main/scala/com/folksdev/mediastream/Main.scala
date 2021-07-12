package com.folksdev.mediastream

import java.time.Instant
import java.time.temporal.ChronoUnit

import com.folksdev.mediastream.core.feed.RssFeedBuilder
import com.folksdev.mediastream.core.model.Source
import com.folksdev.mediastream.core.reader.ReaderServiceImpl
import com.folksdev.mediastream.core.source.CsvSourceService

object Main extends App {

  val defaultSource = Source(0, "", "", "", "http://feeds.dzone.com/java")

  val sources = new CsvSourceService("Sources.csv").sources

  sources.foreach(source =>
    println(List(source.id, source.name).mkString(": "))
  )

  //val feed = RssFeedBuilder.getFeed(defaultSource)
  val now = Instant.now().minus(1, ChronoUnit.DAYS).minus(1, ChronoUnit.HALF_DAYS)
  val retentionFilter: Instant => Boolean = instant => instant.isAfter(now)

  val feeds = new ReaderServiceImpl(RssFeedBuilder, retentionFilter)
    .read(sources)
    .foreach(_.articles.foreach(a => println(a.title)))

}
