package com.folksdev.mediastream.core.reader

import java.time.Instant
import java.time.temporal.{ChronoUnit, TemporalUnit}

import com.folksdev.mediastream.core.feed.{FeedBuilder, RssFeedBuilder}
import com.folksdev.mediastream.core.model.{Feed, Source}

trait ReaderService {
  def read(sources: List[Source]): List[Feed]
}

class ReaderServiceImpl(feedBuilder: FeedBuilder,
                       retentionFilter: Instant => Boolean) extends ReaderService{

  override def read(sources: List[Source]): List[Feed] = {
    sources.map(feedBuilder.getFeed)
      .map(_.map{feed =>
        feed.copy(articles = feed.articles.filter(article => retentionFilter(article.created)))
      })
      .flatMap(_.toOption)
  }
}
