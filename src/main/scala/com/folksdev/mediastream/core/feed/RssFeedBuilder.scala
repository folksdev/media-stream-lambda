package com.folksdev.mediastream.core.feed

import java.net.URL

import com.folksdev.mediastream.core.model.{Article, Feed, Source}
import com.rometools.rome.feed.synd.{SyndCategory, SyndContent, SyndEntry, SyndLink}
import com.rometools.rome.io.{SyndFeedInput, XmlReader}

import scala.util.{Failure, Success, Try}
import scala.jdk.CollectionConverters._

object RssFeedBuilder extends FeedBuilder {

  private val input = new SyndFeedInput

  override def getFeed(source: Source): Try[Feed] = Try {
    val feedUrl = new URL(source.sourceUrl)
    val feed = input.build(new XmlReader(feedUrl))

    val converted = feed.getEntries.asScala.toList
      .flatMap(syndEntry => convert(syndEntry))

    println(s"Scraping: ${source.name}. Initial Size: ${feed.getEntries
      .size()} - After Conversion: ${converted.size}")
    Feed(source, converted)
  }

  private def convert(syndEntry: SyndEntry): Option[Article] =
    Try {
      val title = syndEntry.getTitle.replaceAll("\\s+", " ").trim
      val uri = syndEntry.getLink

      require(title.nonEmpty)
      require(uri.nonEmpty)

      val content: List[String] =
        syndEntry.getContents.asScala
          .map((x: SyndContent) => x.getValue)
          .toList
      val links: List[String] =
        syndEntry.getLinks.asScala.toSet
          .map((x: SyndLink) => x.getHref)
          .toList
      val categories: List[String] =
        syndEntry.getCategories.asScala.toList
          .map((x: SyndCategory) => x.getName)
      val description =
        Try(syndEntry.getDescription.getValue.replaceAll("\\s+", " ").trim)
          .getOrElse("")

      Article(
        title,
        description,
        uri,
        links,
        content,
        categories,
        "",
        Option(syndEntry.getUpdatedDate)
          .getOrElse(syndEntry.getPublishedDate)
          .toInstant
      )
    } match {
      case Success(value) => Some(value)
      case Failure(exception) =>
        println(
          "Exception occured during SyndEntry -> Entry conversion",
          exception
        )
        None
    }
}
